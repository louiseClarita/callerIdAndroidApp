package com.example.calleridapplication;

public class ContactModel {
    private String contact_id;
    private String contact_lname;
    private String contact_fname;
    private String contact_company;
    private String contact_job;
    private String contact_email;
    private String contact_mobilephone;

    public String getContact_mobilephone() {
        return contact_mobilephone;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "contact_id='" + contact_id + '\'' +
                ", contact_lname='" + contact_lname + '\'' +
                ", contact_fname='" + contact_fname + '\'' +
                ", contact_company='" + contact_company + '\'' +
                ", contact_job='" + contact_job + '\'' +
                ", contact_email='" + contact_email + '\'' +
                ", contact_mobilephone='" + contact_mobilephone + '\'' +
                '}';
    }

    public void setContact_mobilephone(String contact_mobilephone) {
        this.contact_mobilephone = contact_mobilephone;
    }

    public ContactModel(String contact_id, String contact_lname, String contact_fname, String contact_company, String contact_job, String contact_email, String contact_mobilephone) {
        this.contact_id = contact_id;
        this.contact_lname = contact_lname;
        this.contact_fname = contact_fname;
        this.contact_company = contact_company;
        this.contact_job = contact_job;
        this.contact_email = contact_email;
        this.contact_mobilephone = contact_mobilephone;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_fname() {
        return contact_fname;
    }

    public void setContact_fname(String contact_fname) {
        this.contact_fname = contact_fname;
    }

    public String getContact_company() {
        return contact_company;
    }

    public void setContact_company(String contact_company) {
        this.contact_company = contact_company;
    }

    public String getContact_job() {
        return contact_job;
    }

    public void setContact_job(String contact_job) {
        this.contact_job = contact_job;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_lname() {
        return contact_lname;
    }

    public void setContact_lname(String contact_lname) {
        this.contact_lname = contact_lname;
    }
}
