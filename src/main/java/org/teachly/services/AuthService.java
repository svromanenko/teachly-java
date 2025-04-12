package org.teachly.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teachly.dto.AuthRequest;
import org.teachly.dto.AuthResponse;
import org.teachly.dto.UserProfileDto;
import org.teachly.models.User;
import org.teachly.repositories.UserRepository;
import org.teachly.security.JwtTokenProvider;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse authenticate(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().toString())
        );

        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername(), authorities);

        // TODO: добавить логику для получения classes и chats
        return AuthResponse.builder()
                .token(token)
                .profile(UserProfileDto.from(user))
                .build();
    }
}
