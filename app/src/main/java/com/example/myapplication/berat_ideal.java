package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class berat_ideal extends AppCompatActivity {

    TextView berat_ideal, bmr_anda, keperluan_air, keperluan_kalori;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    Animation atg, atg2;

    LinearLayout card_berat_ideal_anda, card_bmr_anda, keperluan_air_anda, card_keperluan_kalori_anda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berat_ideal);

        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atg2 = AnimationUtils.loadAnimation(this, R.anim.atg2);

        card_berat_ideal_anda = findViewById(R.id.card_berat_ideal_anda);
        card_bmr_anda = findViewById(R.id.card_bmr_anda);
        keperluan_air_anda = findViewById(R.id.keperluan_air_anda);
        card_keperluan_kalori_anda = findViewById(R.id.card_keperluan_kalori_anda);
        berat_ideal = findViewById(R.id.berat_ideal);
        bmr_anda = findViewById(R.id.bmr_anda);
        keperluan_air = findViewById(R.id.keperluan_air);
        keperluan_kalori = findViewById(R.id.keperluan_kalori);

        card_berat_ideal_anda.startAnimation(atg);
        card_bmr_anda.startAnimation(atg2);
        keperluan_air_anda.startAnimation(atg);
        card_keperluan_kalori_anda.startAnimation(atg2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                berat_ideal.setText(documentSnapshot.getString("berat_ideal"));
                bmr_anda.setText(documentSnapshot.getString("BMR"));
                keperluan_air.setText(documentSnapshot.getString("keperluan_air"));
                keperluan_kalori.setText(documentSnapshot.getString("keperluan_kalori"));

            }
        });

    }

    public void info_beratideal(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Berat Ideal Anda");

        alertDialogBuilder
                .setMessage("Berat ideal Anda diambil berdasarkan perhitungan yang telah dilakukan" +
                        ". Berat ideal ini merupakan berat yang seharusnya anda raih. Berat ideal menunjukkan bahwa tubuh dalam keadaan sehat")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)


                .setNegativeButton("Oke Paham",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void info_ketbmr(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Pengertian BMR");

        alertDialogBuilder
                .setMessage("Basal Metabolic Rate (BMR) atau Angka Metabolisme Basal (AMB) adalah " +
                        "kebutuhan minimal energi untuk melakukan proses kegiatan sehari-hari. ")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)


                .setNegativeButton("Oke Paham",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void info_ketair(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Keperluan air");

        alertDialogBuilder
                .setMessage("Keterangan air disini merupakan kadar mineral yang minimal dicukupi oleh pengguna," +
                        " air mineral ini berguna untuk menopang kegiatan sehari-hari" +
                        ". Apabila kekurangan cairan / mineral, nanti akan terjadi dehidrasi pada tubuh")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)


                .setNegativeButton("Oke Paham",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void info_ketkalori(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Keperluan kalori");

        alertDialogBuilder
                .setMessage("Kalori adalah energi yang dibutuhkan tubuh agar bisa beraktivitas dan menjalankan fungsinya dengan baik.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)


                .setNegativeButton("Oke Paham",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
