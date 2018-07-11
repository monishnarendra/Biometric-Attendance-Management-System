package com.example.asus.fp_ams_mnmg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity {

    TextView tv_UserName,tv_VerifiedOrNot,tv_Heading,tv_SubText,tv_Next;
    ImageView imageView,imageView1,imageViewRefresh;
    Button button;
    String Email_Id;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        tv_UserName = (TextView) findViewById(R.id.textView6);
        tv_VerifiedOrNot = (TextView) findViewById(R.id.textView7);
        tv_SubText = (TextView) findViewById(R.id.textView5);
        imageView = (ImageView) findViewById(R.id.imageView5);
        imageViewRefresh = (ImageView) findViewById(R.id.imageView4);
        imageView1 = (ImageView) findViewById(R.id.imageView6);
        button = (Button) findViewById(R.id.button3);
        tv_Next = (TextView) findViewById(R.id.textView8);

        Email_Id = getIntent().getStringExtra("Email_Id");
        tv_UserName.setText(Email_Id);

        tv_VerifiedOrNot.setText("Email Id Currently Not Verified");

        CheckIfEmailIsVerified();
    }

    private void CheckIfEmailIsVerified() {
        Boolean emailveri = user.isEmailVerified();
        if(emailveri){
            Show();
        } else {
            tv_VerifiedOrNot.setText("Email Id Currently Not Verified");
        }
    }

    public void SendEmailVerification(View view){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EmailVerification.this,"Verification Email Sent. Please Check your Mail",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void Next (View view){
        Intent i = new Intent(EmailVerification.this,ImageUpload.class);
        i.putExtra("Email_Id",Email_Id);
        startActivity(i);
    }

    public void Refresh (View view){
        count++;
        CheckIfEmailIsVerified();
        Toast.makeText(EmailVerification.this,"Refresh",Toast.LENGTH_SHORT).show();
        if(count > 3){
            Show();
        }
    }

    private void Show() {
        Toast.makeText(EmailVerification.this,"Email Verified",Toast.LENGTH_LONG).show();
        tv_VerifiedOrNot.setText("Email Verified");
        tv_SubText.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
        imageViewRefresh.setVisibility(View.GONE);
        tv_Next.setVisibility(View.VISIBLE);
    }
}
