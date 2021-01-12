package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class DetailProses extends AppCompatActivity {

    EditText alasan_;
    TextView status_proses, tanggal_pemesan, nama_trainer, harga_trainer, nama_trainer_, no_hp, alamat_pengguna;
    ImageView rImageView;
    ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    public static String foto_pengguna = "";
    public static String user_ID = "";
    String userId, harga_trainer_,catatan_pengguna, foto_trainer, user_id, id_triner, id_trainer_, alasan, tanggal_memesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_proses);

        status_proses = findViewById(R.id.status_proses);
        tanggal_pemesan = findViewById(R.id.tanggal_pemesan);
        nama_trainer = findViewById(R.id.nama_trainer);
        harga_trainer = findViewById(R.id.harga_trainer);
        nama_trainer_ = findViewById(R.id.nama_trainer_);
        no_hp = findViewById(R.id.no_hp);
        alamat_pengguna = findViewById(R.id.alamat_pengguna);
        rImageView = findViewById(R.id.rImageView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        user_ID = getIntent().getStringExtra("userID");
        user_id = user_ID;

        DocumentReference documentReference_user_ = firebaseFirestore.collection("users").document(userId);
        documentReference_user_.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                foto_pengguna = snapshot.getString("foto");
            }
        });

        DocumentReference documentReference_user = firebaseFirestore.collection("users").document(userId).collection("proses").document(user_ID);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                status_proses.setText("Pesanan Sedang di "+documentSnapshot.getString("status"));
                tanggal_pemesan.setText(documentSnapshot.getString("tanggal_pemesan"));
                nama_trainer.setText(documentSnapshot.getString("nama_trainer"));
                harga_trainer.setText("Rp. "+documentSnapshot.getString("harga_trainer"));
                harga_trainer_ = documentSnapshot.getString("harga_trainer");
                nama_trainer_.setText(documentSnapshot.getString("nama_pengguna"));
                no_hp.setText(documentSnapshot.getString("no_hp"));
                alamat_pengguna.setText(documentSnapshot.getString("alamat_pengguna"));
                Picasso.get().load(documentSnapshot.getString("foto_trainer")).into(rImageView);

                catatan_pengguna = documentSnapshot.getString("catatan");
                foto_trainer = documentSnapshot.getString("foto_trainer");
                id_triner = documentSnapshot.getString("userID");
                id_trainer_ = documentSnapshot.getString("id_trainer");
                tanggal_memesan = documentSnapshot.getString("tanggal_pemesan");
            }
        });
    }

    public void pesanan_selesai(View view) {
        new SweetAlertDialog(DetailProses.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Ingin menyelesaikan pesanan ?")
                .setContentText("Jika sudah yakin maka pilih (Lanjut) agar dapat di proses")
                .setConfirmText("Lanjut")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        progressDialog = new ProgressDialog(DetailProses.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );
                        detail_sukses();
                        startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
                        finish();
                        progressDialog.dismiss();
                    }
                })
                .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }

    private void detail_sukses() {
        // Untuk Pengguna
        Map<String, Object> riwayat_pengguna = new HashMap<>();
        riwayat_pengguna.put("status", "Selesai");
        riwayat_pengguna.put("nama_trainer", nama_trainer.getText().toString());
        riwayat_pengguna.put("harga_trainer", harga_trainer_);
        riwayat_pengguna.put("nama_pengguna", nama_trainer_.getText().toString());
        riwayat_pengguna.put("no_hp", no_hp.getText().toString());
        riwayat_pengguna.put("alamat_pengguna", alamat_pengguna.getText().toString());
        riwayat_pengguna.put("catatan", catatan_pengguna);
        riwayat_pengguna.put("tanggal_pemesan", tanggal_pemesan.getText().toString());
        riwayat_pengguna.put("foto_trainer", foto_trainer);
        riwayat_pengguna.put("id_trainer", id_trainer_);
        riwayat_pengguna.put("userID", user_id);
        riwayat_pengguna.put("rating", "Menunggu diberikan Rating");
        riwayat_pengguna.put("komentar", "Menunggu Diberikan Komentar");
        riwayat_pengguna.put("ulasan", "Menunggu Diulas");

        firebaseFirestore.collection("users").document(userId)
                .collection("selesai").document(user_id)
                .set(riwayat_pengguna);

//        1. status
//        2. tanggal pemesan
//        3. nama pemesan
//        4. no hp pemesan
//        5. alamat pemesan

        // Untuk Trainer
        Map<String, Object> riwayat_trainer = new HashMap<>();
        riwayat_trainer.put("status", "Selesai");
        riwayat_trainer.put("tanggal_pemesan", tanggal_pemesan.getText().toString());
        riwayat_trainer.put("nama_pemesan", nama_trainer_.getText().toString());
        riwayat_trainer.put("no_hp", no_hp.getText().toString());
        riwayat_trainer.put("catatan", catatan_pengguna);
        riwayat_trainer.put("alamat_pengguna", alamat_pengguna.getText().toString());
        riwayat_trainer.put("foto_pengguna", foto_pengguna);
        riwayat_trainer.put("userID", user_id);
        riwayat_trainer.put("rating", "Menunggu diberikan Rating");
        riwayat_trainer.put("komentar", "Menunggu Diberikan Komentar");
        riwayat_trainer.put("ulasan", "Menunggu Diulas");

        firebaseFirestore.collection("trainer").document(id_trainer_)
                .collection("selesai").document(id_triner)
                .set(riwayat_trainer);

        delete_document_pengguna();
    }

    private void delete_document_pengguna() {
        firebaseFirestore.collection("users").document(userId)
                .collection("proses").document(user_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailProses.this, "sudah menghapus dokumen pengguna", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailProses.this, "error menghapus dokumen pengguna", Toast.LENGTH_SHORT).show();
                    }
                });

        firebaseFirestore.collection("trainer").document(id_trainer_)
                .collection("proses").document(id_triner)
                .delete()
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailProses.this, "berhasil menghapus dokumen trainer", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailProses.this, "error menghapus dokumen pengguna", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
        finish();
        super.onBackPressed();
    }

    public void back_riwayat(View view) {
        startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
        finish();
    }

    public void pesanan_batal(View view) {
        dialog = new AlertDialog.Builder(DetailProses.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.input_dialog_batal, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        final AlertDialog alertDialog = dialog.create();

        alasan_ = (EditText) dialogView.findViewById(R.id.alasan);
        final LinearLayout btn_batal = (LinearLayout) dialogView.findViewById(R.id.btn_batal);
        final LinearLayout btn_ok = (LinearLayout) dialogView.findViewById(R.id.btn_ok);

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "alasan pengguna adalah "+alasan_pengguna, Toast.LENGTH_SHORT).show();
                detail_gagal();
                alertDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
            }
        });

        alertDialog.show();
    }

    private void detail_gagal() {
        // Untuk Pengguna
        Map<String, Object> riwayat_pengguna = new HashMap<>();
        riwayat_pengguna.put("status", "Pesanan dibatalkan");
        riwayat_pengguna.put("nama_trainer", nama_trainer.getText().toString());
        riwayat_pengguna.put("harga_trainer", harga_trainer_);
        riwayat_pengguna.put("nama_pengguna", nama_trainer_.getText().toString());
        riwayat_pengguna.put("no_hp", no_hp.getText().toString());
        riwayat_pengguna.put("alamat_pengguna", alamat_pengguna.getText().toString());
        riwayat_pengguna.put("catatan", catatan_pengguna);
        riwayat_pengguna.put("tanggal_pemesan", tanggal_pemesan.getText().toString());
        riwayat_pengguna.put("foto_trainer", foto_trainer);
        riwayat_pengguna.put("id_trainer", id_trainer_);
        riwayat_pengguna.put("userID", user_id);
        riwayat_pengguna.put("rating", "0");
        riwayat_pengguna.put("komentar", alasan_.getText().toString());

        firebaseFirestore.collection("users").document(userId)
                .collection("gagal").document(user_id)
                .set(riwayat_pengguna);

//        1. status
//        2. tanggal pemesan
//        3. nama pemesan
//        4. no hp pemesan
//        5. alamat pemesan

        // Untuk Trainer
        Map<String, Object> riwayat_trainer = new HashMap<>();
        riwayat_trainer.put("status", "Pesanan dibatalkan");
        riwayat_trainer.put("tanggal_pemesan", tanggal_pemesan.getText().toString());
        riwayat_trainer.put("nama_pemesan", nama_trainer_.getText().toString());
        riwayat_trainer.put("no_hp", no_hp.getText().toString());
        riwayat_trainer.put("catatan", catatan_pengguna);
        riwayat_trainer.put("alamat_pengguna", alamat_pengguna.getText().toString());
        riwayat_trainer.put("foto_pengguna", foto_pengguna);
        riwayat_trainer.put("userID", user_id);
        riwayat_trainer.put("rating", "0");
        riwayat_trainer.put("komentar", alasan_.getText().toString());

        firebaseFirestore.collection("trainer").document(id_trainer_)
                .collection("gagal").document(id_triner)
                .set(riwayat_trainer);

        delete_document_pengguna();
    }

    public void chat(View view) {
        Chat.user_id = id_trainer_;
        Chat.keterangan = "dari pengguna";
        Chat.kembali = "dari proses";
        startActivity(new Intent(getApplicationContext(), Chat.class));
        finish();
    }
}
