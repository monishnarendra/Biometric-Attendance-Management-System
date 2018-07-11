package com.example.asus.fp_ams_mnmg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Attendance extends AppCompatActivity {

    private TextView tvtest,HW,TD;
    private String Email,FPID,ii,oo,dd,s = " ";
    private int HoursWorked = 0;
    ProgressBar progressBar;
    String date = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());
    ArrayList<String> Date = new ArrayList<String>();
    ArrayList<Integer> InTime = new ArrayList<Integer>();
    ArrayList<Integer> OutTime = new ArrayList<Integer>();
    CardView totaldays,hoursworked,details,graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Email = getIntent().getStringExtra("Email");
        FPID = getIntent().getStringExtra("FPID");


        tvtest = findViewById(R.id.textView13);
        HW = findViewById(R.id.textView25);
        TD = findViewById(R.id.textView26);
        progressBar = (ProgressBar) findViewById(R.id.progressBar5);
        totaldays = (CardView) findViewById(R.id.totaldays);
        details = (CardView) findViewById(R.id.details);
        hoursworked = (CardView) findViewById(R.id.hoursworked);
        graph = (CardView) findViewById(R.id.graph);

        Toast.makeText(Attendance.this, "Email : " + Email, Toast.LENGTH_SHORT).show();
        Toast.makeText(Attendance.this, "FPID : " + FPID, Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(FPID);
                mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Gson gson = new Gson();
                    final String s1 = gson.toJson(dataSnapshot.getValue());

                    String dt = "1-4-2018";  // Start date
                    SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
                    Calendar c = Calendar.getInstance();
                    int i = 0,TotalDays = 0;

                    while (!Objects.equals(dt, date)) {
                        try {
                            c.setTime(sdf.parse(dt));

                            c.add(Calendar.DATE, 1);  // number of days to add
                            dt = sdf.format(c.getTime());
                            Toast.makeText(Attendance.this, "Incremented Date : " + dt, Toast.LENGTH_SHORT).show();
                            TotalDays++;
                            JSONObject jsonObject = new JSONObject(s1);

                            JSONObject object = jsonObject.getJSONObject(dt);
                            dd = object.getString("Date");
                            ii = object.getString("Arrival Time");
                            oo = object.getString("Departed Time");
                            if(dd != null || ii != null || oo != null) {
                                Date.add(dd);
                                InTime.add(Integer.parseInt(ii));
                                OutTime.add(Integer.parseInt(oo));
                                s = s + "Date: " + Date.get(i) + " Arrival Time: " + InTime.get(i) + " Departed Time: " + OutTime.get(i) + "\n";
                                i++;
                            }

                        } catch(JSONException | ParseException e){
                            e.printStackTrace();
                        }
                    }

                    for(i = 0;i < Date.size(); i++){
                        HoursWorked = HoursWorked + (OutTime.get(i) - InTime.get(i));
                    }

                    s = s + "Size : " + Date.size() + "\n";
                    s = s + "Hours Worked : " + HoursWorked + "\n";
                    s = s + "Total Days : " + TotalDays + "\n";

                    tvtest.setText(s);
                    HW.setText(new StringBuilder().append(HoursWorked).append(" ").append(" ").toString());
                    TD.setText(new StringBuilder().append(TotalDays).append(" ").toString());
                    progressBar.setVisibility(View.GONE);
                    totaldays.setVisibility(View.VISIBLE);
                    hoursworked.setVisibility(View.VISIBLE);
                    details.setVisibility(View.VISIBLE);
                    graph.setVisibility(View.VISIBLE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Attendance.this, "Failed To Retrieve Data", Toast.LENGTH_SHORT).show();
                }
            });
            }
        }, 7000);

    }

    public String EncodeEmail(String emailAddress){
        emailAddress = emailAddress.replace(".", ",");
        return emailAddress;
    }

    public String DecodeEmail(String emailAddress) {
        emailAddress = emailAddress.replace(",", ".");
        return emailAddress;
    }

    public void GetDetails(View view){
        Intent i = new Intent(Attendance.this,DetailedView.class);
        i.putExtra("Email",Email);
        i.putExtra("FPID",FPID);
        i.putStringArrayListExtra("Date",Date);
        i.putIntegerArrayListExtra("OutTime",OutTime);
        i.putIntegerArrayListExtra("InTime",InTime);
        startActivity(i);
    }

}
