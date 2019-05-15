package com.alexa.ioc.reader.xml;

import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.reader.BeanDefinitionReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SaxXmlBeanDefinitionsReader implements BeanDefinitionReader {
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    private String[] paths;

    public SaxXmlBeanDefinitionsReader() {
        paths = new String[]{"/context.xml"};
    }

    public SaxXmlBeanDefinitionsReader(String... paths) {
        this.paths = paths;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions(String[] paths) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String path:paths) {
            try {
                SAXParser saxParser = factory.newSAXParser();
                SaxXmlParser saxXmlParser = new SaxXmlParser();

                try (InputStream inputStream = SaxXmlBeanDefinitionsReader.class.getResourceAsStream(path);
                     BufferedInputStream xmlBufferedInputStream = new BufferedInputStream(inputStream)) {

                    saxParser.parse(xmlBufferedInputStream, saxXmlParser);
                    beanDefinitions.addAll(saxXmlParser.getBeanDefinitions());
                } catch (IOException e) {
                    throw new RuntimeException("Can't read XML file: " + path, e);
                }
            } catch (ParserConfigurationException | SAXException e) {
                throw new RuntimeException("Can't parse XML file: " + path, e);
            }

        }
        return beanDefinitions;
    }



    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return readBeanDefinitions(this.paths);
    }
}
