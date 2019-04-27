package com.example.lxysss.ocrbi.Entity;

/**
 * Created by Lxysss on 2019/4/10.
 */

public class UserIof {
    Integer id;
    String user_vip;
    String username;
    String password;
    String phone;
    String mail;
    String age;
    String sex;

    public UserIof(String username, String password, String phone, String mail) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.mail = mail;
    }

    public UserIof(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_vip() {
        return user_vip;
    }

    public void setUser_vip(String user_vip) {
        this.user_vip = user_vip;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
