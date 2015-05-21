package com.xwang.maven.account.captcha;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class RandomGeneratorTest {

    @Test
    public void testGetRandomString() throws Exception {
        Set<String> randoms = new HashSet<String>(100);

        for (int i = 0; i < 100; i++) {
            String random = RandomGenerator.getRandomString();
            assertThat(RandomGenerator.getRandomString().length(), is(8));
            assertFalse(randoms.contains(random));
            randoms.add(random);
        }
    }
}