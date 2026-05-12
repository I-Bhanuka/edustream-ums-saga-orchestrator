package com.example.edustream_saga_orchestrator.exception;

public class NotFoundException extends SagaApplicationException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
