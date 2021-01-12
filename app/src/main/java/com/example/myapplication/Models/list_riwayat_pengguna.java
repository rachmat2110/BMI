package com.example.myapplication.Models;

public class list_riwayat_pengguna {
    private String nama_trainer;
    private String foto_trainer;
    private String status;
    private String tanggal_pemesan;
    private String userID;
    private String ulasan;

    private list_riwayat_pengguna(){}

    private list_riwayat_pengguna(String nama_trainer, String foto_trainer, String status, String tanggal_pemesan, String userID, String ulasan){
        this.nama_trainer = nama_trainer;
        this.foto_trainer = foto_trainer;
        this.status = status;
        this.tanggal_pemesan = tanggal_pemesan;
        this.userID = userID;
        this.ulasan = ulasan;
    }

    public String getNama_trainer() {
        return nama_trainer;
    }

    public void setNama_trainer(String nama_trainer) {
        this.nama_trainer = nama_trainer;
    }

    public String getFoto_trainer() {
        return foto_trainer;
    }

    public void setFoto_trainer(String foto_trainer) {
        this.foto_trainer = foto_trainer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal_pemesan() {
        return tanggal_pemesan;
    }

    public void setTanggal_pemesan(String tanggal_pemesan) {
        this.tanggal_pemesan = tanggal_pemesan;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUlasan() {
        return ulasan;
    }

    public void setUlasan(String ulasan) {
        this.ulasan = ulasan;
    }
}
