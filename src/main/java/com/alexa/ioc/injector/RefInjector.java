package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RefInjector extends Injector {
    public RefInjector(Map<BeanDefinition, Bean> beanDefinitionBeanMap) {
        super(beanDefinitionBeanMap);
    }

    @Override
    Map<String, String> getDependencies(BeanDefinition beanDefinition) {
        return beanDefinition.getRefDependencies();
    }

    @Override
    void invoke(Object object, String value, Method method) {
        Class<?> parameterTypes = method.getParameterTypes()[0];
        Object refObject = getBean(value);
        try {
            method.invoke(object, parameterTypes.cast(refObject));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object getBean(String beanId) {
        for (Bean bean : beanDefinitionBeanMap.values()) {
           if(bean.getId().equals(beanId)){
               return bean.getValue();
           }
        }
        throw new RuntimeException();
    }
}
