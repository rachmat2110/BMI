package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginTrainer extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    //ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    Animation atgup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_trainer);

        atgup = AnimationUtils.loadAnimation(this, R.anim.atgup);

        mEmail = findViewById(R.id.Email_Trainer);
        mPassword = findViewById(R.id.Password_Trainer);
        //progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.LoginBtn);

        mLoginBtn.startAnimation(atgup);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        if (firebaseUser != null){
////            Intent intent = new Intent(LoginTrainer.this, HomeTrainer.class);
////            startActivity(intent);
////            finish();
////        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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
                progressDialog = new ProgressDialog(LoginTrainer.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                // authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener< AuthResult >() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginTrainer.this,"Logged in successfully.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeTrainer.class));
                            finish();
                        }else{
                            Toast.makeText(LoginTrainer.this,"Error!!!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

    }
}
