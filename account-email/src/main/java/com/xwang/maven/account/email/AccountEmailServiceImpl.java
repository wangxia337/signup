package com.xwang.maven.account.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class AccountEmailServiceImpl implements AccountEmailService{

    private JavaMailSender javaMailSender;

    private String systemEmail;

    public void sendEmail(String to, String subject, String htmlText) throws AccountEmailException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom(systemEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(htmlText, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new AccountEmailException("Fail to send mail.", e);
        }
    }

    public JavaMailSender getJavaMailSender(){
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String getSystemEmail() {
        return systemEmail;
    }

    public void setSystemEmail(String systemEmail) {
        this.systemEmail = systemEmail;
    }
}
