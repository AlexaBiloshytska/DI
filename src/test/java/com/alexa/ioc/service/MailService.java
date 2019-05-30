package com.alexa.ioc.service;

import com.alexa.ioc.entity.BeanDefinition;
import com.alexa.ioc.processor.BeanFactoryPostProcessor;

import javax.annotation.PostConstruct;
import java.util.List;

public class MailService implements BeanFactoryPostProcessor {
    private String protocol;
    private int port;

    @Override
    public void postProcessBeanFactory(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            System.out.println(beanDefinition.getId());
        }
    }

    @PostConstruct
    private void init(){
        sendEmail("aleksandranecheporuk@gmail.com","POP3");
    }

    public void sendEmail(String mailTo, String content){
        System.out.println("Sending email To: " + mailTo);
        System.out.println("With content :" + content);
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "MailService{" +
                "protocol='" + protocol + '\'' +
                ", port=" + port +
                '}';
    }


}
