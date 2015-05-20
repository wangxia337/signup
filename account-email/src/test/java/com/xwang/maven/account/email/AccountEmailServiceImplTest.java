package com.xwang.maven.account.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.Message;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AccountEmailServiceImplTest {
    private GreenMail greenMail;

    @Before
    public void startMailServer() throws Exception {
        greenMail = new GreenMail(new ServerSetup(3025, "localhost", "smtp"));
        greenMail.setUser("test@xwang.com", "123456");
        greenMail.start();
    }

    @Test
    public void testSendEmail() throws Exception {
        String subject = "Test Subject";
        String htmlText = "<h3> test </h3>";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("account-email.xml");
        AccountEmailServiceImpl accountEmailService = (AccountEmailServiceImpl) applicationContext.getBean("accountEmailService");
        accountEmailService.sendEmail("test2@xwang.com", subject, htmlText);

        greenMail.waitForIncomingEmail(2000, 1);
        Message[] messages = greenMail.getReceivedMessages();
        assertThat(messages.length, is(1));
        assertThat(messages[0].getSubject(), is(subject));
        assertThat(GreenMailUtil.getBody(messages[0]).trim(), is(htmlText));
    }

    @After
    public void stopMailServer() throws Exception{
        greenMail.stop();
    }
}