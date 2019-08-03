package com.adithya.restaurant;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String firstname;
    public String lastname;
    public String dob;
    public String gender;
    public String maritalstatus;
    public String restaurantchosen;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstname, String lastname, String dob, String gender, String maritalstatus, String restaurant){

        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.gender = gender;
        this.maritalstatus = maritalstatus;
        this.restaurantchosen= restaurant;


    }

}
