package com.example.edustream_saga_orchestrator.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Omit null fields from JSON response
public class ErrorResponseDTO {

    private int status;
    private String message;
    private String downstreamMessage; //Only for exceptions caused by downstream microservices, will be null for validation errors and other application exceptions
    private LocalDateTime timestamp;
    private Map<String, String> details; //Only for validation errors, key is field name and value is error message

    // Static factory method for creating error response without details field
    public static ErrorResponseDTO of(int status, String message) {
        return ErrorResponseDTO.builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Static factory method for creating error response without details field but with downstreamMessage field
    public static ErrorResponseDTO of(int status, String message, String downstreamMessage) {
        return ErrorResponseDTO.builder()
                .status(status)
                .message(message)
                .downstreamMessage(downstreamMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
