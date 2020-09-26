package com.dhiman.crackify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    //START "onCreate Method"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    //END "onCreate Method"


    /**
     * This method will bring you the Login Screen
     * @param view
     */
    //START "callLoginScreen Method"
    public void callLoginScreen(View view){
        Log.d(TAG, "callLoginScreen: ***Inside the the method***");

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    //END "callLoginScreen Method"


    /**
     * This method will bring you the Sign Up Screen
     * @param view
     */
    //START "callSignUpScreen Method"
    public void callSignUpScreen(View view){
        Log.d(TAG, "callSignUpScreen: ***Inside the method***");

        Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();

    }
    //END "callSignUpScreen Method"

}
//END "HomeActivity Class"