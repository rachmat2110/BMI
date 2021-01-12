package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    String data1[], data2[], volumes[], pendahuluans[], metodes[], hasils[], kesimpulans[];
    int images[];
    Context context;

    public MyAdapter(Context ct, String s1[], String s2[], String volume[], String pendahuluan[], String metode[], String hasil[],
            String kesimpulan[], int img[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
        pendahuluans = pendahuluan;
        metodes = metode;
        hasils = hasil;
        kesimpulans = kesimpulan;
        volumes = volume;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.judulText.setText(data1[position]);
        holder.rDescriptionTv.setText(data2[position]);
        holder.imgView.setImageResource(images[position]);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, detail_post_offline.class);
                intent.putExtra("pendahuluan", pendahuluans[position]);
                intent.putExtra("data1", data1[position]);
                intent.putExtra("data2", data2[position]);
                intent.putExtra("volume", volumes[position]);
                intent.putExtra("metode", metodes[position]);
                intent.putExtra("hasil", hasils[position]);
                intent.putExtra("kesimpulan", kesimpulans[position]);
                intent.putExtra("myImage", images[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView judulText, descText, rDescriptionTv;
        ImageView imgView;
        CardView mainLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            rDescriptionTv = itemView.findViewById(R.id.rDescriptionTv);
            judulText = itemView.findViewById(R.id.edukasi_txt);
            //descText = itemView.findViewById(R.id.description_txt);
            imgView = itemView.findViewById(R.id.imgView);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
