package com.m4gung.kalkulator_gaharu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TambahKelas extends DialogFragment {
    @BindView(R.id.et_nama_kelas)
    EditText etNamaKelas;
    @BindView(R.id.et_harga)
    EditText etHarga;

    Unbinder unbinder;
    RecyclerView recyclerView;
    Activity activity;
    ArrayList<KelasData> kelasData = new ArrayList<>();

    public TambahKelas(RecyclerView recyclerView, Activity activity, ArrayList<KelasData> kelasDataArrayList) {
        this.recyclerView = recyclerView;
        this.activity = activity;
        if(kelasDataArrayList != null) {
            this.kelasData = kelasDataArrayList;
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Tambah Kelas")
                .setView(R.layout.form_tambah_kelas)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nama;
                        int harga;
                        if(etNamaKelas.getText().toString().equals("") || etHarga.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Isian tidak boleh kosong", Toast.LENGTH_LONG).show();
                        } else {
                            nama = String.valueOf(etNamaKelas.getText());
                            harga = Integer.parseInt(String.valueOf(etHarga.getText()));
                            kelasData.add(new KelasData(nama, harga));
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(kelasData);
                            editor.putString("kelas_data", json);
                            editor.apply();
                            Toast.makeText(getActivity(), "Kelas berhasil disimpan", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(this, getDialog());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
