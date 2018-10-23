package com.dfreez3.spoilalert;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScanFoodActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "ScanFood";

    private Button mTakePhotoButton;
    private ImageView mImageView;
    private String mCurrentPhotoPath;

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
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(image);
                analyzeImage(image);
            } else {
                Log.d(TAG, "Image capture intent not successful");
            }
        } else {
            Log.d(TAG, "Completed activity is not one we were listening for");
        }
    }

    private void analyzeImage(Bitmap imageBitmap){
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS).build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);
        FirebaseVisionImage visionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(visionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                        // Task completed successfully
                        processScannedBarcodes(barcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        Log.d(TAG, "Barcode scanning failed: ", e);
                    }
                });
    }

    private void processScannedBarcodes(List<FirebaseVisionBarcode> barcodes){
        int count = barcodes.size();
        if (count == 0){
            Toast.makeText(getApplicationContext(), "No barcodes found", Toast.LENGTH_SHORT).show();
        } else  {
            Toast.makeText(getApplicationContext(), "There were " + count + " barcodes scanned", Toast.LENGTH_SHORT).show();
        }

        for (FirebaseVisionBarcode barcode: barcodes) {
            Log.d(TAG, "Printing barcode info...");
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();

            int valueType = barcode.getValueType();

            // See API reference for complete list of supported types
            switch (valueType) {
                case FirebaseVisionBarcode.FORMAT_UPC_A:
                    Log.d(TAG, "Barcode is UPC A");
                    break;
                case FirebaseVisionBarcode.FORMAT_UPC_E:
                    Log.d(TAG, "Barcode is UPC E");
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensures that there is an application that can handle the intent before it is
        // dispatched
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Log.d(TAG, "No activity available to take picture");
        }
    }


}
