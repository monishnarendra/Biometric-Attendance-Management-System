package com.example.asus.fp_ams_mnmg;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    Double currentUserlat,currentUserlng,User1lat,User1lng,User2lat,User2lng;
    String Email_Id,User1,User2;
    ArrayList<String> Users = new ArrayList<String>();;
    double[] Userslat = new double[50];
    double[] Userslng = new double[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Email_Id = getIntent().getStringExtra("Email");
        Users = getIntent().getStringArrayListExtra("Users");
        Userslat = getIntent().getDoubleArrayExtra("Userslat");
        Userslng = getIntent().getDoubleArrayExtra("Userslng");

        currentUserlat = getIntent().getDoubleExtra("lat",0);
        currentUserlng = getIntent().getDoubleExtra("lng",0);

        Toast.makeText(MapsActivity.this, "CurrentUser : Longitude: " + currentUserlat + " Latitude: " + currentUserlng, Toast.LENGTH_LONG).show();
        Toast.makeText(MapsActivity.this, "User1 : Longitude: " + Userslng[0] + " Latitude: " + Userslat[0], Toast.LENGTH_LONG).show();
        Toast.makeText(MapsActivity.this, "User2 : Longitude: " + Userslng[1] + " Latitude: " + Userslat[1], Toast.LENGTH_LONG).show();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("MapActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapActivity", "Can't find style. Error: ", e);
        }
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        MarkerOptions marker1 = new MarkerOptions().position(new LatLng(currentUserlat, currentUserlng)).title(Email_Id);
        googleMap.addMarker(marker1);

        MarkerOptions marker3 = new MarkerOptions().position(new LatLng(Userslat[1], Userslng[1])).title(Users.get(1));
        googleMap.addMarker(marker3);

        MarkerOptions marker2 = new MarkerOptions().position(new LatLng(Userslat[0], Userslng[0])).title(Users.get(0));
        googleMap.addMarker(marker2);

        MarkerOptions marker4 = new MarkerOptions().position(new LatLng(12.976611, 77.729087)).title("tejasjayanna009@gmail.com");
        googleMap.addMarker(marker4);
    }
}
