package com.dfreez3.spoilalert;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ScanFoodActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "ScanFood";

    private Button mTakePhotoButton;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_food);

        mTakePhotoButton = findViewById(R.id.take_photo_button);
        mTakePhotoButton.setOnClickListener(this);
        mImageView = findViewById(R.id.image_preview);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == mTakePhotoButton.getId()){
            // Ensure that the device actually has a camera
            PackageManager pm = getPackageManager();
            if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                // Fire off intent to take picture using default camera app
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, R.string.no_camera, Toast.LENGTH_LONG).show();
                Log.d(TAG, "No camera available on device");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the user successfully took a photo, display the thumbnail in an image view
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        } else {
            Log.d(TAG, "Completed activity is not one we were listening for");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensures that there is an application that can handle the intent before it is
        // dispatched
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Log.d(TAG, "No camera application installed to use camera hardware");
        }
    }

}
