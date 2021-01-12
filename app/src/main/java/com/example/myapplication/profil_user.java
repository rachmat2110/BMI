package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import javax.annotation.Nullable;

public class profil_user extends AppCompatActivity {

    public static final String TAG = "TAG";

    ImageView profilImage;
    TextView fullName, email, phone, TB, BB, BMI, status, count, BBT;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String userBMI;
    Integer obesitasCount;
    DatabaseReference postsRef;
    Button perhitungan_BMI;
    ProgressDialog progressDialog;

    Animation atgup, atgup2, atg3, atg4;

    RelativeLayout piechart, history, edukasi;

    LinearLayout card_berat_ideal_, card_berat_badan;

    int TAKE_IMAGE_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);

        atg4 = AnimationUtils.loadAnimation(this, R.anim.atg4);
        atg3 = AnimationUtils.loadAnimation(this, R.anim.atg3);
        atgup = AnimationUtils.loadAnimation(this, R.anim.atgup);
        atgup2 = AnimationUtils.loadAnimation(this, R.anim.atgup2);

        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        phone = findViewById(R.id.profileNumber);
        TB = findViewById(R.id.txtTB);
        BB = findViewById(R.id.txtBB);
        BMI = findViewById(R.id.txtBMI);
        status = findViewById(R.id.txtstatus);
        count = findViewById(R.id.countStatus);
        BBT = findViewById(R.id.BB);
        perhitungan_BMI = findViewById(R.id.perhitungan);
        profilImage = findViewById(R.id.profilImage);

        card_berat_ideal_ = findViewById(R.id.card_berat_ideal_);
        card_berat_badan = findViewById(R.id.card_berat_badan);

        history = findViewById(R.id.history);
        piechart = findViewById(R.id.piechart);
        edukasi = findViewById(R.id.edukasi);

        card_berat_ideal_.startAnimation(atg3);
        card_berat_badan.startAnimation(atg4);

        postsRef = FirebaseDatabase.getInstance().getReference().child("users");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        userBMI = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullName.setText(documentSnapshot.getString("fName"));
                status.setText(documentSnapshot.getString("status"));
                TB.setText(documentSnapshot.getString("Tinggi_Badan"));
                BB.setText(documentSnapshot.getString("Berat_Badan"));
                BMI.setText(documentSnapshot.getString("BMI"));
                Picasso.get().load(documentSnapshot.getString("foto")).into(profilImage);
            }
        });

        history.startAnimation(atgup2);
        edukasi.startAnimation(atgup2);
        piechart.startAnimation(atgup2);
        perhitungan_BMI.startAnimation(atgup);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        if (user.getPhotoUrl() != null){
//            Glide.with(this)
//                    .load(user.getPhotoUrl())
//                    .into(profilImage);
//        }

    }

    public void perhitungan(View view) {
        startActivity(new Intent(getApplicationContext(), hitung_bmi_online.class));
    }

    public void history(View view) {
        startActivity(new Intent(getApplicationContext(), history.class));
    }

    public void edukasi(View view){
        startActivity(new Intent(getApplicationContext(), news_app.class));
    }

    public void berat_ideal (View view){
        startActivity(new Intent(getApplicationContext(), berat_ideal.class));
    }

    public void target_berat (View view){
        startActivity(new Intent(getApplicationContext(), target_berat.class));
    }
    public void piechart(View view){
        startActivity(new Intent(getApplicationContext(), PieChart.class));
    }

    public void handleImageClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profilImage.setImageBitmap(bitmap);
                    progressDialog = new ProgressDialog(profil_user.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(userId + ".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener< UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e.getCause() );
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener< Uri >() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri);

                        fStore.collection("users").document(userId)
                                .update("foto", String.valueOf(uri))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(profil_user.this, "Updated"+String.valueOf(uri), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(profil_user.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(profil_user.this, "Updates Succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profil_user.this, "Profile Image Failed...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sign_out(View view) {
//        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void dashboard(View view) {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}
