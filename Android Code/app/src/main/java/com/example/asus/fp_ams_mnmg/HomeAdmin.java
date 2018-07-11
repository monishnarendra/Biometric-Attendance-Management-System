package com.example.asus.fp_ams_mnmg;

import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeAdmin extends AppCompatActivity {
    protected LocationManager locationManager;
    private DatabaseReference mDatabase;
    private String Name,Email_Id,FPID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(HomeAdmin.this, "Entered MainPage", Toast.LENGTH_SHORT).show();

        Email_Id = getIntent().getStringExtra("Email");
        Name = getIntent().getStringExtra("Name");


        getSupportActionBar().setTitle("Welcome " + Name);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Email_Id = EncodeEmail(Email_Id);
                FPID = dataSnapshot.child("Users").child(Email_Id).child("FPID").getValue().toString();
                Email_Id = DecodeEmail(Email_Id);
                Toast.makeText(HomeAdmin.this, "FPID: " + FPID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Toast.makeText(HomeAdmin.this, "Name: " + Name, Toast.LENGTH_SHORT).show();
    }

    public void ProfileCV(View view){
        Intent i = new Intent(HomeAdmin.this,Profile.class);
        i.putExtra("Email",Email_Id);
        startActivity(i);
    }

    public void ViewAttendance(View view){

        Intent i = new Intent(HomeAdmin.this,Attendance.class);
        i.putExtra("Email",Email_Id);
        i.putExtra("FPID",FPID);
        startActivity(i);
    }

    public String EncodeEmail(String emailAddress){
        emailAddress = emailAddress.replace(".", ",");
        return emailAddress;
    }

    public String DecodeEmail(String emailAddress) {
        emailAddress = emailAddress.replace(",", ".");
        return emailAddress;
    }
    public void LocationCV(View view){
        Intent i = new Intent(HomeAdmin.this, com.example.asus.fp_ams_mnmg.Location.class);
        i.putExtra("Email",Email_Id);
        startActivity(i);
    }
}
