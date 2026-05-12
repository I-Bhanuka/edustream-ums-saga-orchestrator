package com.example.edustream_bff.exception;

public class StudentMicroServiceException extends BFFApplicationException {
    public StudentMicroServiceException(String message, int statusCode) {
        super(message, statusCode);
    }

    public StudentMicroServiceException(String message, int statusCode, String downStreamMessage) {
        super(message, statusCode, downStreamMessage);
    }
}
