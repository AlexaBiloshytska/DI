package com.alexa.ioc.entity;

import java.util.HashMap;
import java.util.Map;

public class BeanDefinition {
    private String id;
    private String beanClassName;
    private Map<String, String> dependencies = new HashMap<>(); // Eager preset to avoid null dependencies
    private Map<String, String> refDependencies = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, String> getRefDependencies() {
        return refDependencies;
    }

    public void setRefDependencies(Map<String, String> refDependencies) {
        this.refDependencies = refDependencies;
    }
}
