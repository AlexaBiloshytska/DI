package com.alexa.ioc.injector;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ValueInjector {
    private List<BeanDefinition> beanDefinitions;
    private List<Bean> beans;

    public ValueInjector(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        this.beanDefinitions = beanDefinitions;
        this.beans = beans;
    }

    public void inject(){
        for (BeanDefinition beanDefinition : beanDefinitions){
            Map<String, String> refDependenciesMap = beanDefinition.getRefDependencies();
            if (refDependenciesMap != null) {
                for (Bean bean : beans) {
                    if (beanDefinition.getId().equals(bean.getId())) {
                        Object object = bean.getValue();
                        Map<String, String> valueMap = beanDefinition.getDependencies();
                        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                            String field = entry.getKey();
                            String value = entry.getValue();

                            String setterMethod = "set" + capitalize(field);

                            Method[] methods = object.getClass().getMethods();
                            Method method = getMethodByName(methods, setterMethod);

                            invoke(object, value, method);
                        }
                    }
                }
            }
        }

    }

    private void invoke(Object object, String value, Method method) {
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

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
