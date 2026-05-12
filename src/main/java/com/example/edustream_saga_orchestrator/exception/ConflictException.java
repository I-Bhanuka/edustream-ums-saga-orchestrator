package com.example.edustream_bff.exception;

public class ConflictException extends BFFApplicationException {
    public ConflictException(String message) {
        super(message, 409);
    }
}
