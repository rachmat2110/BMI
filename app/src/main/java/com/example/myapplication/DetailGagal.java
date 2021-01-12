package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class DetailGagal extends AppCompatActivity {

    TextView status_pemesanan, komentar_pemesan, tanggal_pemesan, nama_trainer, harga_trainer, nama_pemesan, no_hp_pemesan, alamat_pemesan;
    ImageView rImageView;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gagal);

        status_pemesanan = findViewById(R.id.status_pemesanan);
        komentar_pemesan = findViewById(R.id.komentar_pemesan);
        tanggal_pemesan = findViewById(R.id.tanggal_pemesan);
        nama_trainer = findViewById(R.id.nama_trainer);
        harga_trainer = findViewById(R.id.harga_trainer);
        nama_pemesan = findViewById(R.id.nama_pemesan);
        no_hp_pemesan = findViewById(R.id.no_hp_pemesan);
        alamat_pemesan = findViewById(R.id.alamat_pemesan);
        rImageView = findViewById(R.id.rImageView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        String user_ID = getIntent().getStringExtra("userID");

        final DocumentReference documentReference_user = fStore.collection("users").document(userId).collection("gagal").document(user_ID);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                status_pemesanan.setText("Pesanan "+documentSnapshot.getString("status"));
                tanggal_pemesan.setText(documentSnapshot.getString("tanggal_pemesan"));
                nama_trainer.setText(documentSnapshot.getString("nama_trainer"));
                no_hp_pemesan.setText(documentSnapshot.getString("no_hp"));
                alamat_pemesan.setText(documentSnapshot.getString("alamat_pengguna"));
                nama_pemesan.setText(documentSnapshot.getString("nama_pengguna"));
                harga_trainer.setText("Rp. "+documentSnapshot.getString("harga_trainer"));
                Picasso.get().load(documentSnapshot.getString("foto_trainer")).into(rImageView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
        finish();
        super.onBackPressed();
    }

    public void pesan_lagi(View view) {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}
