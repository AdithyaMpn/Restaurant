package com.adithya.restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class yourbookings extends AppCompatActivity {

    private static final String TAG = "yourboookings";
    private ChildEventListener mchildeventlistener;
    private TextView val;

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("RESTAURANT/Users/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourbookings);

        val = findViewById(R.id.textView);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User copy=dataSnapshot.child(RestaurantsMenu.formattedPhoneNumber).child("/").getValue(User.class);
                val.setText(copy.restaurantchosen);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
