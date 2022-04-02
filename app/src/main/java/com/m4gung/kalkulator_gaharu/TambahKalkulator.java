package com.m4gung.kalkulator_gaharu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TambahKalkulator extends DialogFragment {
    @BindView(R.id.et_berat)
    EditText etBerat;
//    @BindView(R.id.sp_kelas)
//    Spinner spKelas;

    Unbinder unbinder;
    RecyclerView recyclerView;
    Activity activity;
    ArrayList<Kalkulator> kalkulatorArrayList = new ArrayList<>();
    ArrayList<KelasData> kelasData = new ArrayList<>();

    public TambahKalkulator(RecyclerView recyclerView, Activity activity, ArrayList<Kalkulator> kalkulatorArrayList, ArrayList<KelasData> kelasData) {
        this.recyclerView = recyclerView;
        this.activity = activity;
        if(kalkulatorArrayList != null)
        this.kalkulatorArrayList = kalkulatorArrayList;
        if(kelasData != null)
        this.kelasData = kelasData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.form_tambah_kalkulator, null);
        Spinner spinner = (Spinner) view.findViewById(R.id.sp_kelas);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < kelasData.size(); i++) {
            arrayList.add(kelasData.get(i).getNama());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        builder.setTitle("Tambah")
                .setView(view)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nama;
                        int harga, berat;

                        if(etBerat.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Isian tidak boleh kosong", Toast.LENGTH_LONG).show();
                        } else {
                            nama = String.valueOf(spinner.getSelectedItem());
                            berat = Integer.parseInt(String.valueOf(etBerat.getText()));
                            harga = kelasData.get(spinner.getSelectedItemPosition()).getHarga() * berat;
                            kalkulatorArrayList.add(new Kalkulator(nama, harga, berat));
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(kalkulatorArrayList);
                            editor.putString("kalkulator_data", json);
                            editor.apply();
                            Intent intent = new Intent(view.getContext(), MainActivity.class);

                            view.getContext().startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        return builder.create();
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
