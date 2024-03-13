package com.example.backend.exception;

public class CsvParsingException extends Exception {
    public CsvParsingException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
