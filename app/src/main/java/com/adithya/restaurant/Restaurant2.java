package com.adithya.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Restaurant2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant2);
    }

    public  void userprofiles(View view){
        Intent intent = new Intent(this,UserProfiles.class);
        startActivity(intent);

    }
}