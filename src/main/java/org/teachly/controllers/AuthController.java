package org.teachly.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teachly.dto.*;
import org.teachly.services.AuthService;
import org.teachly.services.UserService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.authenticate(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = userService.registerUser(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody ProfileRequest profileRequest) {
        UserProfileDto userProfile = userService.updateProfile(profileRequest);
        return ResponseEntity.ok(userProfile); // Возвращаем обновленные данные пользователя
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile() {
        UserProfileDto profile = userService.getUserProfile();
        return ResponseEntity.ok(profile);
    }
}
