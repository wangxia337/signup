package com.xwang.maven.account.email;

public interface AccountEmailService {
    void sendEmail(String to, String subject, String htmlText) throws AccountEmailException;
}
