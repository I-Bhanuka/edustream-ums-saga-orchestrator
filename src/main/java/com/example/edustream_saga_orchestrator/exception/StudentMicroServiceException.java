package com.example.edustream_saga_orchestrator.exception;

public class StudentMicroServiceException extends SagaApplicationException {
    public StudentMicroServiceException(String message, int statusCode) {
        super(message, statusCode);
    }

    public StudentMicroServiceException(String message, int statusCode, String downStreamMessage) {
        super(message, statusCode, downStreamMessage);
    }
}
