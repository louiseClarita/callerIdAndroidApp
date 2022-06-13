package com.example.calleridapplication;

public class CallLogs {
    private String duration;
    private String direction;
    private String date;
    private String phoneNbre;
    private String saved="0";
    private String callerid;
    private String calllogid;

    public CallLogs(String calllogid,String duration, String direction, String date, String phoneNbre, String saved, String callername) {
        this.duration = duration;
        this.direction = direction;
        this.date = date;
        this.phoneNbre = phoneNbre;
        this.saved = saved;
        this.callerid = callername;
        this.calllogid = calllogid;

    }

    public CallLogs(String duration, String direction, String date, String phoneNbre, String saved, String callername) {
        this.duration = duration;
        this.direction = direction;
        this.date = date;
        this.phoneNbre = phoneNbre;
        this.saved = saved;
        this.callerid = callername;
    }
    public String getCalllogid() {
        return calllogid;
    }

    public void setCalllogid(String calllogid) {
        this.calllogid = calllogid;
    }

    @Override
    public String toString() {
        return "CallLogs{" +
                "duration='" + duration + '\'' +
                ", direction='" + direction + '\'' +
                ", date='" + date + '\'' +
                ", phoneNbre='" + phoneNbre + '\'' +
                ", saved='" + saved + '\'' +
                ", callerid='" + callerid + '\'' +
                ", calllogid='" + calllogid + '\'' +
                '}';
    }

    public String getCallerid() {
        return callerid;
    }

    public void setCallerid(String callerid) {
        this.callerid = callerid;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSaved() {
        return saved;
    }



    public void setSaved(String saved) {
        this.saved = saved;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoneNbre() {
        return phoneNbre;
    }

    public void setPhoneNbre(String phoneNbre) {
        this.phoneNbre = phoneNbre;
    }





}
