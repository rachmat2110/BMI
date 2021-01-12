package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class Komentar extends AppCompatActivity {

    public static String id_trainer = "";

    RatingBar ratingbar;
    EditText komentar;
    TextView nama_trainer, text;
    ImageView rImageView;

    FirebaseAuth fAuth;
    String userId;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentar);

        ratingbar = findViewById(R.id.ratingbar);
        komentar = findViewById(R.id.komentar);
        nama_trainer = findViewById(R.id.nama_trainer);
        text = findViewById(R.id.text);
        rImageView = findViewById(R.id.rImageView);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference_user_ = fStore.collection("users").document(userId).collection("selesai").document(DetailSukses.documentID);
        documentReference_user_.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                nama_trainer.setText(documentSnapshot.getString("nama_trainer"));
                Picasso.get().load(documentSnapshot.getString("foto_trainer")).into(rImageView);
                text.setText("Hai "+documentSnapshot.getString("nama_pengguna")+", suka dengan trainer ini? Yuk berikan komentar anda :)");
            }
        });

        final DocumentReference documentReference_user = fStore.collection("trainer").document(DetailSukses.ID_TRAINER).collection("selesai").document(DetailSukses.documentID);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.getString("ulasan").equals("")){
                    new SweetAlertDialog(Komentar.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("OOPS!")
                            .setContentText("Anda sudah memberikan komentar disini :)")
                            .setConfirmText("OKE")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
                                    finish();
                                }
                            }).show();
                }else{

                }
            }
        });
    }

    public void ulasan(View view) {
        if (komentar.getText().toString().equals("")){
            Toast.makeText(this, "Harap isi terlebih dahulu komentar", Toast.LENGTH_SHORT).show();
        }else if(ratingbar.getRating() < 1){
            Toast.makeText(this, "Harap isi terlebih dahulu ratingnya", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog = new ProgressDialog(Komentar.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            fStore.collection("trainer").document(DetailSukses.ID_TRAINER).collection("selesai").document(DetailSukses.documentID)
                    .update("komentar", komentar.getText().toString(), "rating", String.valueOf(ratingbar.getRating()), "ulasan","")
                    .addOnCompleteListener(new OnCompleteListener< Void >() {
                        @Override
                        public void onComplete(@NonNull Task< Void > task) {
                            Toast.makeText(Komentar.this, "Rating dan komentar berhasil diupload", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),RiwayatPengguna.class));
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Komentar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            fStore.collection("users").document(userId).collection("selesai").document(DetailSukses.documentID)
                    .update("komentar", komentar.getText().toString(), "rating", String.valueOf(ratingbar.getRating()), "ulasan","")
                    .addOnCompleteListener(new OnCompleteListener< Void >() {
                        @Override
                        public void onComplete(@NonNull Task< Void > task) {
                            Toast.makeText(Komentar.this, "Rating dan komentar berhasil diupload", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),RiwayatPengguna.class));
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Komentar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
