package com.example.phase5;

public class Reghelper {
    String name,mail,phone,pass;

    public Reghelper(String name, String mail, String phone, String pass) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.pass = pass;
    }

    public Reghelper() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
