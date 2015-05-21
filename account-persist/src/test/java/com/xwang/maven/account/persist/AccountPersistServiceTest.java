package com.xwang.maven.account.persist;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class AccountPersistServiceTest {

    private AccountPersistService accountPersistService;

    @Before
    public void prepare() throws AccountPersistException {
        File persistDataFile = new File("target/test-classes/persist-data.xml");

        if(persistDataFile.exists()){
            persistDataFile.delete();
        }

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("account-persist.xml");
        accountPersistService = (AccountPersistService) applicationContext.getBean("accountPersistService");

        Account account = new Account();
        account.setId("number1");
        account.setName("wang xia");
        account.setEmail("test@xwang.com");
        account.setPassword("123456");
        account.setActivated(true);

        accountPersistService.createAccount(account);
    }

    @Test
    public void testReadAccount() throws AccountPersistException {
        Account account = accountPersistService.readAccount("number1");

        assertNotNull(account);
        assertThat(account.getName(), is("wang xia"));
        assertThat(account.getEmail(), is("test@xwang.com"));
        assertThat(account.getPassword(), is("123456"));
        assertTrue(account.isActivated());
    }

    @Test
    public void testUpdateAccount() throws AccountPersistException {
        Account account = new Account();
        account.setId("number1");
        account.setName("michelle wang");
        account.setEmail("michelle@xwang.com");
        account.setPassword("654321");
        account.setActivated(false);

        accountPersistService.updateAccount(account);

        Account updatedAccount = accountPersistService.readAccount("number1");

        assertNotNull(updatedAccount);
        assertThat(updatedAccount.getName(), is("michelle wang"));
        assertThat(updatedAccount.getEmail(), is("michelle@xwang.com"));
        assertThat(updatedAccount.getPassword(), is("654321"));
        assertFalse(updatedAccount.isActivated());
    }

    @Test
    public void testDeleteAccount() throws AccountPersistException {
        accountPersistService.deleteAccount("number1");

        Account account = accountPersistService.readAccount("number1");
        assertNull(account);
    }
}