package de.unibayreuth.se.taskboard;

import de.unibayreuth.se.taskboard.api.dtos.TaskDto;
import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.api.mapper.TaskDtoMapper;
import de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper;
import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.domain.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class TaskBoardSystemTests extends AbstractSystemTest {

    @Autowired
    private TaskDtoMapper taskDtoMapper;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Test
    void getAllCreatedTasks() {
        List<Task> createdTasks = TestFixtures.createTasks(taskService);

        List<Task> retrievedTasks = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/tasks")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdTasks.size()))
                .and()
                .extract().jsonPath().getList("$", TaskDto.class)
                .stream()
                .map(taskDtoMapper::toBusiness)
                .toList();

        assertThat(retrievedTasks)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .containsExactlyInAnyOrderElementsOf(createdTasks);
    }

    @Test
    void createAndDeleteTask() {
        Task createdTask = taskService.create(
                TestFixtures.getTasks().getFirst()
        );

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .delete("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(400);

    }

    @Test
    void getAllUsers() {
        List<User> createdUsers = TestFixtures.createUsers(userService);

        List<User> retrievedUsers = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdUsers.size()))
                .and()
                .extract().jsonPath().getList("$", UserDto.class)
                .stream()
                .map(userDtoMapper::toBusiness)
                .toList();

        assertThat(retrievedUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt")
                .containsExactlyInAnyOrderElementsOf(createdUsers);
    }

    @Test
    void createAndGetUserById() {
        var userDto = new UserDto(
                null,
                null,
                "RP Test"
        );
        var createdUser = given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        var retrievedUser = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/{id}", createdUser.getId())
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        assertThat(retrievedUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(createdUser);

        when()
                .get("/api/users/{id}", "non-existing-id")
                .then()
                .statusCode(400);
    }

}