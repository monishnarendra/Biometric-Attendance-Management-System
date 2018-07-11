package com.example.asus.fp_ams_mnmg;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Message extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    EditText mobileno,message;
    Button sendsms;
    String Email_Id,PhoneNo;
    int n;
    ArrayList<String> paths = new ArrayList<String>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Email_Id = getIntent().getStringExtra("Email");
        paths = getIntent().getStringArrayListExtra("Users");

        mobileno = (EditText)findViewById(R.id.editText14);
        message = (EditText)findViewById(R.id.editText15);
        sendsms = (Button)findViewById(R.id.button7);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Message.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void SendSms(View view){
        String no = mobileno.getText().toString();
        String msg = message.getText().toString();
        Intent intent = new Intent(getApplicationContext(),Message.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(no, null, msg, pi,null);

        Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
    }

    public void SetText(final String UserId){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PhoneNo = dataSnapshot.child("Users").child(UserId).child("PhoneNo").getValue().toString();
                Toast.makeText(getApplicationContext(), "Phone: " + PhoneNo, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mobileno.setText(PhoneNo);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                SetText(paths.get(0));
                Toast.makeText(getApplicationContext(), paths.get(0), Toast.LENGTH_LONG).show();
                break;
            case 1:
                SetText(paths.get(1));
                Toast.makeText(getApplicationContext(), paths.get(1), Toast.LENGTH_LONG).show();
                break;
            case 2:
                SetText(paths.get(2));
                Toast.makeText(getApplicationContext(), paths.get(2), Toast.LENGTH_LONG).show();
                break;
            case 3:
                SetText(paths.get(3));
                Toast.makeText(getApplicationContext(), paths.get(3), Toast.LENGTH_LONG).show();
                break;
            case 4:
                SetText(paths.get(4));
                Toast.makeText(getApplicationContext(), paths.get(4), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
