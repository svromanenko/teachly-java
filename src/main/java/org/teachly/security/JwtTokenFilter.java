package org.teachly.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.teachly.exceptions.JwtAuthenticationException;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            extractToken(request)
                    .filter(tokenProvider::validateToken)
                    .ifPresent(token -> {
                        Authentication auth = tokenProvider.getAuthentication(token);
                        // Данные авторизации будем брать из SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            sendErrorResponse(response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT_ERROR",
                    e.getMessage());
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            sendErrorResponse(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "AUTH_ERROR",
                    "Authentication failed");
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            int status,
            String errorCode,
            String message) throws IOException {

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(
                response.getWriter(),
                Map.of(
                        "error", errorCode,
                        "message", message,
                        "timestamp", Instant.now()
                )
        );
    }

    private Optional<String> extractToken(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(AUTH_HEADER))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .filter(token -> !token.isBlank());
    }
}
