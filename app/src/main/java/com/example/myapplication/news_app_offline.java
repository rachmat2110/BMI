package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class news_app_offline extends AppCompatActivity {

    RecyclerView recyclerView;

    String s1[], s2[], volume[], pendahuluan[], metode[], hasil[], kesimpulan[];
    int images[] = {R.drawable.riset, R.drawable.image_bmi};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_app_offline);

        s1 = getResources().getStringArray(R.array.edukasi);
        s2 = getResources().getStringArray(R.array.abstrak);
        volume = getResources().getStringArray(R.array.volume);
        metode = getResources().getStringArray(R.array.metode);
        hasil = getResources().getStringArray(R.array.hasil);
        kesimpulan = getResources().getStringArray(R.array.kesimpulan);
        pendahuluan = getResources().getStringArray(R.array.pendahuluan);

        recyclerView = findViewById(R.id.recyclerView);

        MyAdapter myAdapter = new MyAdapter(this, s1, s2, volume, pendahuluan, metode, hasil, kesimpulan, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
