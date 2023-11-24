package com.tryvault.velocityservice.exception;

public class IncorrectTransactionException extends RuntimeException {
    public IncorrectTransactionException(String message) {
        super(message);
    }
}
