package com.example.asus.fp_ams_mnmg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView tvname,tvpassword,tvphone,tvemail,emailTV,tvid,tvfpid;
    private String Name,Email,Password,Phone,USN,FPID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your Profile ");

        tvname = findViewById(R.id.textView15);
        tvpassword = findViewById(R.id.textView16);
        tvemail = findViewById(R.id.textView17);
        tvphone = findViewById(R.id.textView18);
        tvid = findViewById(R.id.textView19);
        tvfpid = findViewById(R.id.textView20);

        Email = getIntent().getStringExtra("Email");
        Toast.makeText(Profile.this, "Email : " + Email, Toast.LENGTH_SHORT).show();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Email = EncodeEmail(Email);
                Name = dataSnapshot.child("Users").child(Email).child("Fname").getValue().toString();
                Password = dataSnapshot.child("Users").child(Email).child("Password").getValue().toString();
                Phone = dataSnapshot.child("Users").child(Email).child("PhoneNo").getValue().toString();
                USN = dataSnapshot.child("Users").child(Email).child("USN").getValue().toString();
                FPID = dataSnapshot.child("Users").child(Email).child("FPID").getValue().toString();
                Email = DecodeEmail(Email);

                Toast.makeText(Profile.this, "Email: " + Email, Toast.LENGTH_SHORT).show();
                Toast.makeText(Profile.this, "Name: " + Name, Toast.LENGTH_SHORT).show();
                Toast.makeText(Profile.this, "Password: " + Password, Toast.LENGTH_SHORT).show();
                Toast.makeText(Profile.this, "Phone: " + Phone, Toast.LENGTH_SHORT).show();
                Toast.makeText(Profile.this, "USN: " + USN, Toast.LENGTH_SHORT).show();
                Toast.makeText(Profile.this, "FPID: " + FPID, Toast.LENGTH_SHORT).show();

                tvname.setText(Name);
                tvemail.setText(Email);
                tvpassword.setText(Password);
                tvphone.setText(Phone);
                tvid.setText(USN);
                tvfpid.setText(FPID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Profile.this, "Failed To Retrieve Data", Toast.LENGTH_SHORT).show();
            }
        });
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
