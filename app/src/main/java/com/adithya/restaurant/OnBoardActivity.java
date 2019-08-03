package com.adithya.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;



public class OnBoardActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("first_time", false))
        {
                /*
                // we will set this true when our ScreenTabs activity
                   ends or the service playing music is stopped.
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("first_time", true);
                editor.commit();

                */

            Intent i = new Intent(this, UserDetails.class);
            this.startActivity(i);
            this.finish();
        }
        setCancelText("Skip");

        addFragment(new Step.Builder().setTitle("EXPLORE")
                .setContent("Choose Your Favourite Restaurants")
                .setBackgroundColor(Color.parseColor("#03A9F4")) // int background color
                .setDrawable(R.drawable.explore) // int top drawable
                //.setSummary("This is summary")
                .build());

        addFragment(new Step.Builder().setTitle("CONNECT")
                .setContent("Connecting People! " +
                        " Build Friends and Develop Networks")
                .setBackgroundColor(Color.parseColor("#03A9F4")) // int background color
                .setDrawable(R.drawable.connect) // int top drawable
                //.setSummary("This is summary")
                .build());

        addFragment(new Step.Builder().setTitle("GET OFFERS")
                .setContent("Special Discounts and Coupons Exclusively For You!!")
                .setBackgroundColor(Color.parseColor("#03A9F4")) // int background color
                .setDrawable(R.drawable.offers) // int top drawable
                //.setSummary("This is summary")
                .build());
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
    @Override
    public void finishTutorial() {
        // Your implementation
        Intent intent= new Intent(this,UserDetails.class);
        startActivity(intent);
        finish();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("first_time", true);
        editor.commit();

    }
}
