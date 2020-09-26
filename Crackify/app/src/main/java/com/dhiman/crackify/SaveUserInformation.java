package com.dhiman.crackify;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaveUserInformation {
    private static final String TAG = "SaveUserInformation";

    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String ROOT = "user";


    /**
     * Function: This method will send to data to Firebase RealTime database
     * In this case it will send the user UID and PhoneNumber
     * @param firebaseAuth
     */
    //START "saveUserInfo method"
    public void saveUserInfo(FirebaseAuth firebaseAuth){
        Log.d(TAG, "saveUserInfo: Inside the method");

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        //if user already login
        if(currentUser != null){
            Log.d(TAG, "saveUserInfo: User Exists");

            //Get the user ID
            String UID = currentUser.getUid();
            Log.d(TAG, "saveUserInfo: UID: "+UID);
            //Get the Phone Number of the user
            String phoneNumber = currentUser.getPhoneNumber();
            Log.d(TAG, "saveUserInfo: Phone Number: "+phoneNumber);

            //Set to data to database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(UID).child("Phone Number").setValue(phoneNumber);


        }
        else{
            Log.d(TAG, "saveUserInfo: Invalid User");
            return;
        }


    }
    //END "saveUserInfo method"
}
