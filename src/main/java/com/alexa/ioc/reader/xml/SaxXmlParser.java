package com.alexa.ioc.reader.xml;

import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.exceptions.BeanParseException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaxXmlParser extends DefaultHandler {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private int classNameCount;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)  {
        if ("bean".equals(qName)){
            processBean(attributes);
        } else if("property".equals(qName)){
            processProperty(attributes);
        }
    }

    private void processProperty(Attributes attributes) {
        String name = attributes.getValue("name");
        String value = attributes.getValue("value");
        String ref = attributes.getValue("ref");

        if (name != null && value != null && ref == null) {
            Map<String, String> valueDependenciesMap =
                    beanDefinitions.get(beanDefinitions.size() - 1).getDependencies();
            valueDependenciesMap.put(name, value);
        } else if (name != null && ref != null && value == null) {
            Map<String, String> refDependenciesMap =
                    beanDefinitions.get(beanDefinitions.size() - 1).getRefDependencies();
            refDependenciesMap.put(name, ref);
        } else {
            throw new BeanParseException("Property attributes are not valid");
        }
    }

    public void processBean(Attributes attributes) {
        BeanDefinition beanDefinition = new BeanDefinition();

        String id = attributes.getValue("id");
        String clazz = attributes.getValue("class");

        if (clazz != null){
            beanDefinition.setBeanClassName(clazz);
        } else {
            throw new BeanParseException("There is no class attribute with id of bean: " + id );
        }

        if (id != null){
            beanDefinition.setId(id);
        } else {
            String createDefaultId = beanDefinition.getBeanClassName() + "$" + (classNameCount++);
            beanDefinition.setId(createDefaultId);
        }
        beanDefinitions.add(beanDefinition);
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

}
