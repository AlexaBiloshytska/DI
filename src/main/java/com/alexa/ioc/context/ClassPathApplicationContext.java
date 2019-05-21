package com.alexa.ioc.context;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanNotFoundException;
import com.alexa.ioc.injector.RefInjector;
import com.alexa.ioc.injector.ValueInjector;
import com.alexa.ioc.reader.BeanDefinitionReader;
import com.alexa.ioc.reader.xml.SaxXmlBeanDefinitionsReader;

import java.util.ArrayList;
import java.util.List;

public class ClassPathApplicationContext implements ApplicationContext {

    private List<Bean> beans = new ArrayList<>();

    public ClassPathApplicationContext() {
        BeanDefinitionReader reader = new SaxXmlBeanDefinitionsReader();
        List<BeanDefinition> beanDefinitions = reader.readBeanDefinitions();
        createBeansFromBeansDefinitions(beanDefinitions);

        new ValueInjector(beanDefinitions, beans).inject();
        new RefInjector(beanDefinitions, beans).inject();



    }

    public <T> T getBean(Class<T> clazz) {
        T resultBeanValue = null;
        boolean isFound = false;
        for (Bean bean : beans) {
            if (bean.getValue().getClass().equals(clazz)) {
                if (!isFound) {
                    resultBeanValue = clazz.cast(bean.getValue());
                    isFound = true;
                } else {
                    throw new RuntimeException("Beans with class: " + clazz + ", more than one!");
                }
            }
        }
        if (isFound) {
            return resultBeanValue;
        } else {
            throw new BeanNotFoundException("Bean with class= " + clazz + ", not found!");
        }
    }

    public <T> T getBean(String id, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getValue().getClass().equals(clazz) && bean.getId().equals(id)) {
                return clazz.cast(bean.getValue());
            }
        }
        throw new BeanNotFoundException("Bean with class= " + clazz + " and id= " + id + ", are not found!");
    }


    public Object getBean(String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return bean.getValue();
            }
        }
        return new BeanNotFoundException("Bean with id= " + id + ", is not found!");
    }

    public List<String> getBeanNames() {
        List<String> beansNames = new ArrayList<>();
        for (Bean bean : beans) {
            beansNames.add(bean.getId());
        }
        return beansNames;
    }


    private void createBeansFromBeansDefinitions(List<BeanDefinition> beanDefinitions){
        for (BeanDefinition beanDefinition: beanDefinitions){
            String beanId = beanDefinition.getId();

            Bean bean = new Bean();
            bean.setId(beanId);

            String beanClassName = beanDefinition.getBeanClassName();

            try {
                Object objectFromBeanClassName = Class.forName(beanClassName).newInstance();
                bean.setValue(objectFromBeanClassName);

            } catch (InstantiationException| IllegalAccessException |ClassNotFoundException e) {
                throw new RuntimeException("Cannot create bean class  " + beanClassName, e );
            }

            beans.add(bean);
        }

    }

}
//TO DO :
// create value, inject, refinject injector
//add test
