package com.xwang.maven.account.service;

import com.xwang.maven.account.captcha.AccountCaptchaException;
import com.xwang.maven.account.captcha.AccountCaptchaService;
import com.xwang.maven.account.captcha.RandomGenerator;
import com.xwang.maven.account.email.AccountEmailException;
import com.xwang.maven.account.email.AccountEmailService;
import com.xwang.maven.account.persist.Account;
import com.xwang.maven.account.persist.AccountPersistException;
import com.xwang.maven.account.persist.AccountPersistService;

import java.util.HashMap;
import java.util.Map;

public class AccountServiceImpl implements AccountService{
    private AccountEmailService accountEmailService;

    private AccountPersistService accountPersistService;

    private AccountCaptchaService accountCaptchaService;
    private Map<String, String> activationMap = new HashMap<String, String>();

    public String generateCaptchaKey() throws AccountServiceException {
        try{
            return accountCaptchaService.generateCaptchaKey();
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("Unable generate captcha key.", e);
        }
    }

    public byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException {
        try{
            return accountCaptchaService.generateCaptchaImage(captchaKey);
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("Unable generate captcha image", e);
        }
    }

    public void signUp(SignUpRequest signUpRequest) throws AccountServiceException {
        try{
            if(!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())){
                throw new AccountServiceException("2 password do not match");
            }

            if(!accountCaptchaService.validateCaptcha(signUpRequest.getCaptchaKey(), signUpRequest.getCaptchaValue())){
                throw new AccountServiceException("Incorrect captcha");
            }

            Account account = new Account();
            account.setId(signUpRequest.getId());
            account.setEmail(signUpRequest.getEmail());
            account.setName(signUpRequest.getName());
            account.setPassword(signUpRequest.getPassword());
            account.setActivated(false);

            accountPersistService.createAccount(account);

            String activationId = RandomGenerator.getRandomString();

            activationMap.put(activationId, account.getId());

            String link = signUpRequest.getActivateServiceUrl().endsWith("/") ?
                    String.format("%s%s", signUpRequest.getActivateServiceUrl(), activationId) :
                    String.format("%s?key=%s", signUpRequest.getActivateServiceUrl(), activationId);
            accountEmailService.sendEmail(account.getEmail(), "Please Activate Your Account", link);
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("Unable to validate captcha", e);
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to create account", e);
        } catch (AccountEmailException e) {
            throw new AccountServiceException("Unable to send activation email", e);
        }
    }

    public void activate(String activateNumber) throws AccountServiceException {
        String accountId = activationMap.get(activateNumber);

        if(accountId == null) {
            throw new AccountServiceException("Invalid account activation ID");
        }

        try{
            Account account = accountPersistService.readAccount(accountId);
            account.setActivated(true);
            accountPersistService.updateAccount(account);
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to activate account");
        }
    }

    public void login(String id, String password) throws AccountServiceException {
        try{
            Account account = accountPersistService.readAccount(id);

            if(account == null) {
                throw new AccountServiceException("Account does not exist!");
            }

            if(!account.isActivated()) {
                throw new AccountServiceException("Account is disabled");
            }

            if(!account.getPassword().equals(password)) {
                throw new AccountServiceException("Password is incorrect");
            }
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to login", e);
        }


    }

    public AccountEmailService getAccountEmailService() {
        return accountEmailService;
    }

    public void setAccountEmailService(AccountEmailService accountEmailService) {
        this.accountEmailService = accountEmailService;
    }

    public AccountPersistService getAccountPersistService() {
        return accountPersistService;
    }

    public void setAccountPersistService(AccountPersistService accountPersistService) {
        this.accountPersistService = accountPersistService;
    }

    public AccountCaptchaService getAccountCaptchaService() {
        return accountCaptchaService;
    }

    public void setAccountCaptchaService(AccountCaptchaService accountCaptchaService) {
        this.accountCaptchaService = accountCaptchaService;
    }
}
