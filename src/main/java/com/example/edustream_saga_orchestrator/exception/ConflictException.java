package com.example.edustream_saga_orchestrator.exception;

public class ConflictException extends SagaApplicationException {
    public ConflictException(String message) {
        super(message, 409);
    }
}
