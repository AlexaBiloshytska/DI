package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RefInjector extends Injector {
    private List<Bean> beans;

    public RefInjector(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        super(beanDefinitions, beans);
    }

    @Override
    public void injectDependencies(Object beanValue, Method setterMethod, String beanRefId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Object refBeanValue = getBean(beans, beanRefId);
    setterMethod.invoke(beanValue, refBeanValue);

    }

    @Override
    public Map<String, String> getDependenciesMap(BeanDefinition beanDefinition) {
        return beanDefinition.getRefDependencies();
    }


    private Object getBean(List<Bean> beans, String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return bean.getValue();
            }
        }
        throw new BeanInjectDependenciesException("Bean with id= " + id + " not found!");
    }
}
