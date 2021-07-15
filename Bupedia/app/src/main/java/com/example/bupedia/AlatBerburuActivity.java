package com.example.bupedia;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bupedia.adapter.AlatBerburuAdapter;
import com.example.bupedia.database.AlatDatabaseHelper;
import com.example.bupedia.database.QueryResponse;
import com.example.bupedia.model.AlatModel;

import java.util.ArrayList;

public class AlatBerburuActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AlatBerburuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alat_berburu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v->{
            onBackPressed();
        });
        recyclerView = (RecyclerView) findViewById(R.id.rvData);
        adapter = new AlatBerburuAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        AlatDatabaseHelper alatDatabaseHelper = new AlatDatabaseHelper();
        alatDatabaseHelper.getAlat(new QueryResponse<ArrayList<AlatModel>>() {
            @Override
            public void onSuccess(ArrayList<AlatModel> data, int idData) {
                adapter.addData(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


}
