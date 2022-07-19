package com.example.calleridapplication;

public class EmailModel {


    private String subject;
    private String time;
    private boolean Read;
    private String bodyPreview;
    public EmailModel(String subject, String time) {
        this.subject = subject;
        this.time = time;

    }

    public EmailModel(String subject, String time, Boolean read, String bodyPreview) {
        this.subject = subject;
        this.time = time;
        Read = read;
        this.bodyPreview = bodyPreview;
    }

    @Override
    public String toString() {
        return "EmailModel{" +
                "subject='" + subject + '\'' +
                ", time='" + time + '\'' +
                ", Read='" + Read + '\'' +
                ", bodyPreview='" + bodyPreview + '\'' +
                '}';
    }

    public boolean isRead() {
        return Read;
    }

    public void setRead(boolean read) {
        Read = read;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
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
