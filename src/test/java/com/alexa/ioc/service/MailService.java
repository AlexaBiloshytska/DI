package com.alexa.ioc.service;

public class MailService {
    private String protocol;
    private int port;


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
