package com.BlogEcho.BlogEcho.exceptions;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
