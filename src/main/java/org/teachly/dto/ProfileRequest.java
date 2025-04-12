package org.teachly.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import org.teachly.models.Role;

import java.util.Set;

/**
 * Запрос на создание/обновление профиля пользователя.
 */
@Data
@Builder
public final class ProfileRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "At least one subject is required")
    @Size(min = 1, message = "At least one subject is required")
    @Builder.Default
    private Set<String> subjects = Set.of();

    @Size(max = 500, message = "About cannot be longer than 500 characters")
    private String about;
}
