package com.m4gung.kalkulator_gaharu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Kelas extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<KelasData> kelasData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas);

        getSupportActionBar().setTitle("Kelas Kayu");
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.tambah_kelas);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                TambahKelas tambahKelas = new TambahKelas(rv, Kelas.this, kelasData);
                tambahKelas.show(fm, "Kelas");
            }
        });
        rv = findViewById(R.id.rv_kelas);

        getData();
    }

    public void showRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        RvKelas rvKelas = new RvKelas(getApplicationContext(), Kelas.this, kelasData);
        rv.setAdapter(rvKelas);
    }

    public void getData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = preferences.getString("kelas_data", "");
        Type type = new TypeToken<ArrayList<KelasData>>() {}.getType();
        if(json != "") {
            kelasData = gson.fromJson(json, type);
            showRv();
        } else {
            kelasData = new ArrayList<>();
            showRv();
        }

    }
}