package com.example.yaatris;

public class Volunteer {
    public String DisplayName, Email, Phone, Activities, Locations;

    long createdAt;

    public Volunteer(String displayName, String phone, String userEmail, String vactivities, String vlocations) {
        this.DisplayName=displayName;
        this.Email=userEmail;
        this.Phone = phone;
        this.Activities = vactivities;
        this.Locations = vlocations;
    }
}
