package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.essam.simpleplacepicker.MapActivity;
import com.essam.simpleplacepicker.utils.SimplePlacePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailTrainer extends AppCompatActivity {

    public static final String TAG = "TAG";
    public static String userID = "";

    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;

    ImageView foto_trainer;
    TextView pesan_trainer,nama_trainer,ulasan,deskripsi_trainer,harga_trainer,lokasi_pemesan, tanggal_pemesan, alamat_manual;
    EditText catatan_alamat;
    String userId, foto_trainer_, id_trainer, lokasi, latitude, longtitude, nama_pengguna, hp_pengguna, foto_pengguna_;
    int rating ;
    ProgressDialog progressDialog;

    final Context context = this;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trainer);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        pesan_trainer = findViewById(R.id.pesan_trainer);
        foto_trainer = findViewById(R.id.foto_trainer);
        ulasan = findViewById(R.id.ulasan_trainer);
        nama_trainer = findViewById(R.id.nama_trainer);
        deskripsi_trainer = findViewById(R.id.deskripsi_trainer);
        harga_trainer = findViewById(R.id.harga_trainer);
        lokasi_pemesan = findViewById(R.id.lokasi_pemesan);
        tanggal_pemesan = findViewById(R.id.tanggal_pemesan);
        catatan_alamat = findViewById(R.id.catatan_alamat);
        alamat_manual = findViewById(R.id.alamat_manual);

        final DocumentReference documentReference_user = firebaseFirestore.collection("users").document(userId);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                nama_pengguna = documentSnapshot.getString("fName");
                hp_pengguna = documentSnapshot.getString("phone");
                foto_pengguna_ = documentSnapshot.getString("foto");
//                Detail_Proses.foto_pengguna = foto_pengguna_;
            }
        });

        final DocumentReference documentReference = firebaseFirestore.collection("trainer").document(userID);
        documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                nama_trainer.setText(documentSnapshot.getString("nama"));
                deskripsi_trainer.setText(documentSnapshot.getString("deskripsi"));
                harga_trainer.setText(documentSnapshot.getString("harga"));
                foto_trainer_= documentSnapshot.getString("foto");
                Picasso.get().load(foto_trainer_).into(foto_trainer);

                id_trainer = documentSnapshot.getString("userID");
            }
        });

        firebaseFirestore.collection("trainer").document(userID).collection("selesai").whereEqualTo("ulasan","").get().addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    rating = task.getResult().size();
                } else {
                    Toast.makeText(DetailTrainer.this, "TIdak ada perhitungan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        itungkomentar();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tanggal_pemesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DetailTrainer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        int bulan;
                        bulan = month+1;
                        if (bulan < 10){
                            String bulan_ = "0"+bulan;
                            if (dayOfMonth < 10){
                                String hari = "0"+dayOfMonth;
                                String sDate = hari + "-" + bulan_ + "-" + year;
                                tanggal_pemesan.setText(sDate);
                            }else{
                                String sDate = dayOfMonth + "-" + bulan_ + "-" + year;
                                tanggal_pemesan.setText(sDate);
                            }
                        }else{
                            if (dayOfMonth < 10){
                                String hari = "0"+dayOfMonth;
                                String Date = hari + "-" + bulan + "-" + year;
                                tanggal_pemesan.setText(Date);
                            }else{
                                String sDate = dayOfMonth + "-" + bulan + "-" + year;
                                tanggal_pemesan.setText(sDate);
                            }
                        }

                    }
                }, year, month, day
                );
                // 2 hari setelah milih
                Calendar hari = Calendar.getInstance();
                hari.add(Calendar.DAY_OF_MONTH, 2);
                Date newDate = hari.getTime();
                datePickerDialog.getDatePicker().setMinDate(newDate.getTime()-(newDate.getTime()%(24*60*60*1000)));

                //untuk hari ini disable
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

