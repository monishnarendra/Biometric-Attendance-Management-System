package com.example.asus.fp_ams_mnmg;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText et_Email, et_Password;
    ProgressBar progressBar;
    public String password,emailAddress,Name,Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        et_Email = (EditText)findViewById(R.id.editText);
        et_Password = (EditText)findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }

    public void ForgotPassword(View view){
        startActivity(new Intent(Login.this,PasswordReset.class));
    }

    public boolean InputVerify(String emailAddress, String password){

        if(emailAddress.isEmpty()){
            et_Email.setError("Email ID is Required");
            et_Email.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            et_Email.setError("Please Enter a Valid Email ID");
            et_Email.requestFocus();
        }

        if(password.isEmpty()){
            et_Password.setError("Password is Required");
            et_Password.requestFocus();
        }

        if(password.length() < 8){
            et_Password.setError("Password Must have Minimum 8 Charecters");
            et_Password.requestFocus();
        }

        if (!password.matches("^(?=.*[@$%&#_()=+?»«<>£§€{}\\[\\]-])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*")) {
            et_Password.setError("Password Must have at least 1 upper case letter \n " +
                    "Password Must have at least 1 lower case letter \n " +
                    "Password Must have at least 1 digit \n ");
            et_Password.requestFocus();
        }
        if (!emailAddress.equals("") && password.length() >= 8 && !password.equals("")
                && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            return true;
        }
        return false;
    }

    public void SignUpText(View view){
        startActivity(new Intent(Login.this,Registration.class));
    }

    public void SignInButton(View view){

        emailAddress = et_Email.getText().toString().trim();
        password = et_Password.getText().toString().trim();

        if(InputVerify(emailAddress,password)) {

            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                emailAddress = EncodeEmail(emailAddress);
                                Name = dataSnapshot.child("Users").child(emailAddress).child("Fname").getValue().toString();
                                Type = dataSnapshot.child("Users").child(emailAddress).child("Type").getValue().toString();

                                Toast.makeText(Login.this, Name, Toast.LENGTH_SHORT).show();
                                emailAddress = DecodeEmail(emailAddress);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(Login.this, "Failed To Retrieve Data", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Toast.makeText(Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if(Type.equals("Admin")){
                                    Intent i = new Intent(Login.this, HomeAdmin.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra("Name", Name);
                                    i.putExtra("Email", emailAddress);
                                    startActivity(i);
                                    finish();
                                }
                                if(Type.equals("User")){
                                    Intent i = new Intent(Login.this, HomeUser.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra("Name", Name);
                                    i.putExtra("Email", emailAddress);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }, 5000);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", task.getException().getMessage());
                        Toast.makeText(Login.this, "UnSuccessfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public String EncodeEmail(String emailAddress){
        emailAddress = emailAddress.replace(".", ",");
        return emailAddress;
    }

    public String DecodeEmail(String emailAddress) {
        emailAddress = emailAddress.replace(",", ".");
        return emailAddress;
    }
}
