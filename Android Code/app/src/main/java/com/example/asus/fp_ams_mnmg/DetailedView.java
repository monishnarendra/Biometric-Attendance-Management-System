package com.example.asus.fp_ams_mnmg;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.asus.fp_ams_mnmg.Adapter.MyAdapter;
import com.example.asus.fp_ams_mnmg.Interface.ILoadMore;
import com.example.asus.fp_ams_mnmg.Model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailedView extends AppCompatActivity {

    List<Item> items = new ArrayList<>();
    MyAdapter adapter;
    ArrayList<String> Date = new ArrayList<String>();
    ArrayList<Integer> InTime = new ArrayList<Integer>();
    ArrayList<Integer> OutTime = new ArrayList<Integer>();
    private String Email,FPID;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(recyclerView,this,items);
        recyclerView.setAdapter(adapter);

        Email = getIntent().getStringExtra("Email");
        FPID = getIntent().getStringExtra("FPID");
        Date = getIntent().getStringArrayListExtra("Date");
        InTime = getIntent().getIntegerArrayListExtra("InTime");
        OutTime = getIntent().getIntegerArrayListExtra("OutTime");

        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if(items.size() <= 20){
                    items.add(null);
                    adapter.notifyItemInserted(items.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(items.size()-1);
                            adapter.notifyItemRemoved(items.size());
                            int index = items.size();
                            int end = index + 10;

                            for (int i=0; i < Date.size();i++){
                                String date = Date.get(i);
                                int ii = InTime.get(i);
                                int oo = OutTime.get(i);
                                Item item = new Item(date,ii,oo);
                                items.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    },5000);
                }else {
                    Toast.makeText(DetailedView.this,"Data load Complete!!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        for (int i=0; i < Date.size();i++){
            String date = Date.get(i);
            int ii = InTime.get(i);
            int oo = OutTime.get(i);
            Item item = new Item(date,ii,oo);
            items.add(item);
        }
    }
}