//        alamat_manual.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openinputdialog();
//            }
//        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(DetailTrainer.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else{
            ActivityCompat.requestPermissions(DetailTrainer.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        //untuk MAPS
        lokasi_pemesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocationOnMap();
            }
        });

        //untuk memesan
        pesan_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                pesan_trainer.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                if (tanggal_pemesan.getText().equals("Pilih tanggal...")){
                    Toast.makeText(DetailTrainer.this, "Harap isi terlebih dahulu tanggal pemesanan", Toast.LENGTH_SHORT).show();
                    pesan_trainer.getBackground().setColorFilter(0xFF03DAC5, PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    new SweetAlertDialog(DetailTrainer.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Apakah Anda Sudah Yakin?")
                            .setContentText("Jika sudah yakin maka pilih (Lanjut) agar dapat di proses")
                            .setConfirmText("Lanjut")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                    progressDialog = new ProgressDialog(DetailTrainer.this);
                                    progressDialog.show();
                                    progressDialog.setContentView(R.layout.progress_dialog);
                                    progressDialog.getWindow().setBackgroundDrawableResource(
                                            android.R.color.transparent
                                    );

                                    //untuk memeriksa apakah ada dokumen yang sama atau tidak
                                    DocumentReference docIdRef = firebaseFirestore.collection("riwayat_semua").document(tanggal_pemesan.getText().toString() + ", "+ nama_trainer.getText().toString());
                                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Snackbar snackbar = Snackbar.make(v, "Sudah ada yang memesan pada hari yang sama", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    progressDialog.dismiss();
                                                    pesan_trainer.getBackground().setColorFilter(0xFF03DAC5, PorterDuff.Mode.SRC_ATOP);
                                                } else {
                                                    insert_riwayat();
                                                    startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
                                                    finish();
                                                    progressDialog.dismiss();
                                                }
                                            } else {
                                                Log.d(TAG, "Failed with: ", task.getException());
                                            }
                                        }
                                    });
                                }
                            })
                            .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    pesan_trainer.getBackground().setColorFilter(0xFF03DAC5, PorterDuff.Mode.SRC_ATOP);
                                }
                            }).show();
                }
            }
        });

    }

    private void itungkomentar() {
        firebaseFirestore.collection("trainer").document(userID).collection("selesai").whereEqualTo("ulasan","")
                .get()
                .addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< QuerySnapshot > task) {
                        if ((task.isSuccessful())){
                            ArrayList< Float > total = new ArrayList< Float >();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String total1 = (String) documentSnapshot.getData().get("rating");
                                Float total_ = Float.parseFloat(total1);
                                total.add(total_);
                            }

                            Double sum = 0.0;
                            for( Float i : total) {
                                sum += i;
                            }

                            Double totharga = Double.valueOf(sum.intValue());
                            Double total_rating;
                            total_rating = totharga / rating;

                            ulasan.setText(total_rating + "  ( "+rating+" komentar"+" )");
                        }else{
                            Log.d(TAG, "Error ", task.getException());
                        }
                    }
                });
    }

    private void insert_riwayat() {
//        1. status
//        2. nama trainer
//        3. harga trainer
//        4. nama pengguna
//        5. no hp pengguna
//        6. alamat pengguna
//        7. tanggal pemesan

        // Untuk Pengguna
        Map<String, Object> riwayat_pengguna = new HashMap<>();
        riwayat_pengguna.put("status", "Proses");
        riwayat_pengguna.put("nama_trainer", nama_trainer.getText().toString());
        riwayat_pengguna.put("harga_trainer", harga_trainer.getText().toString());
        riwayat_pengguna.put("nama_pengguna", nama_pengguna);
        riwayat_pengguna.put("no_hp", hp_pengguna);
        riwayat_pengguna.put("alamat_pengguna", lokasi_pemesan.getText().toString());
        riwayat_pengguna.put("catatan", catatan_alamat.getText().toString());
        riwayat_pengguna.put("tanggal_pemesan", tanggal_pemesan.getText().toString());
        riwayat_pengguna.put("foto_trainer", foto_trainer_);
        riwayat_pengguna.put("id_trainer", id_trainer);
        riwayat_pengguna.put("id_pengguna", userId);
        riwayat_pengguna.put("latitude_pengguna", latitude);
        riwayat_pengguna.put("longtitude_pengguna", longtitude);
        riwayat_pengguna.put("userID", tanggal_pemesan.getText().toString() + ", "+ nama_trainer.getText().toString());

        firebaseFirestore.collection("users").document(userId)
                .collection("proses").document(tanggal_pemesan.getText().toString() + ", "+ nama_trainer.getText().toString())
                .set(riwayat_pengguna);

//        1. status
//        2. tanggal pemesan
//        3. nama pemesan
//        4. no hp pemesan
//        5. alamat pemesan

        // Untuk Trainer
        Map<String, Object> riwayat_trainer = new HashMap<>();
        riwayat_trainer.put("status", "Proses");
        riwayat_trainer.put("tanggal_pemesan", tanggal_pemesan.getText().toString());
        riwayat_trainer.put("nama_pemesan", nama_pengguna);
        riwayat_trainer.put("no_hp", hp_pengguna);
        riwayat_trainer.put("catatan", catatan_alamat.getText().toString());
        riwayat_trainer.put("alamat_pengguna", lokasi_pemesan.getText().toString());
        riwayat_trainer.put("foto_pengguna", foto_pengguna_);
        riwayat_trainer.put("latitude_pengguna", latitude);
        riwayat_trainer.put("longtitude_pengguna", longtitude);
        riwayat_trainer.put("id_pengguna", userId);
        riwayat_trainer.put("userID", tanggal_pemesan.getText().toString() + ", "+ nama_trainer.getText().toString());

        firebaseFirestore.collection("trainer").document(userID)
                .collection("proses").document(tanggal_pemesan.getText().toString() + ", "+ nama_trainer.getText().toString())
                .set(riwayat_trainer);

        // Untuk semua cek tanggal dan nama pemesan
        Map<String, Object> riwaat_semua = new HashMap<>();
        riwaat_semua.put("status", "Proses");
        riwaat_semua.put("tanggal_pemesan", tanggal_pemesan.getText().toString());
        riwaat_semua.put("nama_pemesan", nama_pengguna);
        riwaat_semua.put("no_hp", hp_pengguna);
        riwaat_semua.put("alamat_pengguna", lokasi_pemesan.getText().toString());
        riwaat_semua.put("catatan", catatan_alamat.getText().toString());
        riwaat_semua.put("userID", tanggal_pemesan.getText().toString() + ", "+ nama_trainer.getText().toString());

        firebaseFirestore.collection("riwayat_semua").document(tanggal_pemesan.getText().toString() +", " + nama_trainer.getText().toString())
                .set(riwaat_semua);
    }

    private void selectLocationOnMap() {
        String apiKey = getString(R.string.places_api_key);
        startMapActivity(apiKey);
    }

    private void startMapActivity(String apiKey) {
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(SimplePlacePicker.API_KEY,apiKey);

        intent.putExtras(bundle);
        startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE  && resultCode == RESULT_OK){
            if (data != null) updateUi(data);

        }
    }

    private void updateUi(Intent data){
        lokasi = data.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS);
        lokasi_pemesan.setText(lokasi);
        latitude = String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,-1));
        longtitude = String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,-1));
