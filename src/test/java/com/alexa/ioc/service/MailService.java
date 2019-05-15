package com.alexa.ioc.service;

public class MailService {
    private String protocol;
    private int port;


    public void sendEmail(String mailTo, String content){
        System.out.println("Sending email To: " + mailTo);
        System.out.println("With content :" + content);
    }

}
