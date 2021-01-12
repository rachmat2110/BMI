package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class DetailProsesTrainer extends AppCompatActivity {

    ImageView rImageView;
    TextView proses_trainer, tanggal_pemesan, nama_pemesan,nama_pemesan_,hp_pengguna,alamat_pemesan;
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    String userId, id_pengguna;

    String longtitude, latitude;

    //untuk map fragment
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_proses_trainer);

        proses_trainer = findViewById(R.id.proses_trainer);
        tanggal_pemesan = findViewById(R.id.tanggal_pemesan);
        nama_pemesan = findViewById(R.id.nama_pemesan);
        nama_pemesan_ = findViewById(R.id.nama_pemesan_);
        hp_pengguna = findViewById(R.id.hp_pengguna);
        alamat_pemesan = findViewById(R.id.alamat_pemesan);
        rImageView = findViewById(R.id.rImageView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        //untuk map fragment
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_maps);

        client = LocationServices.getFusedLocationProviderClient(this);

        String user_ID = getIntent().getStringExtra("userID");
        final DocumentReference documentReference_user = firebaseFirestore.collection("trainer").document(userId).collection("proses").document(user_ID);
        documentReference_user.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                proses_trainer.setText("Pesanan Sedang di "+documentSnapshot.getString("status"));
                tanggal_pemesan.setText(documentSnapshot.getString("tanggal_pemesan"));
                nama_pemesan.setText(documentSnapshot.getString("nama_pemesan"));
                nama_pemesan_.setText(documentSnapshot.getString("nama_pemesan"));
                hp_pengguna.setText(documentSnapshot.getString("no_hp"));
                alamat_pemesan.setText(documentSnapshot.getString("alamat_pengguna"));
                Picasso.get().load(documentSnapshot.getString("foto_pengguna")).into(rImageView);

                latitude = documentSnapshot.getString("latitude_pengguna");
                longtitude = documentSnapshot.getString("longtitude_pengguna");
                id_pengguna = documentSnapshot.getString("id_pengguna");

                final double latitude_pengguna = Double.valueOf(latitude);
                final double longtitude_pengguna = Double.valueOf(longtitude);

                if (latitude.equals("") && longtitude.equals("")){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(-6.307316675366932, 106.91914413124321);

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("ini lokasi rumah pemesan");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                            googleMap.addMarker(options);
                        }
                    });
                }else{
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(latitude_pengguna, longtitude_pengguna);

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("ini lokasi rumah pemesan");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeTrainer.class));
        finish();
        super.onBackPressed();
    }

    public void chat(View view) {
        Chat.keterangan = "dari trainer";
        Chat.user_id = id_pengguna;
        Chat.kembali = "dari proses";
        startActivity(new Intent(getApplicationContext(), Chat.class));
        finish();
    }
}
