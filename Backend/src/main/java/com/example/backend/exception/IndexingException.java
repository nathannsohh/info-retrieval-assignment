package com.example.backend.exception;

public class IndexingException extends Exception {
    public IndexingException(String message, Throwable error) {
        super(message, error);
    }
}
