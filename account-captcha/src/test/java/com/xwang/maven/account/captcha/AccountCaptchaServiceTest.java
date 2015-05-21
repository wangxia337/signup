package com.xwang.maven.account.captcha;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccountCaptchaServiceTest {

    private AccountCaptchaService accountCaptchaService;

    @Before
    public void prepare(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("account-captcha.xml");
        accountCaptchaService = (AccountCaptchaService) applicationContext.getBean("accountCaptchaService");
    }

    @Test
    public void testGenerateCaptcha() throws Exception {
        String captchaKey = accountCaptchaService.generateCaptchaKey();
        assertNotNull(captchaKey);

        byte[] captchaImage = accountCaptchaService.generateCaptchaImage(captchaKey);
        assertTrue(captchaImage.length > 0);

        File image = new File("target/test-classes/" + captchaKey + ".jpg");
        OutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(image);
            outputStream.write(captchaImage);
        } finally {
            if(outputStream != null){
                outputStream.close();
            }
        }

        assertTrue(image.exists() && image.length() > 0);
    }

    @Test
    public void testValidateCaptcha() throws Exception {
        List<String> preDefinedTexts = newArrayList("12345", "abcde");
        accountCaptchaService.setPerDefinedTexts(preDefinedTexts);

        String captchaKey = accountCaptchaService.generateCaptchaKey();
        accountCaptchaService.generateCaptchaImage(captchaKey);
        assertFalse(accountCaptchaService.validateCaptcha(captchaKey, "67878"));

        captchaKey = accountCaptchaService.generateCaptchaKey();
        accountCaptchaService.generateCaptchaImage(captchaKey);
        assertTrue(accountCaptchaService.validateCaptcha(captchaKey, "abcde"));
    }
}