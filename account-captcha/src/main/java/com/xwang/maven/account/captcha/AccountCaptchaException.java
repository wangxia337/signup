package com.xwang.maven.account.captcha;

public class AccountCaptchaException extends Exception {
    AccountCaptchaException(String message, Exception e){
        super(message, e);
    }

    public AccountCaptchaException(String message) {
        super(message);
    }
}
