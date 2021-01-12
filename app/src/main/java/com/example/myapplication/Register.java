package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";

    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText mFullName, mEmail, mPassword, mPhone, mUmur;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    //ProgressBar progressBar;
    ProgressDialog progressDialog;
    FirebaseFirestore fStore;
    String userID;
    String status;

    Animation atgup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        atgup = AnimationUtils.loadAnimation(this, R.anim.atgup);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.joinus);
        mLoginBtn = findViewById(R.id.LogInbtn);
        mUmur = findViewById(R.id.umur);
        radioGroup = findViewById(R.id.radioGroup);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //progressBar = findViewById(R.id.progressBar);

        mRegisterBtn.setAnimation(atgup);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone = mPhone.getText().toString();
                final String umur = mUmur.getText().toString();
                final String status = "";
                final String TB = "";
                final String BB = "";
                final String BMI = "";
                final String berat_ideal = "";
                final String BMR = "";
                final String keperluan_air = "";
                final String keperluan_kalori = "";
                final String Target = "";

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                final String Kelamin = radioButton.getText().toString();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6){
                    mPassword.setError("Password must be >= 6 characters");
                    return;
                }
                //progressBar.setVisibility(View.VISIBLE);
                progressDialog = new ProgressDialog(Register.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                //register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this,"User Created.",Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("status",status);
                            user.put("Tinggi_Badan", TB);
                            user.put("Berat_Badan", BB);
                            user.put("BMI", BMI);
                            user.put("Umur", umur);
                            user.put("Kelamin", Kelamin);
                            user.put("berat_ideal", berat_ideal);
                            user.put("BMR", BMR);
                            user.put("keperluan_air", keperluan_air);
                            user.put("keperluan_kalori", keperluan_kalori);
                            user.put("Target", Target);
                            user.put("Password", password);
                            user.put("status_chat", "offline");
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onsuccess : user profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "OnFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),profil_user.class));
                            finish();

                        }else{
                            Toast.makeText(Register.this,"Error!!!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

    }
    public void checkButton (View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this,"Kamu Memilih : " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }
}
