package org.teachly.dto;

import lombok.*;

import java.util.Collections;
import java.util.List;

import org.teachly.models.ChatInfo;
import org.teachly.models.ClassInfo;

/**
 * Ответ на аутентификацию пользователя.
 */
@Getter
@Builder
public final class AuthResponse {
    private final String token;
    private final UserProfileDto profile;
    @Builder.Default
    private final List<ClassInfo> classes = Collections.emptyList();
    @Builder.Default
    private final List<ChatInfo> chats = Collections.emptyList();
}
