package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.service.MailService;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ValueInjectorTest {

    @Test
    public void inject() {
        List<BeanDefinition> beanDefinitions = getBeanDefinitions();
        List<Bean> beans = getBeans();

        ValueInjector valueInjector = new ValueInjector(beanDefinitions, beans);
        valueInjector.inject();

        Bean bean = beans.get(0);
        MailService value = (MailService) bean.getValue();
        Assert.assertEquals("POP3", value.getProtocol());
        Assert.assertEquals(80, value.getPort());
        System.out.println(value);

    }

    private List<BeanDefinition> getBeanDefinitions(){
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        Map< String, String> dependencies = new HashMap<>();
        dependencies.put("protocol", "POP3");
        dependencies.put("port", "80");

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName("com.alexa.ioc.service.MailService");
        beanDefinition.setDependencies(dependencies);
        beanDefinition.setId("mailService");

        beanDefinitions.add(beanDefinition);

        return beanDefinitions;
    }

    private List<Bean> getBeans(){
        List<Bean> beans = new ArrayList<>();

        Bean bean = new Bean();
        bean.setValue(new MailService());
        bean.setId("mailService");

        beans.add(bean);

        return beans;

     }
}