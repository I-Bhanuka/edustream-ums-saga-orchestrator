package com.example.edustream_saga_orchestrator.exception;

import lombok.Getter;

@Getter
public class BFFApplicationException extends RuntimeException {

    private final int statusCode;
    private final String downstreamMessage;

    public BFFApplicationException(String message, int statusCode, String downstreamMessage) {
        super(message);
        this.statusCode = statusCode;
        this.downstreamMessage = downstreamMessage;
    }

    public BFFApplicationException(String message, int statusCode) {
        this(message, statusCode, null); // Constructor chaining to set downstreamMessage as null
        // Otherwise there will be a compilation error because every private field needs to be initialized before the constructor ends
    }
}
