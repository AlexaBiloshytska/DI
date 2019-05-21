package com.alexa.ioc.context;

import com.alexa.ioc.service.MailService;
import com.alexa.ioc.service.UserService;
import org.junit.Assert;
import org.junit.Test;

public class ClassPathApplicationContextTest {
    @Test
    public void getBeanByClass() {

        ClassPathApplicationContext applicationContext = new ClassPathApplicationContext();
        MailService mailService = applicationContext.getBean(MailService.class);
        UserService userService = applicationContext.getBean(UserService.class);

        Assert.assertNotNull(mailService);
        Assert.assertNotNull(userService);

    }
}