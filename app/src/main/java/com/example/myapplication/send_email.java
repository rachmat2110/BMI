package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class send_email extends AppCompatActivity {

    EditText mSubject, mMessage, email_sender;

    TextView mEmail;

    Button kirimID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        mEmail = findViewById(R.id.mailID);
        mMessage = findViewById(R.id.messageID);
        mSubject = findViewById(R.id.subjectID);
        kirimID = findViewById(R.id.kirimID);
        //email_sender = findViewById(R.id.email_sender);

        kirimID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });


    }

    private void sendMail() {
        String mail = mEmail.getText().toString();
        String message = mMessage.getText().toString();
        String subject = mSubject.getText().toString();

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mail,subject,message);

        javaMailAPI.execute();
    }
}
