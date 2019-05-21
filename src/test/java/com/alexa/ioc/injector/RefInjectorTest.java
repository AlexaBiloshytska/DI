package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.service.MailService;
import com.alexa.ioc.service.UserService;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RefInjectorTest {

    @Test
    public void inject() {
        List<BeanDefinition> beanDefinitions = getBeanDefinitions();
        List<Bean> beans = getBeans();

        Bean userBean = beans.get(1);
        UserService userService = ((UserService) userBean.getValue());

        Assert.assertNull(userService.getService());

        RefInjector refInjector = new RefInjector(beanDefinitions, beans);
        refInjector.inject();

        Assert.assertNotNull(userService.getService());

    }

    private List<BeanDefinition> getBeanDefinitions(){
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        Map< String, String> dependencies = new HashMap<>();
        dependencies.put("protocol", "POP3");
        dependencies.put("port", "80");

        BeanDefinition mailServiceBeanDefinition = new BeanDefinition();
        mailServiceBeanDefinition.setBeanClassName("com.alexa.ioc.service.MailService");
        mailServiceBeanDefinition.setDependencies(dependencies);
        mailServiceBeanDefinition.setId("mailService");

        Map<String, String> refDependencies = new HashMap<>();
        refDependencies.put("service", "mailService");

        BeanDefinition userServiceBeanDefinition = new BeanDefinition();
        userServiceBeanDefinition.setBeanClassName("com.alexa.ioc.service.UserService");
        userServiceBeanDefinition.setRefDependencies(refDependencies);
        userServiceBeanDefinition.setId("userService");

        beanDefinitions.add(mailServiceBeanDefinition);
        beanDefinitions.add(userServiceBeanDefinition);

        return beanDefinitions;
    }

    private List<Bean> getBeans(){
        List<Bean> beans = new ArrayList<>();

        Bean mailServiceBean = new Bean();
        mailServiceBean.setId("mailService");
        mailServiceBean.setValue(new MailService());

        Bean userServiceBean = new Bean();
        userServiceBean.setId("userService");
        userServiceBean.setValue(new UserService());

        beans.add(mailServiceBean);
        beans.add(userServiceBean);



        return beans;

    }
}