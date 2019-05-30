package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RefInjector {
    private List<BeanDefinition> beanDefinitions;
    private List<Bean> beans;

    public RefInjector(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        this.beanDefinitions = beanDefinitions;
        this.beans = beans;
    }


    public void inject () {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Map<String, String> refDependenciesMap = beanDefinition.getRefDependencies();
            if (refDependenciesMap != null) {
                for (Bean targetBean : beans) {
                    if (targetBean.getId().equals(beanDefinition.getId())) {
                        for (Map.Entry<String, String> entry : refDependenciesMap.entrySet()) {
                            String fieldName = entry.getKey();
                            String refBeanId = entry.getValue();

                            String setterMethod = "set" + capitalize(fieldName);

                            Object targetObject = targetBean.getValue();
                            Method[] methods = targetObject.getClass().getMethods();

                            Method method = getMethodByName(methods, setterMethod);

                            Class<?> parameterTypes = method.getParameterTypes()[0];

                            Object refObject = getBean(refBeanId);
                            try {
                                method.invoke(targetObject, parameterTypes.cast(refObject));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private Object getBean(String beanId) {
        for (Bean bean : beans) {
           if(bean.getId().equals(beanId)){
               return bean.getValue();
           }
        }
        throw new RuntimeException();
    }

    private Method getMethodByName(Method[] methods, String name) {
        for(Method method : methods){
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new BeanInjectDependenciesException("Setter method not found by name" + name);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
