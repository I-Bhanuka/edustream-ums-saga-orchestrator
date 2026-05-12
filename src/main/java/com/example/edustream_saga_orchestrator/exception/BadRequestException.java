package com.example.edustream_saga_orchestrator.exception;

public class BadRequestException extends SagaApplicationException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}
