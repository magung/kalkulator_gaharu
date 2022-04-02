package com.m4gung.kalkulator_gaharu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvKalkulator extends RecyclerView.Adapter<RvKalkulator.KalkulatorViewHolder> {

    Context context;
    Activity activity;
    ArrayList<Kalkulator> kalkulatorArrayList;

    public RvKalkulator(Context context, Activity activity, ArrayList<Kalkulator> kalkulatorArrayList) {
        this.context = context;
        this.activity = activity;
        if(kalkulatorArrayList != null) {
            this.kalkulatorArrayList = kalkulatorArrayList;
        }
    }

    @NonNull
    @Override
    public RvKalkulator.KalkulatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.kalkulator_row_layout, parent, false);
        return new KalkulatorViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull RvKalkulator.KalkulatorViewHolder holder, int position) {
        final String nama = kalkulatorArrayList.get(position).getNama();
        final int harga = kalkulatorArrayList.get(position).getHarga(), berat = kalkulatorArrayList.get(position).getBerat();
        holder.nama.setText(nama);
        holder.harga.setText(String.valueOf(harga));
        holder.berat.setText(String.valueOf(berat));
        int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Nama Kelas : " + nama + "\nHarga : " + String.valueOf(harga) + "\nKg : " + berat)
                        .setCancelable(true)
                        .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                kalkulatorArrayList.remove(pos);
                                notifyDataSetChanged();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(kalkulatorArrayList);
                                editor.putString("kalkulator_data", json);
                                editor.apply();
                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                view.getContext().startActivity(intent);
                            }
                        })
                        .setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kalkulatorArrayList.size();
    }

    public class KalkulatorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.kal_kelas)
        TextView nama;
        @BindView(R.id.kal_harga_kelas)
        TextView harga;
        @BindView(R.id.kal_berat)
        TextView berat;
        public KalkulatorViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
