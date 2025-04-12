package org.teachly.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.teachly.dto.AuthRequest;
import org.teachly.dto.AuthResponse;
import org.teachly.dto.ProfileRequest;
import org.teachly.dto.UserProfileDto;
import org.teachly.exceptions.JwtAuthenticationException;
import org.teachly.models.User;
import org.teachly.repositories.UserRepository;
import org.teachly.security.JwtTokenProvider;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse registerUser(AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        User newUser = userRepository.save(user);

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + newUser.getRole().toString())
        );

        String token = jwtTokenProvider.createToken(newUser.getId(), newUser.getUsername(), authorities);

        return AuthResponse.builder()
                .token(token)
                .profile(UserProfileDto.from(user))
                .build();
    }

    public UserProfileDto updateProfile(ProfileRequest profileRequest) {

        User user = userRepository.findById(getUserIdFromSecurityContext())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateProfile(
                profileRequest.getName(),
                profileRequest.getAbout(),
                profileRequest.getRole(),
                profileRequest.getSubjects()
        );

        return UserProfileDto.from(userRepository.save(user));
    }

    public UserProfileDto getUserProfile() {
        User user = userRepository.findById(getUserIdFromSecurityContext())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return UserProfileDto.from(user);
    }

    // Получаем данные из SecurityContextHolder (заполнен в JwtTokenFilter)
    private Long getUserIdFromSecurityContext() {
        try {
            return (Long) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
        } catch (ClassCastException e) {
            throw new JwtAuthenticationException("Invalid token");
        }
    }
}
