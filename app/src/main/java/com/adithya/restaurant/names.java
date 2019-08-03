package com.adithya.restaurant;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class names {

    public String name;

    public names(){

    }

    public names(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
