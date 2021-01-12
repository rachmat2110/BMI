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

public class DetailSukses extends AppCompatActivity {

    TextView riwayat_sukses, tanggal_pemesan, nama_trainer, nama_pemesan, hp_pengguna, alamat_pengguna, harga_trainer;
    ImageView rImageView;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, id_trainer;
    public static String documentID = "";
    public static String ID_TRAINER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sukses);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        riwayat_sukses = findViewById(R.id.riwayat_sukses);
        tanggal_pemesan = findViewById(R.id.tanggal_pemesan);
        nama_trainer = findViewById(R.id.nama_trainer);
        nama_pemesan = findViewById(R.id.nama_pemesan);
        hp_pengguna = findViewById(R.id.hp_pengguna);
        alamat_pengguna = findViewById(R.id.alamat_pengguna);
        harga_trainer = findViewById(R.id.harga_trainer);
        rImageView = findViewById(R.id.rImageView);

        String user_ID = getIntent().getStringExtra("userID");

        final DocumentReference documentReference_user = fStore.collection("users").document(userId).collection("selesai").document(user_ID);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                riwayat_sukses.setText("Pesanan "+documentSnapshot.getString("status"));
                tanggal_pemesan.setText(documentSnapshot.getString("tanggal_pemesan"));
                nama_trainer.setText(documentSnapshot.getString("nama_trainer"));
                hp_pengguna.setText(documentSnapshot.getString("no_hp"));
                alamat_pengguna.setText(documentSnapshot.getString("alamat_pengguna"));
                nama_pemesan.setText(documentSnapshot.getString("nama_pengguna"));
                harga_trainer.setText("Rp. "+documentSnapshot.getString("harga_trainer"));
                Picasso.get().load(documentSnapshot.getString("foto_trainer")).into(rImageView);

                id_trainer = documentSnapshot.getString("id_trainer");
                ID_TRAINER = documentSnapshot.getString("id_trainer");
                documentID = documentSnapshot.getString("userID");
            }
        });
    }

    public void komentar(View view) {
        startActivity(new Intent(getApplicationContext(), Komentar.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
        finish();
        super.onBackPressed();
    }
}
