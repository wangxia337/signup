package com.xwang.maven.account.email;

import javax.mail.MessagingException;

public class AccountEmailException extends RuntimeException{
    public AccountEmailException(String message, MessagingException e){
        super(message, e);
    }
}
