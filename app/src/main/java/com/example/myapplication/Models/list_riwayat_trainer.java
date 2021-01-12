package com.example.myapplication.Models;

public class list_riwayat_trainer {
    private String nama_pemesan;
    private String foto_pengguna;
    private String no_hp;
    private String tanggal_pemesan;
    private String userID;

    private list_riwayat_trainer(){}

    private list_riwayat_trainer(String nama_pemesan, String foto_pengguna, String no_hp, String tanggal_pemesan, String userID){
        this.nama_pemesan = nama_pemesan;
        this.foto_pengguna = foto_pengguna;
        this.no_hp = no_hp;
        this.tanggal_pemesan = tanggal_pemesan;
        this.userID = userID;
    }

    public String getNama_pemesan() {
        return nama_pemesan;
    }

    public void setNama_pemesan(String nama_pemesan) {
        this.nama_pemesan = nama_pemesan;
    }

    public String getFoto_pengguna() {
        return foto_pengguna;
    }

    public void setFoto_pengguna(String foto_pengguna) {
        this.foto_pengguna = foto_pengguna;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
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
}
