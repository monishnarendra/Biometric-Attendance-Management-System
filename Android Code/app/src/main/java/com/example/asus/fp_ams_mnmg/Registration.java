package com.example.asus.fp_ams_mnmg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    EditText et_FName, et_USN, et_Email, et_Password, et_PhoneNo, et_ConfirmPassword,et_FPID;
    TextView tv_Heading;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String User_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        et_FName = (EditText) findViewById(R.id.editText3);
        et_USN = (EditText) findViewById(R.id.editText7);
        et_Email = (EditText) findViewById(R.id.editText8);
        et_Password = (EditText) findViewById(R.id.editText5);
        et_ConfirmPassword = (EditText) findViewById(R.id.editText6);
        et_PhoneNo = (EditText) findViewById(R.id.editText10);
        et_FPID = (EditText) findViewById(R.id.editText12);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void RegisterUser(View view){
        String Fname = et_FName.getText().toString().trim();
        String USN = et_USN.getText().toString().trim();
        User_Email = et_Email.getText().toString().trim();
        String Password = et_Password.getText().toString().trim();
        String ConfirmPassword = et_ConfirmPassword.getText().toString().trim();
        String PhoneNo = et_PhoneNo.getText().toString().trim();
        String FPID = et_FPID.getText().toString().trim();

        if(Fname.isEmpty()){
            et_FName.setError("First Name is Required");
            et_FName.requestFocus();
            return;
        }

        if(USN.isEmpty()){
            et_USN.setError("USN is Required");
            et_USN.requestFocus();
            return;
        }

        if (!USN.matches("^\\d[a-zA-Z]\\w{1}\\d{2}[a-zA-Z]\\w{1}\\d{3}$")) {
            et_USN.setError("Please Enter a Valid USN");
            et_USN.requestFocus();
            return;
        }

        if(User_Email.isEmpty()){
            et_Email.setError("Email ID is Required");
            et_Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(User_Email).matches()){
            et_Email.setError("Please Enter a Valid Email ID");
            et_Email.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            et_Password.setError("Password is Required");
            et_Password.requestFocus();
            return;
        }

        if(Password.length() < 8){
            et_Password.setError("Password Must have Minimum 8 Charecters");
            et_Password.requestFocus();
            return;
        }

        if (!Password.matches("^(?=.*[@$%&#_()=+?»«<>£§€{}\\[\\]-])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*")) {
            et_Password.setError("Password Must have at least 1 upper case letter \n " +
                    "Password Must have at least 1 lower case letter \n " +
                    "Password Must have at least 1 digit \n ");
            et_Password.requestFocus();
            return;
        }

        if(!ConfirmPassword.matches(Password)){
            et_ConfirmPassword.setError("Must be Same as Above");
            et_ConfirmPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        UploadToDataBase(Fname,USN,Password,PhoneNo,FPID);

        mAuth.createUserWithEmailAndPassword(User_Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(),"User Registered Successfull",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Registration.this,EmailVerification.class);
                    i.putExtra("Email_Id",User_Email);
                    startActivity(i);
                }else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User Already Registered",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void UploadToDataBase(String Fname,String USN,String Password,String PhoneNo,String FPID){
        User_Email = EncodeString(User_Email);
        Toast.makeText(getApplicationContext(),User_Email,Toast.LENGTH_LONG).show();
        mDatabase.child("Users").child(User_Email).child("Fname").setValue(Fname);
        mDatabase.child("Users").child(User_Email).child("USN").setValue(USN);
        mDatabase.child("Users").child(User_Email).child("Password").setValue(Password);
        mDatabase.child("Users").child(User_Email).child("PhoneNo").setValue(PhoneNo);
        mDatabase.child("Users").child(User_Email).child("FPID").setValue(FPID);
        User_Email = DecodeString(User_Email);
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }

}
