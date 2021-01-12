package com.example.myapplication.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class list_chat {
    private String message;
    private String receiver;
    private String sender;
    private boolean isseen;

    @ServerTimestamp
    private Date Tanggal;

    public list_chat() {
    }

    public list_chat(String message, String receiver, String sender, boolean isseen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.isseen = isseen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getTanggal() {
        return Tanggal;
    }

    public void setTanggal(Date tanggal) {
        Tanggal = tanggal;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
