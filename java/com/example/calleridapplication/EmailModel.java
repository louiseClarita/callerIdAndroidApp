package com.example.calleridapplication;

public class EmailModel {


    private String subject;
    private String time;

    public EmailModel(String subject, String time) {
        this.subject = subject;
        this.time = time;

    }

    @Override
    public String toString() {
        return "EmailModel{" +
                "subject='" + subject + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
