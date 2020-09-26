package com.dhiman.crackify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CaptureImageActivity extends AppCompatActivity {
    private static final String TAG = "CaptureImageActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView ivPreviewImage;
    private Button btCaptureButton,btCropImageButton;
    Uri photoUri;
    File image;
    private View.OnClickListener openCameraListener, cropImageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        //Checking the permissions necessary for this class
        checkAllPermissions();

        //Initialize the global objects
        init();
    }

    /**
     * Function: This method uses the "Dextor Lib"
     * Link: https://github.com/Karumi/Dexter
     * Dextor lib is very useful to check run time permission
     */
    //START "checkAllPermission method"
    private void checkAllPermissions(){
        Log.d(TAG, "checkAllPermissions: Inside the method");
        //Creating a Composite Listener which contains more than one listeners
        MultiplePermissionsListener compositeListener = createListenerForPermission();

        Dexter.withActivity(CaptureImageActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .withListener(compositeListener)
                .check();

    }
    //END "checkAllPermission method"

    /**
     * Function: This method creates listeners for both the cases
     * 1. If user granted the permission
     * 2. If user denied the permission
     * @return
     */
    //START "createListenerForPermission method"
    private MultiplePermissionsListener createListenerForPermission(){
        Log.d(TAG, "createListenerForPermission: Inside the method");

        //If User Granted/Denied  the Request Permission
        MultiplePermissionsListener multiplePermissionsListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();

            }

        };

        //If user Denied the Permission then show a msg using this listener
        MultiplePermissionsListener dialogueMultiplePermissionListener
                = DialogOnAnyDeniedMultiplePermissionsListener.Builder
                .withContext(CaptureImageActivity.this)
                .withTitle("Camera & Gallery Permission")
                .withMessage("Both Camera and Gallery Permission needed to take picture. Restart the app or you can grand them in app settings")
                .withButtonText("OK")
                .build();




        MultiplePermissionsListener compositeListeners = new CompositeMultiplePermissionsListener(multiplePermissionsListener,dialogueMultiplePermissionListener);

        return(compositeListeners);
    }
    //END "createListenerForPermission method"

    /**
     * Function: This method initializes the global objects
     */
    //START "init method"
    private void init(){
        Log.d(TAG, "init: ***Inside method***");
        ivPreviewImage = findViewById(R.id.ivPreviewImageId);
        btCaptureButton = findViewById(R.id.btCaptureButtonId);
        btCropImageButton = findViewById(R.id.btCropButtonId);

        /**
         * Disabling the button "btCropImageButton" at the starting of the activity
         * so that no one can access it before taking photo and changing it's opacity
         * for the look and feel >_<
         * will Enable the button after taking photo
         */
        btCropImageButton.setEnabled(false);
        btCropImageButton.setAlpha(0.5f);
        btCaptureButton.setText("Capture");


        //Initializing the listeners and add to button
        initializeListeners();
    }
    //END "init method"

    /**
     * Function: This method is used to initialize all the listeners
     * and also add the listener with their respective buttons
     */
    //START "initializeListeners method"
    private void initializeListeners(){
        Log.d(TAG, "initializeListeners: ***Inside the method***");

        openCameraListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        };

        cropImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        };

        //Adding listeners to Buttons
        btCaptureButton.setOnClickListener(openCameraListener);
        btCropImageButton.setOnClickListener(cropImageListener);


    }
    //END "initializeListeners method"

    /**
     * Function: This method will do everything i.e. from opening the camera
     * to create file (image)
     * This method is linked to other method also
     */
    //START "dispatchTakePictureIntent method"
    public void dispatchTakePictureIntent(){
        Log.d(TAG, "dispatchTakePictureIntent: ***Inside the method***");

        //Creating an Empty File to store the Image
        createEmptyImageFile();

        //Accessing the in build camera of the device
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Storing the Image on SD card
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        //Tell the Camera to Request Write Permission
        takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }

    }
    //END "dispatchTakePictureIntent method"

    /**
     * Function: This method will create a Directory in the Public directory
     * It will create an empty file to store the image
     *
     */
    //START "createEmptyImageFile method"
    private void createEmptyImageFile(){
        Log.d(TAG, "createEmptyImageFile: ***Inside the method***");

        //Choose a directory or create a directory so that other app can access this directory
        File storeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Image Cropper");

        //Creating an Image FileName
        String imageName = createImageFileName();

        //Creating an Empty File
        try {
            image = new File(storeDir, imageName + ".jpg");
        }
        catch (Exception e){
            Log.d(TAG, "saveImage: ***Error while creating Empty File");
        }


        //Creating directory on the SD card
        if(!storeDir.exists()){
            if(!storeDir.mkdirs()){
                Log.d(TAG, "createImageFile: Error while creating CameraIntent Directory");
            }
        }


        // Using the FileProvider to get the URI
        if(image != null){
            Log.d(TAG, "createEmptyImageFile: Image File is not NULL" + getApplicationContext().getPackageName());
            photoUri = FileProvider.getUriForFile(CaptureImageActivity.this,getApplicationContext().getPackageName() +".fileprovider",image );
            if(photoUri != null){
                Log.d(TAG, "createEmptyImageFile: PhotoURI is Null!! Something is WRONG!!!!");
            }

        }


    }
    //END "createEmptyImageFile method"

    /**
     * Function: This method is used to create File Name/Image Name
     * using the current time stamp method so that every name is unique
     * @return
     */
    //START "createImageFile method"
    private String createImageFileName(){
        Log.d(TAG, "createImageFileName: ***Inside the method***");

        //Create Image File name using the timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
        String imageFileName = "JEPG_" + timeStamp;
        return imageFileName;

    }
    //END "createImageFile method"

    /**
     * Function: When get the result or when you successfully capture the picture
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    //START "onActivityResult method"
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handling Activity For Image Capture For Camera Intent
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            //Setting the Preview of the Image
            try {

                ivPreviewImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri));

                /**
                 * Setting the "Crop Button" from disable to Enable
                 * Also Setting the "Capture Button" to "Recapture Button"
                 */
                btCropImageButton.setEnabled(true);
                btCropImageButton.setAlpha(1f);
                btCaptureButton.setText("Recapture");

                //To see the photo in Gallery we need to refresh it
                showPhotoinGallery();
            }
            catch (Exception e){
                Log.d(TAG, "onActivityResult: Error while Previewing the Image");
            }

        }


        //Hnadling Activity for Image Cropping using the Lib "Android Image Cropper"
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Getting the image URI");
                photoUri = result.getUri();
                try {
                    ivPreviewImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri));

                }
                catch (Exception e){
                    Log.d(TAG, "onActivityResult: Error while setting the Cropped Image");
                }
            }
            else{
                Log.d(TAG, "onActivityResult: Error while getting the Image URI");
            }
        }
    }
    //END "onActivityResult method"

    /**
     * Function: When you capture an Image using your own app
     * it doesn't show the image in the Gallery
     * You have to refresh the "in build" Gallery app
     */
    //START "saveImage method"
    private void showPhotoinGallery() {
        Log.d(TAG, "showPhotoinGallery: ****Inside the method****");

        //To see the Image in the Gallery, you need to add this line
        MediaScannerConnection.scanFile(this, new String[]{image.toString()}, null, new MediaScannerConnection.OnScanCompletedListener()
        {
            @Override
            public void onScanCompleted(String path, Uri uri) {

            }
        });
    }
    //END "saveImage method"

    /**
     * This method will use the Lib "Android Image Cropper"
     * Link : https://arthurhub.github.io/Android-Image-Cropper/
     */
    //START "cropImage method"
    public void cropImage(){
        Log.d(TAG, "cropImage: ***Inside the method***");
        //Start picker to get Image for Cropping and use the image in Cropping Activity
        CropImage.activity(photoUri).setGuidelines(CropImageView.Guidelines.ON).start(this);



    }
    //END "cropImage method"


}
