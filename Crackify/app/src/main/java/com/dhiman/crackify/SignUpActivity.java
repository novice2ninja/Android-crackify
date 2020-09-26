package com.dhiman.crackify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    //Global Object
    private EditText etPhoneNumber;
    private Button btSignUpButton;


    //START "onCreate Method"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Calling Methods which needs to call before anything
        init();
    }
    //END "onCreate Method"

    /**
     * Description : This method will initialize all the global
     * variables
     */
    //START "init method"
    private void init(){
        etPhoneNumber = findViewById(R.id.etSignUpPhoneNumberId);
        btSignUpButton = findViewById(R.id.btSignUpButtonId);

    }
    //END "init method"

    /**
     * Description : This method will generate the OTP
     * Basically this function will call a class which will connect to
     * firebase authentication service
     * @param view
     */
    //START "generateVerificationCode Method"
    public void generateVerificationCode(View view){
        Log.d(TAG, "generateVerificationCode: ***Inside the method***");

        String phoneNumber = "+91" + etPhoneNumber.getText().toString().trim();
        Intent intent = new Intent(SignUpActivity.this, VeritfyPhoneAuthActivity.class);
        intent.putExtra("PhoneNumber",phoneNumber);
        startActivity(intent);
        finish();

    }
    //END "generateVerificationCode Method"
}
