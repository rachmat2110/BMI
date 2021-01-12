package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class DetailGagalTrainer extends AppCompatActivity {

    public static String id_trainer = "";

    TextView status_pemesanan, komentar_pemesan, tanggal_pemesan, nama_pemesan, nama_pemesan_, no_hp_pemesan, alamat_pemesan;
    ImageView rImageView;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gagal_trainer);

        status_pemesanan = findViewById(R.id.status_pemesanan);
        komentar_pemesan = findViewById(R.id.komentar_pemesan);
        tanggal_pemesan = findViewById(R.id.tanggal_pemesan);
        nama_pemesan = findViewById(R.id.nama_pemesan);
        nama_pemesan_ = findViewById(R.id.nama_pemesan_);
        no_hp_pemesan = findViewById(R.id.no_hp_pemesan);
        alamat_pemesan = findViewById(R.id.alamat_pemesan);
        rImageView = findViewById(R.id.rImageView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        String user_ID = getIntent().getStringExtra("userID");

        final DocumentReference documentReference_user = firebaseFirestore.collection("trainer").document(id_trainer).collection("gagal").document(user_ID);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                status_pemesanan.setText("Pesanan "+documentSnapshot.getString("status"));
                komentar_pemesan.setText(documentSnapshot.getString("komentar"));
                tanggal_pemesan.setText(documentSnapshot.getString("tanggal_pemesan"));
                nama_pemesan.setText(documentSnapshot.getString("nama_pemesan"));
                nama_pemesan_.setText(documentSnapshot.getString("nama_pemesan"));
                no_hp_pemesan.setText(documentSnapshot.getString("no_hp"));
                alamat_pemesan.setText(documentSnapshot.getString("alamat_pengguna"));
                Picasso.get().load(documentSnapshot.getString("foto_pengguna")).into(rImageView);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), RiwayatTrainer.class));
        finish();
        super.onBackPressed();
    }
}
