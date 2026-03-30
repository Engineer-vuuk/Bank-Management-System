package com.bms.bank.management.system.exception;

public class RecipientNotFoundException extends RuntimeException {

    public RecipientNotFoundException(String message) {
        super(message);
    }
}
