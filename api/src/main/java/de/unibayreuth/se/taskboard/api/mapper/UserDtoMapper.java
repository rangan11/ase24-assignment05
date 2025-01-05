package de.unibayreuth.se.taskboard.api.mapper;

import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.business.domain.User;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean
@NoArgsConstructor
public abstract class UserDtoMapper {

    public abstract UserDto fromBusiness(@NonNull User source);

    @Mapping(target = "createdAt", ignore = true)
    public abstract User toBusiness(UserDto source);

    public UserDto fromOptional(Optional<User> source) {
        return source.map(this::fromBusiness).orElse(null);
    }


}
