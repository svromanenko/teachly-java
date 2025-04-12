package org.teachly.dto;

import lombok.*;
import org.teachly.models.Role;
import org.teachly.models.User;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String name;
    private Role role;
    private Set<String> subjects;
    private String about;

    // Фабричный метод для конвертации User -> UserProfileDto
    public static UserProfileDto from(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getName(),
                user.getRole(),
                user.getSubjects(), // Уже unmodifiableSet
                user.getAbout()
        );
    }
}
