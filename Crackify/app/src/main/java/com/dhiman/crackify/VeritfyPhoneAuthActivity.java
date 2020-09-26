package com.dhiman.crackify;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VeritfyPhoneAuthActivity extends AppCompatActivity {
    private static final String TAG = "VeritfyPhoneAuthActivity";
    private FirebaseAuth firebaseAuth;
    //private EditText etOTP;
    private String verificationId;
    //private Button btConfirmOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veritfy_phone_auth);

        //To initialize the global variables
        init();

        //To start the animation for "breathe in & breathe out
        textAnimation();

        //To get the data from intent
        getDataFromIntent();

        /**
         * Function: Basically If the code doesn't detect automatically
         * then the user have to put the code manually
         * So we need to call the function "verifyCode()"
         */
        //Adding Listener to that "Confirm OTP button"
        /*
        btConfirmOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting the code from the user manually
                String code = etOTP.getText().toString().trim();

                //If the code is empty
                if(code.isEmpty() || code.length() < 6){
                    etOTP.setError("Enter the Code...");
                    etOTP.requestFocus();
                    return;
                }

                //If code or OTP is not empty
                verifyCode(code);
            }
        });
        */


    }

    /**
     * Function: this method will initialize all the global varibals
     *
     */
    //START "init method"
    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        //etOTP = findViewById(R.id.etOTPId);
        //btConfirmOTP = findViewById(R.id.btConfirmOTPid);

    }
    //END"init method"

    /**
     * Function: this method will start the animation for the text "breathe in & breathe out"
     */
    //START "textAnimation method"
    private void textAnimation(){
        TextView tvBreatheIn = findViewById(R.id.tvBreatheInId);
        TextView tvBreatheOut = findViewById(R.id.tvBreatheOutId);

        Animation animBreatheIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        Animation animBreatheOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);

        tvBreatheIn.startAnimation(animBreatheIn);
        tvBreatheOut.startAnimation(animBreatheOut);
    }
    //END "textAnimation method"

    /**
     * Function: This method will provide you data which are sent from other intent
     * in this case it is "SignUpActivity class"
     *
     */
    //START "getDataFromIntent method"
    private void getDataFromIntent(){
        String phoneNumber = getIntent().getStringExtra("PhoneNumber");
        sendVarificationCode(phoneNumber);
    }
    //END "getDataFromInetent method"

    /**
     * Function: This method will send the varification code to that
     * Particular Phone Number
     * @param phoneNumber
     */
    //START "sendVarificationCode method"
    private void sendVarificationCode(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callBacks
        );
    }
    //END "sendVarificationCode method"

    /**
     * Function: This will do the following task:
     * 1. What to do after the code is sent
     * 2. What to do to complete verification
     * 3. What to do if verification failed
     */
    //START "callBacks object"
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //Storing the verification id
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //This will detect the OTP or the Code Automatically
            String code = phoneAuthCredential.getSmsCode();

            if(code != null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VeritfyPhoneAuthActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };
    //END "callBacks object"



    /**
     * Function: To verify the OTP with verificationID
     * @param code
     */
    //START "verifyCode method"
    public void verifyCode(String code){

        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        signinWithCredential(phoneAuthCredential);

    }
    //END "verifyCode method"

    /**
     * Function: If the code is correct then what you want to do
     * In this case referring to other Intent
     * Parent method is "verifyCode"
     * @param credential
     */
    //START "signinWithCredential method"
    private void signinWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VeritfyPhoneAuthActivity.this,"Successful",Toast.LENGTH_LONG).show();

                            //Saving the user information to the real time database
                            SaveUserInformation saveUserInformation = new SaveUserInformation();
                            saveUserInformation.saveUserInfo(firebaseAuth);

                            //Referring to the class where I want to send the user after SignUp
                            Intent intent = new Intent(VeritfyPhoneAuthActivity.this, SubjectsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(VeritfyPhoneAuthActivity.this,"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    //START "signinWithCredential method"
}