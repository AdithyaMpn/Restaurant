package com.adithya.restaurant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantsMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "RestaurantMenu";
    static List<String> mobno = new ArrayList<>();
    static String formattedPhoneNumber;
    String fullname;
    static String no;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("RESTAURANT/Users/");
    DatabaseReference profile = database.getReference("RESTAURANT/");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_menu);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());




                    myref.addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            mobno.add(dataSnapshot.getKey());
                            System.out.println(mobno);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    System.out.println("check = "+mobno);
                    System.out.println("check = "+formattedPhoneNumber);
                    if (mobno.contains(formattedPhoneNumber)) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference dpref = storage.getReference("images/" + formattedPhoneNumber + "/dp.jpg");
                        final long ONE_MEGABYTE = 1024 * 1024;

                        dpref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Data for "images/island.jpg" is returns, use this as needed
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ImageView imageView = findViewById(R.id.profile_image);
                                imageView.setImageBitmap(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        myref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                User copy = dataSnapshot.child(formattedPhoneNumber).child("/").getValue(User.class);
                                TextView name = findViewById(R.id.name);
                                name.setText(copy.firstname + " " + copy.lastname);
                                fullname = copy.firstname + " " + copy.lastname;

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "This Mobile Number is not Registered, Please register!!",Toast.LENGTH_LONG).show();
                        finish();
                        //Intent intent = new Intent(this, com.adithya.restaurant.UserDetails.class);
                        //startActivity(intent);
                        System.out.println("failed");

                    }

                }

            }

            @Override
            public void onError(final AccountKitError error) {
                // display error
                String toastMessage = error.getErrorType().getMessage();
                Toast.makeText(RestaurantsMenu.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });


    }







    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restaurants_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(this, yourbookings.class);
            startActivity(intent);

        } else if (id == R.id.nav_tools) {

                Intent intent = new Intent(this, com.adithya.restaurant.AccountActivity.class);
                startActivity(intent);



        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        }
        catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }
    public void R1(View view){
    myref.child(formattedPhoneNumber).child("/").child("restaurantchosen").setValue("R1");

    profile.child("R1/").child(formattedPhoneNumber).child("name").setValue(fullname);
    Intent intent = new Intent(this,Restaurant1.class);
    startActivity(intent);
    }
    public void R2(View view) {
        myref.child(formattedPhoneNumber).child("/").child("restaurantchosen").setValue("R2");

        profile.child("R2/").child(formattedPhoneNumber).child("name").setValue(fullname);
        Intent intent = new Intent(this,Restaurant2.class);
        startActivity(intent);
    }
    public void R3(View view){
        myref.child(formattedPhoneNumber).child("/").child("restaurantchosen").setValue("R3");

        profile.child("R3/").child(formattedPhoneNumber).child("name").setValue(fullname);
        Intent intent = new Intent(this,Restaurant3.class);
        startActivity(intent);
    }

}
