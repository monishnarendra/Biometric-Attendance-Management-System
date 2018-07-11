package com.example.asus.fp_ams_mnmg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {

    private EditText et_Email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        et_Email = (EditText) findViewById(R.id.editText4);
        Button button_Password_Reset = (Button) findViewById(R.id.button4);
    }

    public void ResetPass(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        String Email_id = et_Email.getText().toString().trim();
        if(Email_id.isEmpty()){
            et_Email.setError("Email ID is Required");
            et_Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email_id).matches()){
            et_Email.setError("Please Enter a Valid Email ID");
            et_Email.requestFocus();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(Email_id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PasswordReset.this,"Password Reset Email Sent",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(PasswordReset.this,Login.class));
                }else {
                    Toast.makeText(PasswordReset.this,"Error Mail Not Recognized",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
