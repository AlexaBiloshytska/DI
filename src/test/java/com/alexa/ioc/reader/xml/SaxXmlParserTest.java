package com.alexa.ioc.reader.xml;


import com.alexa.ioc.entity.BeanDefinition;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SaxXmlParserTest {

    @Test
    public void getBeanDefinitions() {
        // Prepare
        SaxXmlBeanDefinitionsReader definitionsReader = new SaxXmlBeanDefinitionsReader();

        // Execute
        List<BeanDefinition> beanDefinitions = definitionsReader.readBeanDefinitions();

        // Verify
        Assert.assertEquals(2, beanDefinitions.size());

        BeanDefinition mailServiceBeanDefinition = beanDefinitions.get(0);
        Assert.assertEquals("mailService", mailServiceBeanDefinition.getId());
        Assert.assertEquals("com.alexa.ioc.service.MailService", mailServiceBeanDefinition.getBeanClassName());
        Map<String, String> mailDependencies = mailServiceBeanDefinition.getDependencies();
        Assert.assertEquals("POP3", mailDependencies.get("protocol"));

        BeanDefinition userServiceBeanDefinition = beanDefinitions.get(1);
        Assert.assertEquals("userService", userServiceBeanDefinition.getId());
        Assert.assertEquals("com.alexa.ioc.service.UserService", userServiceBeanDefinition.getBeanClassName());
        Map<String, String> userDependencies = userServiceBeanDefinition.getRefDependencies();
        Assert.assertEquals("mailService", userDependencies.get("service"));
    }



}
