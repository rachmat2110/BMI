package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class DetailSuksesTrainer extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    String userId;

    TextView status_pesanan_trainer, komentar, rating, tanggal_pemesan, nama_pemesan, nama_pemesan_, hp_pengguna, alamat_pengguna;
    ImageView rImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sukses_trainer);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        status_pesanan_trainer = findViewById(R.id.status_pesanan_trainer);
        komentar = findViewById(R.id.komentar);
        rating = findViewById(R.id.rating);
        tanggal_pemesan = findViewById(R.id.tanggal_pemesan);
        nama_pemesan = findViewById(R.id.nama_pemesan);
        nama_pemesan_ = findViewById(R.id.nama_pemesan_);
        hp_pengguna = findViewById(R.id.hp_pengguna);
        alamat_pengguna = findViewById(R.id.alamat_pengguna);
        rImageView = findViewById(R.id.rImageView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        String user_ID = getIntent().getStringExtra("userID");

        final DocumentReference documentReference_user = firebaseFirestore.collection("trainer").document(userId).collection("selesai").document(user_ID);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                status_pesanan_trainer.setText("Pesanan "+documentSnapshot.getString("status"));
                komentar.setText(documentSnapshot.getString("komentar"));
                rating.setText(documentSnapshot.getString("rating"));
                tanggal_pemesan.setText(documentSnapshot.getString("tanggal_pemesan"));
                nama_pemesan.setText(documentSnapshot.getString("nama_pemesan"));
                nama_pemesan_.setText(documentSnapshot.getString("nama_pemesan"));
                hp_pengguna.setText(documentSnapshot.getString("no_hp"));
                alamat_pengguna.setText(documentSnapshot.getString("alamat_pengguna"));
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
