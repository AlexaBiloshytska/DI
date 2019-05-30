package com.alexa.ioc.context;

import com.alexa.ioc.entity.Bean;
import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanNotFoundException;
import com.alexa.ioc.injector.RefInjector;
import com.alexa.ioc.injector.ValueInjector;
import com.alexa.ioc.processor.BeanFactoryPostProcessor;
import com.alexa.ioc.processor.BeanPostProcessor;
import com.alexa.ioc.reader.BeanDefinitionReader;
import com.alexa.ioc.reader.xml.SaxXmlBeanDefinitionsReader;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassPathApplicationContext implements ApplicationContext {
    private List<String> beansNames = new ArrayList<>();
    private List<BeanPostProcessor> beanPostProcessorsInstances = new ArrayList<>();
    private List<Bean> beans = new ArrayList<>();
    private List<BeanDefinition> beanDefinitions;

    public ClassPathApplicationContext() {
        BeanDefinitionReader reader = new SaxXmlBeanDefinitionsReader();
        List<BeanDefinition> beanDefinitions = reader.readBeanDefinitions();

        createBeansFromBeansDefinitions(beanDefinitions);

        beanFactoryPostProcessor(beanDefinitions);

        new ValueInjector(beanDefinitions, beans).inject();
        new RefInjector(beanDefinitions, beans).inject();

        postProcessBeforeInitialization();
        init();
        postProcessAfterInitialization();

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
       if (!beansNames.isEmpty()){
           return beansNames;

       }
        for (Bean bean : beans) {
            beansNames.add(bean.getId());
        }
        return beansNames;
    }

    public void init(){
        for (Bean bean : beans ) {
            Method[] methods = bean.getValue().getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(PostConstruct.class)){
                    try {
                        method.invoke(bean.getValue(), (Object[]) null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw  new RuntimeException("Can't invoke method: " + method +"in class " +bean.getValue().getClass());
                    }
                }

            }

        }
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

    private void beanFactoryPostProcessor(List<BeanDefinition> beanDefinitions){
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<?> beanDefinitionClass = Class.forName(beanClassName);
                if (BeanFactoryPostProcessor.class.isAssignableFrom(beanDefinitionClass)) { // Figure out
                    BeanFactoryPostProcessor createBeanDefClass = (BeanFactoryPostProcessor) beanDefinitionClass.newInstance();
                    createBeanDefClass.postProcessBeanFactory(beanDefinitions);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Can't create object: " + beanClassName, e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class: " + beanClassName
                        + " with bean id= " + beanDefinition.getId() + ", not found");
            }
        }
    }

    private void postProcessBeforeInitialization() {
        for (BeanPostProcessor beanPostProcessorsInstance : beanPostProcessorsInstances) {
            for (Bean bean : beans) {
                Object newBeanValue = beanPostProcessorsInstance.postProcessBeforeInitialization(bean.getValue(), bean.getId());
                bean.setValue(newBeanValue);

            }
        }
    }

    private void postProcessAfterInitialization() {
        for (BeanPostProcessor beanPostProcessorsInstance : beanPostProcessorsInstances) {
            for (Bean bean : beans) {
                Object newBeanValue = beanPostProcessorsInstance.postProcessAfterInitialization(bean.getValue(), bean.getId());
                bean.setValue(newBeanValue);


            }
        }
    }
}