//        Log.d(TAG, "Latitude Maps Activity : " +String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,-1)));
//        Log.d(TAG, "Longtitudr Maps Activity : " +String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,-1)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectLocationOnMap();
        }
    }

    //check for location permission
    public static boolean hasPermissionInManifest(Activity activity, int requestCode, String permissionName) {
        if (ContextCompat.checkSelfPermission(activity,
                permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionName},
                    requestCode);
        } else {
            return true;
        }
        return false;
    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener< Location >() {
            @Override
            public void onComplete(@NonNull Task< Location > task) {
                Location location = task.getResult();
                if (location != null){
                    try {
                        Geocoder geocoder = new Geocoder(DetailTrainer.this, Locale.getDefault());
                        List< Address > addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        //tv.setText(addresses.get(0).getAddressLine(0));
                        lokasi = addresses.get(0).getAddressLine(0);
                        lokasi_pemesan.setText(lokasi);
                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        longtitude = String.valueOf(addresses.get(0).getLongitude());
//                        Log.d(TAG, "Latitude Current Location: " + addresses.get(0).getLatitude());
//                        Log.d(TAG, "Longtitude Current Location: " + addresses.get(0).getLongitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void openinputdialog() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.nama);
        final EditText kecamatan = (EditText) promptsView.findViewById(R.id.kecamatan);
        final EditText kelurahan = (EditText) promptsView.findViewById(R.id.kelurahan);
        final EditText rw = (EditText) promptsView.findViewById(R.id.rw);
        final EditText rt = (EditText) promptsView.findViewById(R.id.rt);
        final EditText jalan = (EditText) promptsView.findViewById(R.id.jalan);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (jalan.getText().toString().equals("")){
                            Toast.makeText(context, "Isi terlebih dahulu nama jalan dan nomor rumah", Toast.LENGTH_SHORT).show();
                        }else if (userInput.getText().toString().equals("")){
                            Toast.makeText(context, "Isi terlebih dahulu Kota", Toast.LENGTH_SHORT).show();
                        }else if (kecamatan.getText().toString().equals("")){
                            Toast.makeText(context, "Isi terlebih dahulu Kecamatan", Toast.LENGTH_SHORT).show();
                        }else if (kelurahan.getText().toString().equals("")){
                            Toast.makeText(context, "Isi terlebih dahulu Kelurahan", Toast.LENGTH_SHORT).show();
                        }else if (rw.getText().toString().equals("")){
                            Toast.makeText(context, "Isi terlebih dahulu RW anda tinggal", Toast.LENGTH_SHORT).show();
                        }else if (rt.getText().toString().equals("")){
                            Toast.makeText(context, "Isi terlebih dahulu RT anda tinggal", Toast.LENGTH_SHORT).show();
                        }else {
                            lokasi_pemesan.setText(jalan.getText()+", RT."+ rt.getText()+"/RW."+rw.getText()+", Kel."+kelurahan.getText()
                                    +", Kec."+kecamatan.getText()+", Kota "+userInput.getText());
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void ulasan(View view) {
        Ulasan.userID = userID;
        startActivity(new Intent(getApplicationContext(), Ulasan.class));
        finish();
    }
}
