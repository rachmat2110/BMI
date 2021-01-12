package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class hitung_bmi_offline extends AppCompatActivity {

    ImageView imgBMI;
    EditText textTB, textBB;
    TextView hasil_angka, hasil_huruf;
    Button hasil_bmi;

    Animation fadein, fadein2;

    DecimalFormat currency = new DecimalFormat("##,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadein2 = AnimationUtils.loadAnimation(this, R.anim.fadein2);

        setContentView(R.layout.activity_hitung_bmi_offline);
        textTB = findViewById(R.id.txtTB_bmi);
        textBB = findViewById(R.id.txtBB_bmi);
        hasil_angka = findViewById(R.id.txtHasil_angka);
        hasil_huruf = findViewById(R.id.txtHasil_huruf);
        hasil_bmi = findViewById(R.id.hitung_bmi);
        imgBMI = findViewById(R.id.imgBMI);

        imgBMI.startAnimation(fadein2);
        hasil_bmi.startAnimation(fadein2);
        hasil_angka.startAnimation(fadein2);


    }
    public void hasil(View view){
        super.onStart();
        Double nilai = Double.parseDouble(textBB.getText().toString());
        Double nilai2 = Double.parseDouble(textTB.getText().toString());
        Double tb_kuadrat = nilai2 / 100;
        Double hasil1 = ((nilai / (tb_kuadrat * tb_kuadrat)));
        hasil_angka.setText(currency.format(hasil1));

        if (hasil1 < 18.5){
            hasil_huruf.setText(" KEKURUSAN ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 < 22.9){
            hasil_huruf.setText(" NORMAL ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 < 24.9){
            hasil_huruf.setText(" GEMUK ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 < 29.9){
            hasil_huruf.setText(" OBESITAS I ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 > 30){
            hasil_huruf.setText(" OBESITAS II ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        }

    }
}
