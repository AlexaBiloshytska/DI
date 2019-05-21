package com.alexa.ioc.service;

public class UserService {
    private MailService service;

    public void sendEmailWithUsersCount(){
        int numberOfUsersInSystem = getUsersCount();
        service.sendEmail("aleksandranecheporuk@gmail.com", "there are " + numberOfUsersInSystem + " users in the system");


    }

    public MailService getService() {
        return service;
    }

    public void setService(MailService service) {
        this.service = service;
    }

    public int getUsersCount() {
        return (int)(Math.random()* 1000);
    }
}
