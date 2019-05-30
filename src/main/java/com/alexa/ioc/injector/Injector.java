package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Injector {
    protected Map<BeanDefinition, Bean> beanDefinitionBeanMap;

    public Injector(Map<BeanDefinition, Bean> beanDefinitionBeanMap) {
        this.beanDefinitionBeanMap = beanDefinitionBeanMap;
    }

    public void inject () {
        for (BeanDefinition beanDefinition : beanDefinitionBeanMap.keySet()) {
            Bean targetBean = beanDefinitionBeanMap.get(beanDefinition);
            Map<String, String> dependencies = getDependencies(beanDefinition);

            for (Map.Entry<String, String> entry : dependencies.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();

                Object targetObject = targetBean.getValue();
                Method method = getSetterMethod(field, targetObject);
                invoke(targetObject, value, method);
            }
        }
    }

    private Method getSetterMethod(String field, Object targetObject) {
        String setterMethod = "set" + capitalize(field);

        Method[] methods = targetObject.getClass().getMethods();
        return getMethodByName(methods, setterMethod);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }


    private Method getMethodByName(Method[] methods, String name) {
        for(Method method : methods){
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new BeanInjectDependenciesException("Setter method not found by name" + name);
    }

    abstract Map<String, String> getDependencies(BeanDefinition beanDefinition);

    abstract void invoke (Object object, String value, Method method);

}
