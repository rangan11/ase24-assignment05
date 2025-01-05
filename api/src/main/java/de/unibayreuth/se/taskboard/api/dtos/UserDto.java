package de.unibayreuth.se.taskboard.api.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(
        @Nullable UUID id,
        @Nullable LocalDateTime createdAt, // Nullable at user creation from external
        @NonNull String name
) { }
