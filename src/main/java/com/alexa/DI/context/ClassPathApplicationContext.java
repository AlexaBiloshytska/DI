package com.alexa.DI.context;

import com.alexa.DI.entity.Bean;
import com.alexa.DI.entity.BeanDefinition;
import com.alexa.DI.exceptions.BeanNotFoundException;
import com.alexa.DI.reader.BeanDefinitionReader;

import java.util.ArrayList;
import java.util.List;

public class ClassPathApplicationContext implements ApplicationContext {
    private BeanDefinitionReader reader;
    private List<Bean> beans = new ArrayList<>();
    private List<BeanDefinition> beanDefinitions;

    public <T> T getBean(Class<T> clazz) {
        return null;
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
}
