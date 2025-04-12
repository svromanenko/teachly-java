package org.teachly.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {
    private String error;
    private String message;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
