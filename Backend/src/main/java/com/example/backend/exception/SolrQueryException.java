package com.example.backend.exception;

public class SolrQueryException extends RuntimeException {
    public SolrQueryException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
