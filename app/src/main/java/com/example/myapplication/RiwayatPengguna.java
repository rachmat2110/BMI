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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Models.list_riwayat_pengguna;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class RiwayatPengguna extends AppCompatActivity {

    private static final String TAG = "TAG";
    TextView riwayat_gagal, riwayat_proses, riwayat_sukses;
    LinearLayout proses;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    private RecyclerView list_riwayat_pengguna;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pengguna);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        riwayat_gagal = findViewById(R.id.riwayat_gagal);
        riwayat_proses = findViewById(R.id.riwayat_proses);
        riwayat_sukses = findViewById(R.id.riwayat_sukses);
        list_riwayat_pengguna = findViewById(R.id.list_riwayat_pengguna);
        //proses = findViewById(R.id.proses);

        // untuk layout halaman proses
        riwayat_gagal.setBackgroundResource(R.drawable.gagal);
        riwayat_proses.setBackgroundResource(R.drawable.proses_klik);
        riwayat_sukses.setBackgroundResource(R.drawable.sukses);

        LoadProses();
        onStop();
        onStart();

        // untuk layout halaman gagal
        riwayat_gagal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riwayat_gagal.setBackgroundResource(R.drawable.gagal_klik);
                riwayat_proses.setBackgroundResource(R.drawable.proses);
                riwayat_sukses.setBackgroundResource(R.drawable.sukses);

                LoadGagal();
                onStop();
                onStart();

            }
        });

        // untuk layout halaman proses
        riwayat_proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riwayat_gagal.setBackgroundResource(R.drawable.gagal);
                riwayat_proses.setBackgroundResource(R.drawable.proses_klik);
                riwayat_sukses.setBackgroundResource(R.drawable.sukses);

                LoadProses();
                onStop();
                onStart();

            }
        });

        // untuk layout halaman sukses
        riwayat_sukses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riwayat_gagal.setBackgroundResource(R.drawable.gagal);
                riwayat_proses.setBackgroundResource(R.drawable.proses);
                riwayat_sukses.setBackgroundResource(R.drawable.sukses_klik);

                LoadSukses();
                onStop();
                onStart();

            }
        });
    }

    private static class Holder extends RecyclerView.ViewHolder {

        View mView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setDetails(Context ctx, String nama_trainer, String foto_trainer, String status, String tanggal_pemesan, String ulasan){
            TextView nama_trainer_ = mView.findViewById(R.id.nama_trainer_riwayat);
            TextView status_ = mView.findViewById(R.id.proses_riwayat);
            ImageView foto_trainer_ = mView.findViewById(R.id.foto_trainer_riwayat);
            TextView tanggal_pemesanan_ = mView.findViewById(R.id.tanggal_riwayat);
            TextView ulasan_ = mView.findViewById(R.id.ulasan);

            nama_trainer_.setText(nama_trainer);
            Picasso.get().load(foto_trainer).into(foto_trainer_);
            status_.setText(status);
            tanggal_pemesanan_.setText(tanggal_pemesan);
            ulasan_.setText(ulasan);

        }

    }

    private void LoadSukses() {
        Query query =  fStore.collection("users").document(userId).collection("selesai");
        FirestoreRecyclerOptions<list_riwayat_pengguna> options = new  FirestoreRecyclerOptions.Builder<list_riwayat_pengguna>()
                .setQuery(query, list_riwayat_pengguna.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<list_riwayat_pengguna, RiwayatPengguna.Holder >(options) {

            @NonNull
            @Override
            public RiwayatPengguna.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
                TextView status_ = view.findViewById(R.id.proses_riwayat);
                status_.setTextColor(0xFF1BA904);
                return new RiwayatPengguna.Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final com.example.myapplication.Models.list_riwayat_pengguna model) {
                holder.setDetails(getApplicationContext(), model.getNama_trainer(), model.getFoto_trainer(), model.getStatus(), model.getTanggal_pemesan(), model.getUlasan());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailSukses.class);
                        intent.putExtra("userID", model.getUserID());
                        v.getContext().startActivity(intent);
                    }
                });
            }

        };
        list_riwayat_pengguna.setHasFixedSize(true);
        list_riwayat_pengguna.setLayoutManager(new LinearLayoutManager(this));
        list_riwayat_pengguna.setAdapter(adapter);
    }

    private void LoadGagal() {
        Query query =  fStore.collection("users").document(userId).collection("gagal");
        FirestoreRecyclerOptions<list_riwayat_pengguna> options = new  FirestoreRecyclerOptions.Builder<list_riwayat_pengguna>()
                .setQuery(query, list_riwayat_pengguna.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<list_riwayat_pengguna, RiwayatPengguna.Holder >(options) {

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final com.example.myapplication.Models.list_riwayat_pengguna model) {
                holder.setDetails(getApplicationContext(), model.getNama_trainer(), model.getFoto_trainer(), model.getStatus(), model.getTanggal_pemesan(), model.getUlasan());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailGagal.class);
                        intent.putExtra("userID", model.getUserID());
                        v.getContext().startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public RiwayatPengguna.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
                TextView status_ = view.findViewById(R.id.proses_riwayat);
                status_.setTextColor(0xFFE72020);
                return new RiwayatPengguna.Holder(view);
            }

        };
        list_riwayat_pengguna.setHasFixedSize(true);
        list_riwayat_pengguna.setLayoutManager(new LinearLayoutManager(this));
        list_riwayat_pengguna.setAdapter(adapter);
    }

    private void LoadProses() {
        Query query =  fStore.collection("users").document(userId).collection("proses");
        FirestoreRecyclerOptions< com.example.myapplication.Models.list_riwayat_pengguna > options = new  FirestoreRecyclerOptions.Builder<list_riwayat_pengguna>()
                .setQuery(query, list_riwayat_pengguna.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<list_riwayat_pengguna, RiwayatPengguna.Holder >(options) {

            @NonNull
            @Override
            public RiwayatPengguna.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
                return new RiwayatPengguna.Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final com.example.myapplication.Models.list_riwayat_pengguna model) {
                holder.setDetails(getApplicationContext(), model.getNama_trainer(), model.getFoto_trainer(), model.getStatus(), model.getTanggal_pemesan(), model.getUlasan());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailProses.class);
                        intent.putExtra("userID", model.getUserID());
                        v.getContext().startActivity(intent);
                    }
                });
            }

        };
        list_riwayat_pengguna.setHasFixedSize(true);
        list_riwayat_pengguna.setLayoutManager(new LinearLayoutManager(this));
        list_riwayat_pengguna.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
