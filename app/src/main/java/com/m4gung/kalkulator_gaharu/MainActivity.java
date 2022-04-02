package com.m4gung.kalkulator_gaharu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;

    ArrayList<Kalkulator> kalkulatorArrayList;
    ArrayList<KelasData> kelasDataArrayList;

    TextView totalHarga;
    TextView totalKg;

    int total_harga = 0, total_kg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Kalkulator Gaharu");
        rv = findViewById(R.id.rv_kalkulator);
        totalHarga = (TextView) findViewById(R.id.total_harga);
        totalKg = (TextView) findViewById(R.id.total_kg);
        getData();

        Button button = (Button) findViewById(R.id.kelas);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Intent intent = new Intent(getApplicationContext(), Kelas.class);
                startActivity(intent);
            }
        });

        Button buttonTambah = (Button) findViewById(R.id.tambah);
        buttonTambah.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                TambahKalkulator tambahKalkulator = new TambahKalkulator(rv, MainActivity.this, kalkulatorArrayList, kelasDataArrayList );
                tambahKalkulator.show(fm, "Kalkulator");
            }
        });

        Button buttonReset = (Button) findViewById(R.id.reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(new ArrayList<>());
                editor.putString("kalkulator_data", json);
                editor.apply();

                getData();
                totalHarga.setText("0");
                totalKg.setText("0");
            }
        });
    }

    public void showRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        RvKalkulator rvKalkulator = new RvKalkulator(getApplicationContext(), MainActivity.this, kalkulatorArrayList);
        rv.setAdapter(rvKalkulator);
    }

    public void getData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = preferences.getString("kelas_data", "");
        String jsonKal = preferences.getString("kalkulator_data", "");
        Type type = new TypeToken<ArrayList<KelasData>>() {}.getType();
        Type typeKal = new TypeToken<ArrayList<Kalkulator>>() {}.getType();
        if(json != "") {
            kelasDataArrayList = gson.fromJson(json, type);
        }
        if(jsonKal != "") {
            kalkulatorArrayList =  gson.fromJson(jsonKal, typeKal);
            if(kalkulatorArrayList.size() > 0) {
                for (int i = 0; i < kalkulatorArrayList.size(); i++) {
                    total_harga += kalkulatorArrayList.get(i).getHarga();
                    total_kg += kalkulatorArrayList.get(i).getBerat();
                }

            }
            showRv();
        } else  {
            kalkulatorArrayList = new ArrayList<>();
            showRv();
        }

        totalHarga.setText(String.valueOf(total_harga));
        totalKg.setText(String.valueOf(total_kg));


    }



}