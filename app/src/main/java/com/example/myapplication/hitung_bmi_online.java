package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class hitung_bmi_online extends AppCompatActivity {

    public static final String TAG = "TAG";

    RadioGroup radioGroup;
    RadioButton radioButton;

    ImageView imgBMI;
    EditText textTB, textBB;
    TextView hasil_angka, hasil_huruf, nama_user, txtDate;
    Button hasil_bmi, simpan_bmi;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String user_umur;
    String bmr_hasil;
    String user_BMR;

    ProgressDialog progressDialog;

    Animation fadein, fadein2;

    DecimalFormat currency = new DecimalFormat("##,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitung_bmi_online);

        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadein2 = AnimationUtils.loadAnimation(this, R.anim.fadein2);

        textTB = findViewById(R.id.txtTB_bmi);
        textBB = findViewById(R.id.txtBB_bmi);
        hasil_angka = findViewById(R.id.txtHasil_angka);
        hasil_huruf = findViewById(R.id.txtHasil_huruf);
        hasil_bmi = findViewById(R.id.hitung_bmi);
        nama_user = findViewById(R.id.nama);
        simpan_bmi = findViewById(R.id.simpan_bmi);
        txtDate = findViewById(R.id.txtTanggal);
        imgBMI = findViewById(R.id.imgBMI);
        radioGroup = findViewById(R.id.radioGroup);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        imgBMI.startAnimation(fadein2);
        simpan_bmi.startAnimation(fadein2);
        hasil_bmi.startAnimation(fadein2);
        hasil_angka.startAnimation(fadein2);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                nama_user.setText(documentSnapshot.getString("fName"));
                user_umur = documentSnapshot.getString("Umur");
            }
        });

        simpan_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tinggi_badan = textTB.getText().toString();
                String berat_badan = textBB.getText().toString();
                String hasil_hitung = hasil_angka.getText().toString();
                String status = hasil_huruf.getText().toString().trim();

                progressDialog = new ProgressDialog(hitung_bmi_online.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                updateberat_ideal();
                updatedata_bmr();

                updateData(status, tinggi_badan, berat_badan, hasil_hitung);
            }
        });

    }
    private void updateberat_ideal(){
        fStore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                        if ((task.isSuccessful())) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.getData().get("Kelamin").equals("Pria")){
                                Double nilai2 = Double.parseDouble(textTB.getText().toString());
                                Double hasil = (nilai2 - 100) - (0.10 * (nilai2 - 100));

                                String berat_ideal_hasil = String.valueOf(Math.ceil(hasil));

                                fStore.collection("users").document(userId)
                                        .update("berat_ideal", berat_ideal_hasil)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else {
                                Double nilai2 = Double.parseDouble(textTB.getText().toString());
                                Double hasil = (nilai2 - 100) - (0.15 * (nilai2 - 100));

                                String berat_ideal_hasil = String.valueOf(Math.ceil(hasil));

                                fStore.collection("users").document(userId)
                                        .update("berat_ideal", berat_ideal_hasil)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }else {
                            Log.d(TAG, "Error getsting documents : ", task.getException());
                        }
                    }
                });

    }

    private void updatedata_bmr(){
        fStore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                        if ((task.isSuccessful())){
                            DocumentSnapshot documentSnapshot = task.getResult();

                            Double umur = Double.parseDouble(user_umur);

                            if (documentSnapshot.getData().get("Kelamin").equals("Pria")){
                                Double nilai = Double.parseDouble(textBB.getText().toString());
                                Double nilai2 = Double.parseDouble(textTB.getText().toString());

                                Double keperluan_air = (nilai * 30) / 1000;
                                Double hasil_bmr = 66 + (13.7 * nilai) + (5 * nilai2) - (6.8 * umur);

                                bmr_hasil = String.valueOf(hasil_bmr);
                                String hasil_keperluan_air = String.valueOf(keperluan_air);
                                fStore.collection("users").document(userId)
                                        .update("BMR", bmr_hasil, "keperluan_air", hasil_keperluan_air)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else{
                                Double nilai = Double.parseDouble(textBB.getText().toString());
                                Double nilai2 = Double.parseDouble(textTB.getText().toString());

                                Double keperluan_air = (nilai * 30) / 1000;
                                Double hasil_bmr = 655 + (9.6 * nilai) + (1.8 * nilai2) - (4.7 * umur);

                                String bmr_hasil = String.valueOf(hasil_bmr);
                                String hasil_keperluan_air = String.valueOf(keperluan_air);
                                fStore.collection("users").document(userId)
                                        .update("BMR", bmr_hasil, "keperluan_air",hasil_keperluan_air)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            updatekeperluan_kalori();
                        }else{
                            Log.d(TAG, "Error getsting documents : ", task.getException());
                        }
                    }
                });
    }

    private void updatekeperluan_kalori(){

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                user_BMR = documentSnapshot.getString("BMR");
            }
        });

        fStore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                        if ((task.isSuccessful())){

                            Double BMR = Double.parseDouble(user_BMR);

                            int radioId = radioGroup.getCheckedRadioButtonId();

                            radioButton = findViewById(radioId);
                            final String aktifitas = radioButton.getText().toString();

                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.getData().get("Kelamin").equals("Pria")){
                                if (aktifitas.equals("(RINGAN) 75% duduk atau berdiri, 25% berdiri atau bergerak")){
                                    //3060.72
                                    Double kalori = 1.56 * BMR;
                                    String kalori_hasil = String.valueOf(kalori);
                                    fStore.collection("users").document(userId)
                                            .update("keperluan_kalori", kalori_hasil)
                                            .addOnCompleteListener(new OnCompleteListener< Void >() {
                                                @Override
                                                public void onComplete(@NonNull Task< Void > task) {
                                                    Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else if (aktifitas.equals("(SEDANG) 25% duduk atau berdiri, 75% Beraktivitas")){
                                    //3389.76
                                    Double kalori = 1.76 * BMR;
                                    String kalori_hasil = String.valueOf(kalori);
                                    fStore.collection("users").document(userId)
                                            .update("keperluan_kalori", kalori_hasil)
                                            .addOnCompleteListener(new OnCompleteListener< Void >() {
                                                @Override
                                                public void onComplete(@NonNull Task< Void > task) {
                                                    Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else{
                                    Double kalori = 2.10 * BMR;
                                    String kalori_hasil = String.valueOf(kalori);
                                    fStore.collection("users").document(userId)
                                            .update("keperluan_kalori", kalori_hasil)
                                            .addOnCompleteListener(new OnCompleteListener< Void >() {
                                                @Override
                                                public void onComplete(@NonNull Task< Void > task) {
                                                    Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }else{
                                if (aktifitas.equals("(RINGAN) 75% duduk atau berdiri, 25% berdiri atau bergerak")){
                                    Double kalori = 1.55 * BMR;
                                    String kalori_hasil = String.valueOf(kalori);
                                    fStore.collection("users").document(userId)
                                            .update("keperluan_kalori", kalori_hasil)
                                            .addOnCompleteListener(new OnCompleteListener< Void >() {
                                                @Override
                                                public void onComplete(@NonNull Task< Void > task) {
                                                    Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else if (aktifitas.equals("(SEDANG) 25% duduk atau berdiri, 75% Beraktivitas")){
                                    Double kalori = 1.70 * BMR;
                                    String kalori_hasil = String.valueOf(kalori);
                                    fStore.collection("users").document(userId)
                                            .update("keperluan_kalori", kalori_hasil)
                                            .addOnCompleteListener(new OnCompleteListener< Void >() {
                                                @Override
                                                public void onComplete(@NonNull Task< Void > task) {
                                                    Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else{
                                    Double kalori = 2.00 * BMR;
                                    String kalori_hasil = String.valueOf(kalori);
                                    fStore.collection("users").document(userId)
                                            .update("keperluan_kalori", kalori_hasil)
                                            .addOnCompleteListener(new OnCompleteListener< Void >() {
                                                @Override
                                                public void onComplete(@NonNull Task< Void > task) {
                                                    Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        }else{
                            Log.d(TAG, "Error getsting documents : ", task.getException());
                        }
                    }
                });
    }

    public void checkButton (View view){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        Toast.makeText(this,"Selected Radio Button: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    private void updateData(String status, String tinggi_badan, String berat_badan, String hasil_hitung ) {
        fStore.collection("users").document(userId)
                .update("status", status, "Tinggi_Badan", tinggi_badan, "Berat_Badan", berat_badan, "BMI", hasil_hitung)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(hitung_bmi_online.this, "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),profil_user.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(hitung_bmi_online.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String tanggal = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        //String tanggal = txtDate.getText().toString().trim();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Tanggal", date+", "+tanggal);
        userMap.put("Status", status);
        userMap.put("BMI", hasil_hitung);
        fStore.collection("users").document(userId)
                .collection("History").add(userMap);
    }

    public void hasil(View view){
        super.onStart();
        Double nilai = Double.parseDouble(textBB.getText().toString());
        Double nilai2 = Double.parseDouble(textTB.getText().toString());
        Double tb_kuadrat = nilai2 / 100;
        Double hasil1 = ((nilai / (tb_kuadrat * tb_kuadrat)));
        hasil_angka.setText(currency.format(hasil1));

        if (hasil1 < 18.5){
            hasil_huruf.setText(" KEKURUSAN ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 < 22.9){
            hasil_huruf.setText(" NORMAL ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 < 24.9){
            hasil_huruf.setText(" GEMUK ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 < 29.9){
            hasil_huruf.setText(" OBESITAS I ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        } else if (hasil1 > 30){
            hasil_huruf.setText(" OBESITAS II ");
            hasil_huruf.startAnimation(fadein);
            hasil_angka.startAnimation(fadein);
        }

    }

}
