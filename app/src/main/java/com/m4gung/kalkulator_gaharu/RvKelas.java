package com.m4gung.kalkulator_gaharu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvKelas extends RecyclerView.Adapter<RvKelas.KelasViewHolder> {

    Context context;
    Activity activity;
    ArrayList<KelasData> kelasData;

    public RvKelas(Context context, Activity activity, ArrayList<KelasData> kelasData) {
        this.context = context;
        this.activity = activity;
        this.kelasData = kelasData;
    }

    @NonNull
    @Override
    public RvKelas.KelasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.kelas_row_layout, parent, false);
        return new KelasViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull RvKelas.KelasViewHolder holder, int position) {
        final String nama = kelasData.get(position).getNama();
        final int harga = kelasData.get(position).getHarga();
        holder.harga.setText(String.valueOf(harga));
        holder.nama_kelas.setText(nama);
        int pos = position;
        Bundle b = new Bundle();
        b.putString("b_nama", nama);
        b.putInt("b_harga", harga);
        b.putInt("b_posisi", pos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Nama Kelas : " + nama + "\nHarga : " + String.valueOf(harga))
                        .setCancelable(true)
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(view.getContext(), DetailKelas.class);
                                intent.putExtras(b);
                                view.getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                kelasData.remove(pos);
                                notifyDataSetChanged();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(kelasData);
                                editor.putString("kelas_data", json);
                                editor.apply();
                                Toast.makeText(context, "Kelas berhasil dihapus", Toast.LENGTH_SHORT).show();
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
        return kelasData.size();
    }

    public class KelasViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nama_kelas)
        TextView nama_kelas;
        @BindView(R.id.harga)
        TextView harga;
        public KelasViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
