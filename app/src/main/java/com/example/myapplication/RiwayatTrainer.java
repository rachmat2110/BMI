package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.list_riwayat_trainer;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class RiwayatTrainer extends AppCompatActivity {

    public static String id_trainer = "";

    FirebaseFirestore fStore;

    TextView riwayat_gagal, riwayat_sukses;
    private RecyclerView list_riwayat_trainer;
    private FirestoreRecyclerAdapter adapter_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_trainer);

        fStore = FirebaseFirestore.getInstance();

        riwayat_gagal = findViewById(R.id.riwayat_gagal);
        riwayat_sukses = findViewById(R.id.riwayat_sukses);
        list_riwayat_trainer = findViewById(R.id.list_riwayat_trainer);

        // untuk layout halaman gagal
        riwayat_gagal.setBackgroundResource(R.drawable.gagal_klik);
        riwayat_sukses.setBackgroundResource(R.drawable.sukses);

        LoadGagal();
        onStop();
        onStart();

        // untuk layout halaman gagal
        riwayat_gagal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riwayat_gagal.setBackgroundResource(R.drawable.gagal_klik);
                riwayat_sukses.setBackgroundResource(R.drawable.sukses);

                LoadGagal();
                onStop();
                onStart();
            }
        });

        // untuk layout halaman sukses
        riwayat_sukses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riwayat_gagal.setBackgroundResource(R.drawable.gagal);
                riwayat_sukses.setBackgroundResource(R.drawable.sukses_klik);

                LoadSelesai();
                onStop();
                onStart();
            }
        });
    }

    public class Holder extends RecyclerView.ViewHolder {

        View mView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDetails(Context ctx, String nama_pemesan, String foto_pengguna, String no_hp, String tanggal_pemesan, String userID){
            TextView nama_trainer_ = mView.findViewById(R.id.nama_pengguna);
//            TextView status_ = mView.findViewById(R.id.hp_pengguna);
            ImageView foto_trainer_ = mView.findViewById(R.id.foto_pengguna);
            TextView tanggal_pemesanan_ = mView.findViewById(R.id.tanggal_pengguna);

            nama_trainer_.setText(nama_pemesan);
            Picasso.get().load(foto_pengguna).into(foto_trainer_);
//            status_.setText(no_hp);
//            status_.setTextColor(0xFF1BA904);
            tanggal_pemesanan_.setText(tanggal_pemesan);
            String user = userID;

        }
    }

    private void LoadSelesai() {
        Query query =  fStore.collection("trainer").document(id_trainer).collection("selesai");
        FirestoreRecyclerOptions< com.example.myapplication.Models.list_riwayat_trainer > options = new  FirestoreRecyclerOptions.Builder<list_riwayat_trainer>()
                .setQuery(query, list_riwayat_trainer.class)
                .build();

        adapter_1 = new FirestoreRecyclerAdapter<list_riwayat_trainer, RiwayatTrainer.Holder>(options) {

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses_trainer, parent, false);
                TextView status_ = view.findViewById(R.id.hp_pengguna);
                status_.setText("Sukses");
                status_.setTextColor(0xFF1BA904);
                return new RiwayatTrainer.Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final com.example.myapplication.Models.list_riwayat_trainer model) {
                holder.setDetails(getApplicationContext(), model.getNama_pemesan(), model.getFoto_pengguna(), model.getNo_hp(), model.getTanggal_pemesan(), model.getUserID());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailSuksesTrainer.class);
                        intent.putExtra("userID", model.getUserID());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };
        list_riwayat_trainer.setHasFixedSize(true);
        list_riwayat_trainer.setLayoutManager(new LinearLayoutManager(this));
        list_riwayat_trainer.setAdapter(adapter_1);
    }

    private void LoadGagal() {
        Query query =  fStore.collection("trainer").document(id_trainer).collection("gagal");
        FirestoreRecyclerOptions<list_riwayat_trainer> options = new  FirestoreRecyclerOptions.Builder<list_riwayat_trainer>()
                .setQuery(query, list_riwayat_trainer.class)
                .build();

        adapter_1 = new FirestoreRecyclerAdapter<list_riwayat_trainer, RiwayatTrainer.Holder>(options) {

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses_trainer, parent, false);
                TextView status_ = view.findViewById(R.id.hp_pengguna);
                status_.setText("Gagal");
                status_.setTextColor(0xFFE72020);
                return new RiwayatTrainer.Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final com.example.myapplication.Models.list_riwayat_trainer model) {
                holder.setDetails(getApplicationContext(), model.getNama_pemesan(), model.getFoto_pengguna(), model.getNo_hp(), model.getTanggal_pemesan(), model.getUserID());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailGagalTrainer.class);
                        intent.putExtra("userID", model.getUserID());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };
        list_riwayat_trainer.setHasFixedSize(true);
        list_riwayat_trainer.setLayoutManager(new LinearLayoutManager(this));
        list_riwayat_trainer.setAdapter(adapter_1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_1.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter_1.startListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeTrainer.class));
        finish();
        super.onBackPressed();
    }
}
