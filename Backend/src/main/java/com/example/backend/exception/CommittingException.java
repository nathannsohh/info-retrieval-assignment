package com.example.backend.exception;

public class CommittingException extends Exception {
    public CommittingException(String message, Throwable error) {
        super(message, error);
    }
}
