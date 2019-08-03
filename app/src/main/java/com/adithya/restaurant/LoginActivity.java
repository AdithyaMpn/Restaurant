package com.adithya.restaurant;

import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Calendar;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("RESTAURANT/Users/");

    public static String uniqueKey;
    public static int APP_REQUEST_CODE = 1;
    private String[] state = { "Male", "Female"};
    private String[] state1 = { "Single", "Married"};
    private Spinner genderspinner;
    private Spinner genderspinner1;
    private EditText mfirstname;
    private EditText mlastname;
    private TextView mdate;
    private String mgender;
    private String mmaritalstatus;
    private EditText mabout;
    static String firstname;
    static String lastname;
    static String date;
    static String gender;
    static String maritulstatus;
    static String restaurant;


    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mfirstname = findViewById(R.id.editText2);
        mlastname = findViewById(R.id.editText4);
        mdate = findViewById(R.id.editText5);
//        mfirstname.setText(UserDetails.firstname);
//        mlastname.setText(UserDetails.lastname);


        genderspinner = (Spinner) findViewById(R.id.genderspinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter_state);




        genderspinner1 = (Spinner) findViewById(R.id.genderspinner1);
        ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state1);
        adapter_state1
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner1.setAdapter(adapter_state1);




        // check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            // if previously logged in, proceed to the account activity
            launchAccountActivity();
        }
        mDisplayDate = (TextView) findViewById(R.id.editText5);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        LoginActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };


    }






    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                // display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null) {

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        // Get Account Kit ID
                        String accountKitId = account.getId();

                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        if (account.getPhoneNumber() != null) {
                            // if the phone number is available, display it
                            String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());

                        }

                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // display error
                        String toastMessage = error.getErrorType().getMessage();
                        Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                    }
                });

                mgender = (String) genderspinner.getSelectedItem();
                mmaritalstatus = (String) genderspinner1.getSelectedItem();
                firstname = mfirstname.getText().toString();
                lastname = mlastname.getText().toString();
                date = mdate.getText().toString();
                gender = mgender;
                maritulstatus = mmaritalstatus;
                restaurant ="";


                // on successful login, proceed to the account activity
                launchAccountActivity();
            }
        }
    }

    private void onLogin(final LoginType loginType) {
        // create intent for the Account Kit activity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        // configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );
        final AccountKitConfiguration configuration = configurationBuilder.build();

        // launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
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

    public void onPhoneLogin(View view) {

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("onPhoneLogin");
        onLogin(LoginType.PHONE);
    }


    private void launchAccountActivity() {

        Intent intent = new Intent(this, com.adithya.restaurant.details.class);
        startActivity(intent);
        finishAffinity();
    }

}
