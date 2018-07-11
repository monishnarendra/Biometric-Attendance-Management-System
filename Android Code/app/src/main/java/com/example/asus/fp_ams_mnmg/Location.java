package com.example.asus.fp_ams_mnmg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Location extends AppCompatActivity implements LocationListener {

    String Email_Id;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView t1,t2;
    Double currentUserlat,currentUserlng;
    private DatabaseReference mDatabase;
    private static final ArrayList<String> Users = new ArrayList<String>();;
    double[] Userslat = new double[50];
    double[] Userslng = new double[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Email_Id = getIntent().getStringExtra("Email");

        t1 = findViewById(R.id.textView22);
        t2 = findViewById(R.id.textView23);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Email_Id = EncodeEmail(Email_Id);

                for(int i = 1;i<3;i++){
                    Users.add(EncodeEmail(dataSnapshot.child("Users").child(Email_Id).child("Student List").child(String.valueOf(i)).child("Email ID").getValue().toString()));
                }

                t1.setText(Users.get(0));
                t2.setText(Users.get(1));

                for(int i = 0;i<2;i++){
                    Userslat[i] = (Double) dataSnapshot.child("Users").child(Users.get(i)).child("Location").child("Latitude").getValue();
                }

                for(int i = 0;i<2;i++){
                    Userslng[i] = (Double) dataSnapshot.child("Users").child(Users.get(i)).child("Location").child("Longitude").getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    @Override
    public void onLocationChanged(android.location.Location location) {
       // txtLat = (TextView) findViewById(R.id.textView14);
      //  txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        currentUserlat = location.getLatitude();
        currentUserlng = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public void SMS(View view){
        Intent i = new Intent(this,Message.class);
        i.putExtra("Email",Email_Id);
        i.putStringArrayListExtra("Users",Users);
        startActivity(i);
    }

    public void Openmap(View view){
        Intent i = new Intent(this,MapsActivity.class);
        i.putExtra("Email",Email_Id);
        i.putStringArrayListExtra("Users",Users);
        i.putExtra("Userslat",Userslat);
        i.putExtra("Userslng",Userslng);

        i.putExtra("lat",currentUserlat);
        i.putExtra("lng",currentUserlng);
        startActivity(i);
    }
    public String EncodeEmail(String emailAddress){
        emailAddress = emailAddress.replace(".", ",");
        return emailAddress;
    }
}
