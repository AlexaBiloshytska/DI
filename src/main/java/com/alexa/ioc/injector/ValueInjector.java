package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ValueInjector extends Injector {

    public ValueInjector(Map<BeanDefinition, Bean> beanDefinitionBeanMap) {
        super(beanDefinitionBeanMap);
    }

    @Override
    public Map<String, String> getDependencies(BeanDefinition beanDefinition) {
        return beanDefinition.getDependencies();
    }


    void invoke(Object object, String value, Method method) {
        Class<?> fieldType = method.getParameterTypes()[0];
        try {
            if (boolean.class.equals(fieldType)) {
                method.invoke(object, Boolean.parseBoolean(value));
            } else if (int.class.equals(fieldType)) {
                method.invoke(object, Integer.parseInt(value));
            } else if (double.class.equals(fieldType)) {
                method.invoke(object, Double.parseDouble(value));
            } else if (long.class.equals(fieldType)) {
                method.invoke(object, Long.parseLong(value));
            } else if (short.class.equals(fieldType)) {
                method.invoke(object, Short.parseShort(value));
            } else if (byte.class.equals(fieldType)) {
                method.invoke(object, Byte.parseByte(value));
            } else if (float.class.equals(fieldType)) {
                method.invoke(object, Float.parseFloat(value));
            } else if (char.class.equals(fieldType)) {
                method.invoke(object, value.charAt(0));
            } else {
                method.invoke(object, value);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanInjectDependenciesException("Unable to inject value: " + value
                    + " using method: " + method
                    + " for bean with class: " + object.getClass());
        }
    }



    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
