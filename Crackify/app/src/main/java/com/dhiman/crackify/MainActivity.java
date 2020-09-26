package com.dhiman.crackify;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //Timeout time for the welcome screen
    private static int SPLASH_TIMEOUT = 3000;
    //private ImageView ivSplashScreen;
    private TextView tvSplashScreenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Function: Splash Screen - text "Loading" blinks for 1 second
         */
        tvSplashScreenText = findViewById(R.id.tvSplashScreenTextId);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        tvSplashScreenText.startAnimation(animation);

        //Code for the Welcome Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Checking if the User is already logged in or not?
                //If logged in send him/her to that particular class
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null){
                    //Calling the SubjectsActivity which will give you the option to select Subjects
                    Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    //Calling the HomeActivity which will give you the option for SignUp & Login
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_TIMEOUT);

    }




}
