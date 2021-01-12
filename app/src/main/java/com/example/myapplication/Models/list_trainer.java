package com.example.myapplication.Models;

public class list_trainer {
    private String deskripsi;
    private String harga;
    private String nama;
    private String userID;
    private String foto;

    private list_trainer(){}

    private list_trainer(String deskripsi, String harga, String nama, String userID, String foto){
        this.deskripsi = deskripsi;
        this.foto = foto;
        this.harga = harga;
        this.nama = nama;
        this.userID = userID;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
