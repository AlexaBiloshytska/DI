package com.alexa.ioc.reader;

import com.alexa.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions(String[] path);

    List<BeanDefinition> readBeanDefinitions();
}
