package com.xwang.maven.account.persist;

public class AccountPersistException extends Exception {
    AccountPersistException(String message, Exception e) {
        super(message, e);
    }
}
