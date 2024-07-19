package com.BlogEcho.BlogEcho.exceptions;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
