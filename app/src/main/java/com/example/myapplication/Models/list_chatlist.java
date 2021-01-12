package com.example.myapplication.Models;

public class list_chatlist {
    private String id;
    private String username;
    private String status;
    private String pesan_terakhir;
    private String jumlah;
    private String foto;

    public list_chatlist() {
    }

    public list_chatlist(String id, String username, String status, String pesan_terakhir, String jumlah, String foto) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.pesan_terakhir = pesan_terakhir;
        this.jumlah = jumlah;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPesan_terakhir() {
        return pesan_terakhir;
    }

    public void setPesan_terakhir(String pesan_terakhir) {
        this.pesan_terakhir = pesan_terakhir;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
