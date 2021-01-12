package com.example.myapplication;

public class ProductsModel {

    private String BMI;
    private String Status;
    private String Tanggal;

    private ProductsModel(){}

    private ProductsModel(String BMI, String Status, String Tanggal){
        this.BMI = BMI;
        this.Status = Status;
        this.Tanggal = Tanggal;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        this.Tanggal = tanggal;
    }

}
