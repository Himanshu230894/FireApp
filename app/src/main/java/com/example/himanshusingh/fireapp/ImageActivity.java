package com.example.himanshusingh.fireapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageActivity extends AppCompatActivity {

    private Button mUploadBtn;
    private ImageView mImageView;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    Uri photoURI;

    private static final int CAMERA_REQUEST_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mUploadBtn= (Button)findViewById(R.id.upload);
        mImageView= (ImageView) findViewById(R.id.imageView);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
               // dispatchTakePictureIntent();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK){

            mProgressDialog.setMessage("Uploading Image...");
            mProgressDialog.show();

//get the camera image
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();

//set the image into imageview
            mImageView.setImageBitmap(bitmap);

            /*************** UPLOADS THE PIC TO FIREBASE***************/
            //Firebase storage folder where you want to put the images
            //StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://...");

//name of the image file (add time to have different files to avoid rewrite on the same file)

            StorageReference imagesRef = mStorage.child("Photos").child("Photos_id" + new Date().getTime());

//upload image

            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

//handle success


                    mProgressDialog.dismiss();
                    Toast.makeText(ImageActivity.this,"Upload done!",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
