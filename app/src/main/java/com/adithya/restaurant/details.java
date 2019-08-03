package com.adithya.restaurant;

import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

public class details extends AppCompatActivity {

    private static final String TAG = "AccountActivity";
    private TextView genderspinner;
    private TextView genderspinner1;
    private TextView mfirstname;
    private TextView mlastname;
    private TextView mdate;
    static String formattedPhoneNumber;
    TextView infoLabel;
    TextView info;


    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("RESTAURANT/Users/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        mfirstname = findViewById(R.id.editText2);
        mlastname = findViewById(R.id.editText4);
        mdate = findViewById(R.id.editText5);
        genderspinner = findViewById(R.id.genderspinner);
        genderspinner1 = findViewById(R.id.genderspinner1);
        final ImageView imageView = findViewById(R.id.profile_image);



        infoLabel = (TextView) findViewById(R.id.info_label);
        info = (TextView) findViewById(R.id.info);





        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    info.setText(formattedPhoneNumber);
                    User obj = new User(LoginActivity.firstname, LoginActivity.lastname, LoginActivity.date, LoginActivity.gender,LoginActivity.maritulstatus, LoginActivity.restaurant);
                    myref.child(formattedPhoneNumber).setValue(obj);
                    Gson gson = new Gson();
                    String json = gson.toJson(obj);
                    System.out.println(json);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference dpref = storage.getReference("images/"+formattedPhoneNumber+"/dp.jpg");
                    dpref.putBytes(UserDetails.image);






                    myref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            User copy=dataSnapshot.child(formattedPhoneNumber).child("/").getValue(User.class);
                            mfirstname.setText(copy.firstname);
                            mlastname.setText(copy.lastname);
                            mdate.setText(copy.dob);
                            genderspinner.setText(copy.gender);
                            genderspinner1.setText(copy.maritalstatus);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }

            }

            @Override
            public void onError(final AccountKitError error) {
                // display error
                String toastMessage = error.getErrorType().getMessage();
                Toast.makeText(details.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });

    }



    public void next(View view) {
        Intent intent = new Intent(this, RestaurantsMenu.class);
        startActivity(intent);
        finishAffinity();
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

}
