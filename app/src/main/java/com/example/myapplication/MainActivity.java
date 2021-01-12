package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Animation atg, atg2, atgup;

    RelativeLayout btn_edukasi, btn_online, btn_offline, btn_info;
    LinearLayout login_trainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setTitle("BMI CALCULATOR");
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        atg2 = AnimationUtils.loadAnimation(this, R.anim.atg2);
        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atgup = AnimationUtils.loadAnimation(this, R.anim.atgup);

        btn_edukasi = findViewById(R.id.btn_edukasi);
        btn_online = findViewById(R.id.btn_online);
        btn_offline = findViewById(R.id.btn_offline);
        btn_info = findViewById(R.id.btn_info);
        login_trainer = findViewById(R.id.login_trainer);

        btn_edukasi.startAnimation(atg);
        btn_online.startAnimation(atg);
        btn_offline.startAnimation(atg2);
        btn_info.startAnimation(atg2);
        login_trainer.startAnimation(atgup);
    }

    public void perhitungan_offline(View view){
        startActivity(new Intent(getApplicationContext(),hitung_bmi_offline.class));
    }

    public void online(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
    public void info(View view) {
        startActivity(new Intent(getApplicationContext(), info.class));
    }

    public void pembelajaran(View view){
        //startActivity(new Intent(getApplicationContext(), news_app_offline.class));
        startActivity(new Intent(getApplicationContext(), ExpandableList.class));
    }

    public void Login_Trainer(View view) {
        startActivity(new Intent(getApplicationContext(), LoginTrainer.class));
    }
}
