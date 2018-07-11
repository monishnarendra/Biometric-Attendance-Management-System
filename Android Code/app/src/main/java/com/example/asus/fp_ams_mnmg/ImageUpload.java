package com.example.asus.fp_ams_mnmg;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ImageUpload extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    ImageView imageView;
    EditText txtImageName;
    Uri ImageUri;

    public static final String FB_Storage_Path = "profileImages/";
    public static final String FB_Database_Path = "Users";

    private static final int REQUEST_CODE = 1234;

    ProgressBar progressBar;
    String Email_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        Email_Id = getIntent().getStringExtra("Email_Id");
        Email_Id = EncodeString(Email_Id);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_Database_Path + "/" + Email_Id);

        imageView = (ImageView) findViewById(R.id.imageView);
        txtImageName = (EditText) findViewById(R.id.editText9);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);

        mAuth = FirebaseAuth.getInstance();
    }

    public void ProfilePicImage(View view){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Please Select Your Profile Image"),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            ImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),ImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void SaveButton(View view){
        if(ImageUri != null){
            progressBar.setVisibility(View.VISIBLE);
            StorageReference ref = mStorageRef.child(FB_Storage_Path + System.currentTimeMillis() + "." + getImageExt(ImageUri));
            ref.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"User Image Successfull Uploaded",Toast.LENGTH_LONG).show();
                    ImageUploader imageUploader = new ImageUploader(txtImageName.getText().toString(),taskSnapshot.getDownloadUrl().toString());
                    //String UploadID = mDatabaseRef.push().getKey();
                    String UploadID = mDatabaseRef.push().getKey();

                    Log.i("ADebugTag","UploadID: " + UploadID);
                    Log.i("ADebugTag","imageUpload- URL: " + imageUploader.url);
                    Log.i("ADebugTag","imageUpload- Name: " + imageUploader.name);
                    Log.i("ADebugTag","imageUpload: " + imageUploader);

                    Toast.makeText(getApplicationContext(),UploadID,Toast.LENGTH_LONG).show();
                    mDatabaseRef.child("Download Link").child("Link").setValue(imageUploader.getUrl());
                    mDatabaseRef.child("Download Link").child("DisplayName").setValue(imageUploader.getName());
                    Toast.makeText(ImageUpload.this,"Please Enter Your Credential and SignIn",Toast.LENGTH_LONG).show();
                    SignOut();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"Please Select Image",Toast.LENGTH_LONG).show();
        }
    }

    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,Login.class));
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
}
