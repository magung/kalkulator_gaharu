package com.m4gung.kalkulator_gaharu;

public class Kalkulator {
    String nama;
    int harga;
    int berat;

    public Kalkulator(String nama, int harga, int berat) {
        this.nama = nama;
        this.harga = harga;
        this.berat = berat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }
}
