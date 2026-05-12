package com.example.edustream_saga_orchestrator.exception;

import com.example.edustream_saga_orchestrator.dto.responseDTO.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Custom exception handler - Every custom exception will be handled by ApplicationException
    @ExceptionHandler(SagaApplicationException.class)
    public ResponseEntity<ErrorResponseDTO> handleApplicationException(
            SagaApplicationException e) {

        ErrorResponseDTO errorResponseDTO = (e.getDownstreamMessage() != null)
                ? ErrorResponseDTO.of(e.getStatusCode(), e.getMessage(), e.getDownstreamMessage())
                : ErrorResponseDTO.of(e.getStatusCode(), e.getMessage());

        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponseDTO);
    }

    // 2. Validation fails of @Valid annotated request bodies will be handled by MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException e) {

        Map<String, String> fieldErrors = new HashMap<>();

        // e.getBindingResult().getFieldErrors() will return a list of FieldError objects
        // Then the forEach loop will iterate over each FieldError object and put the field name and error message into the fieldErrors map
        e.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        log.error("Validation Exception: {}", fieldErrors);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(400)
                .message("Validation failed")
                .details(fieldErrors)
                .build();

        return ResponseEntity
                .status(400)
                .body(error);
    }

    // 3. Handle malformed JSON request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(
            HttpMessageNotReadableException e) {

        log.error("Malformed JSON request: {}", e.getMessage());

        return ResponseEntity
                .status(400)
                .body(ErrorResponseDTO.of(400, "Malformed JSON request"));
    }


    // 4. SAFETY NET
    //  Catches anything we didn't anticipate — always last
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception e) {

        log.error("Unexpected error: {}", e.getMessage(), e);

        return ResponseEntity
                .status(500)
                .body(ErrorResponseDTO.of(500, "An unexpected error occurred"));
    }


}
