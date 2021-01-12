package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class info extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle("Profil Developer")
                .setContent("\n\n\nNoer Rachmat Ocavianto \n54417526 \nTeknik Informatika \nUniversitas Gunadarma ")
                .setBackgroundColor(Color.parseColor("#75D3C0")) // int background color
                .setDrawable(R.drawable.developer_1) // int top drawable
                .setSummary("Halaman 1")
                .build());

        addFragment(new Step.Builder().setTitle("Edukasi Pembelajaran")
                .setContent("\nFitur Menu Yang Menyediakan Penjelasan mengenai Aplikasi dan \nPembelajaran tentang BMI")
                .setBackgroundColor(Color.parseColor("#75D3C0")) // int background color
                .setDrawable(R.drawable.edukasi_info) // int top drawable
                .setSummary("Halaman 2")
                .build());

        addFragment(new Step.Builder().setTitle("Perhitungan OFFLINE")
                .setContent("\nFitur Menu Yang Menyediakan Perhitungan BMI \nTanpa melakukan registrasi LOGIN")
                .setBackgroundColor(Color.parseColor("#75D3C0")) // int background color
                .setDrawable(R.drawable.offline) // int top drawable
                .setSummary("Halaman 3")
                .build());

        addFragment(new Step.Builder().setTitle("Perhitungan ONLINE")
                .setContent("\nFitur Menu Yang Menyediakan perhitungan \nMelakukan LOGIN terlebih dahulu,\nAgar data dapat tersimpan")
                .setBackgroundColor(Color.parseColor("#75D3C0")) // int background color
                .setDrawable(R.drawable.online) // int top drawable
                .setSummary("Halaman 4")
                .build());

    }

    @Override
    public void finishTutorial() {
        startActivity(new Intent(getApplicationContext(), send_email.class));
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}
