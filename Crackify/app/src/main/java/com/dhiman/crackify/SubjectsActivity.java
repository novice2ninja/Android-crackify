package com.dhiman.crackify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SubjectsActivity extends AppCompatActivity {
    private static final String TAG = "SubjectsActivity";
    //Objects
    private Button btSubject1, btSubject2, btSubject3, btSubject4;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        Log.d(TAG, "onCreate: Inside the method");

        //To initialize the global instances
        init();

        //To add any listener to any button
        addListener();

    }

    /**
     * Function: This method will initialize all the global instances
     */
    //START "init method"
    private void init(){
        Log.d(TAG, "init: Inside the method");
        btSubject1 = findViewById(R.id.btSubject1Id);
        btSubject2 = findViewById(R.id.btSubject2Id);
        btSubject3 = findViewById(R.id.btSubject3Id);
        btSubject4 = findViewById(R.id.btSubject4Id);

        //Listener for the buttons
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Inside the method");

                switch (v.getId()){
                    case R.id.btSubject1Id:
                        //Toast.makeText(getApplicationContext(),"Button Clicked: "+v.getId(),Toast.LENGTH_LONG).show();
                        captureImage();
                        break;
                    case R.id.btSubject2Id:
                        captureImage();
                        break;
                    case R.id.btSubject3Id:
                        captureImage();
                        break;
                    case R.id.btSubject4Id:
                        captureImage();
                        break;
                }
            }
        };
    }
    //END "init method"

    /**
     * Funtion: Add any listener object to any button object
     */
    //START "addListener method"
    private void addListener(){
        Log.d(TAG, "addListener: Inside the method");
        btSubject1.setOnClickListener(onClickListener);
        btSubject2.setOnClickListener(onClickListener);
        btSubject3.setOnClickListener(onClickListener);
        btSubject4.setOnClickListener(onClickListener);
    }
    //END "addListener method"

    /**
     * Function: This method will Open the class CaptureImageActivity
     * which will capture image and crop the image
     */
    //START "captureImage method"
    private void captureImage(){
        Log.d(TAG, "captureImage: Inside the method");
        Intent intent = new Intent(SubjectsActivity.this,CaptureImageActivity.class);
        startActivity(intent);
    }
    //END "captureImage method"

}
