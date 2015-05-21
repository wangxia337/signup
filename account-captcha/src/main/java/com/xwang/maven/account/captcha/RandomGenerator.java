package com.xwang.maven.account.captcha;

import java.util.Random;

public class RandomGenerator {
    private static String range = "0123456789abcdefghigklmnopqrstuvwxyz";

    public static synchronized String getRandomString() {
        Random random = new Random();
        StringBuffer results = new StringBuffer();
        for (int index = 0; index < 8; index++) {
            results.append(range.charAt(random.nextInt(range.length())));
        }

        return results.toString();
    }
}
