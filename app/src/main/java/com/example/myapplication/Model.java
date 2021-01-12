package com.example.myapplication;

public class Model {

    private String image;
    private String title;
    private String tanggal;
    private String description;
    private String link;

    private Model(){}

    private Model(String image, String title, String tanggal, String description, String link){
        this.image = image;
        this.title = title;
        this.tanggal = tanggal;
        this.description = description;
        this.link = link;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
