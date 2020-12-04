package com.example.yaatris;

public class User {
    public String Displayname;
    public String Phone;
    public String Email;
    public long createdAt;

    public User(String displayName, String phone, String userEmail, long time) {
        this.Displayname=displayName;
        this.Email=userEmail;
        this.Phone = phone;
        this.createdAt=time;
    }
}
