package com.example.edustream_bff.exception;

public class NotFoundException extends BFFApplicationException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
