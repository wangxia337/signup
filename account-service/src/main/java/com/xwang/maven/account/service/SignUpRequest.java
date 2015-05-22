package com.xwang.maven.account.service;

public class SignUpRequest {
    private String password;
    private String confirmPassword;
    private String captchaKey;
    private String captchaValue;
    private String id;
    private String email;
    private String name;

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public String getCaptchaValue() {
        return captchaValue;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getActivateServiceUrl() {
        return null;
    }
}
