package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class detail_post_offline extends AppCompatActivity {

    ImageView img;
    TextView title, description, volume_txt, pendahuluan_txt, metode_txt, hasil_txt, kesimpulan_txt;

    String data1, data2, volume, pendahuluan, metode, hasil, kesimpulan;
    int myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post_offline);

        volume_txt = findViewById(R.id.volume_txt);
        img = findViewById(R.id.img);
        title = findViewById(R.id.title_txt);
        description = findViewById(R.id.description_txt);
        pendahuluan_txt = findViewById(R.id.pendahuluan_txt);
        metode_txt = findViewById(R.id.metode_txt);
        hasil_txt = findViewById(R.id.hasil_txt);
        kesimpulan_txt = findViewById(R.id.kesimpulan_txt);

        getData();
        setData();
    }
    private  void getData(){
        if (getIntent().hasExtra("myImage") && getIntent().hasExtra("data1") && getIntent().hasExtra("data2") && getIntent().hasExtra("volume")
        && getIntent().hasExtra("pendahuluan") && getIntent().hasExtra("metode") && getIntent().hasExtra("hasil") && getIntent().hasExtra("kesimpulan") ){

            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            volume = getIntent().getStringExtra("volume");
            pendahuluan = getIntent().getStringExtra("pendahuluan");
            metode = getIntent().getStringExtra("metode");
            hasil = getIntent().getStringExtra("hasil");
            kesimpulan = getIntent().getStringExtra("kesimpulan");
            myImage = getIntent().getIntExtra("myImage", 1);

        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        title.setText(data1);
        description.setText(data2);
        volume_txt.setText(volume);
        pendahuluan_txt.setText(pendahuluan);
        metode_txt.setText(metode);
        hasil_txt.setText(hasil);
        kesimpulan_txt.setText(kesimpulan);
        img.setImageResource(myImage);
    }
}
