package com.example.memoryspark.item;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_history")
public class ItemHistory implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String deckName;
    private String tanggal;
    private String nilai;
    private int jumlahBenar;
    private int jumlahSalah;

    // Constructor, Getter, dan Setter
    public ItemHistory(String deckName, String tanggal, String nilai, int jumlahBenar, int jumlahSalah) {
        this.deckName = deckName;
        this.tanggal = tanggal;
        this.nilai = nilai;
        this.jumlahBenar = jumlahBenar;
        this.jumlahSalah = jumlahSalah;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDeckName() { return deckName; }
    public void setDeckName(String deckName) { this.deckName = deckName; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getNilai() { return nilai; }
    public void setNilai(String nilai) { this.nilai = nilai; }

    public int getJumlahBenar() { return jumlahBenar; }
    public void setJumlahBenar(int jumlahBenar) { this.jumlahBenar = jumlahBenar; }

    public int getJumlahSalah() { return jumlahSalah; }
    public void setJumlahSalah(int jumlahSalah) { this.jumlahSalah = jumlahSalah; }
}