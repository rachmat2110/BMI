package com.example.myapplication.Models;

public class list_komentar {
    private String foto_pengguna;
    private String nama_pemesan;
    private String rating;
    private String komentar;

    private list_komentar(){}

    private list_komentar(String foto_pengguna, String nama_pemesan, String rating, String komentar){
        this.foto_pengguna = foto_pengguna;
        this.nama_pemesan = nama_pemesan;
        this.nama_pemesan = nama_pemesan;
        this.rating = rating;
        this.komentar = komentar;
    }

    public String getFoto_pengguna() {
        return foto_pengguna;
    }

    public void setFoto_pengguna(String foto_pengguna) {
        this.foto_pengguna = foto_pengguna;
    }

    public String getNama_pemesan() {
        return nama_pemesan;
    }

    public void setNama_pemesan(String nama_pemesan) {
        this.nama_pemesan = nama_pemesan;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }
}
