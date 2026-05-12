package com.example.edustream_bff.exception;

public class BadRequestException extends BFFApplicationException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}
