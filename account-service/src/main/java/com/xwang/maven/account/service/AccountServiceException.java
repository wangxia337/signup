package com.xwang.maven.account.service;

public class AccountServiceException extends Exception {
    public AccountServiceException(String message, Exception e) {
        super(message, e);
    }

    public AccountServiceException(String message) {
        super(message);
    }
}
