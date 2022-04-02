package com.m4gung.kalkulator_gaharu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailKelas extends AppCompatActivity {

    @BindView(R.id.detail_harga)
    EditText etHarga;
    @BindView(R.id.detail_nama_kelas)
    EditText etNama;
    @BindView(R.id.bt_hapus_kelas)
    Button btHapus;
    @BindView(R.id.bt_update_kelas)
    Button btUpdate;

    private  int posisi;

    ArrayList<KelasData> kelasData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kelas);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Detail Kelas");

        Bundle bundle = null;
        bundle = this.getIntent().getExtras();
        etNama.setText(bundle.getString("b_nama"));
        etHarga.setText(String.valueOf(bundle.getInt("b_harga")));
        posisi = bundle.getInt("b_posisi");

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama;
                int harga;
                nama = etNama.getText().toString();
                harga = Integer.parseInt(etHarga.getText().toString());
                kelasData.set(posisi, new KelasData(nama, harga));
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(kelasData);
                editor.putString("kelas_data", json);
                editor.apply();
                Intent intent = new Intent(view.getContext(), Kelas.class);
                view.getContext().startActivity(intent);
            }
        });
        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kelasData.remove(posisi);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(kelasData);
                editor.putString("kelas_data", json);
                editor.apply();
                Intent intent = new Intent(view.getContext(), Kelas.class);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = preferences.getString("kelas_data", "");
        Type type = new TypeToken<ArrayList<KelasData>>() {}.getType();
        if(json != "") {
            kelasData = gson.fromJson(json, type);
        }
    }
}