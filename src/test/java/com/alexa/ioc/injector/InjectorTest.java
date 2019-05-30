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

public class InjectorTest {

    @Test
    public void inject() {
        Map<BeanDefinition, Bean> beanDefinitionBeanMap = getBeanDefinitionsMap();

        for (Bean bean : beanDefinitionBeanMap.values()) {
            if (bean.getId().equals("userService")) {
                UserService userService = ((UserService) bean.getValue());
                Assert.assertNull(userService.getService());

                RefInjector refInjector = new RefInjector(beanDefinitionBeanMap);
                refInjector.inject();

                Assert.assertNotNull(userService.getService());
            }
        }

        for (Bean bean : beanDefinitionBeanMap.values()) {
            if (bean.getId().equals("mailService")) {
                ValueInjector valueInjector = new ValueInjector(beanDefinitionBeanMap);
                valueInjector.inject();

                MailService value = (MailService) bean.getValue();
                Assert.assertEquals("POP3", value.getProtocol());
                Assert.assertEquals(80, value.getPort());
                System.out.println(value);
            }
        }
    }

    private Map<BeanDefinition, Bean> getBeanDefinitionsMap(){
        Map<BeanDefinition, Bean> beanDefinitionBeanMap = new HashMap<>();

        Map<String, String> dependencies = new HashMap<>();
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

        Bean mailServiceBean = new Bean();
        mailServiceBean.setId("mailService");
        mailServiceBean.setValue(new MailService());

        Bean userServiceBean = new Bean();
        userServiceBean.setId("userService");
        userServiceBean.setValue(new UserService());

        beanDefinitionBeanMap.put(userServiceBeanDefinition, userServiceBean);
        beanDefinitionBeanMap.put(mailServiceBeanDefinition, mailServiceBean);
        return beanDefinitionBeanMap;
    }
}