package com.example.edustream_saga_orchestrator.exception;

public class CourseMicroServiceException extends SagaApplicationException {
    public CourseMicroServiceException(String message, int statusCode) {
        super(message, statusCode);
    }

    public CourseMicroServiceException(String message, int statusCode, String downStreamMessage) {
        super(message, statusCode, downStreamMessage);
    }
}
