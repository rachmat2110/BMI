package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    TextView mTitleTv, mDetailTv, tanggalTv;
    ImageView mImageIv;

    Bitmap bitmap;

    Button mSaveBtn, mShareBtn, mWallBtn ,btn_link;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //initialize
        tanggalTv = findViewById(R.id.tanggalTv);
        mTitleTv = findViewById(R.id.titleTv);
        mDetailTv = findViewById(R.id.descriptionTv);
        mImageIv = findViewById(R.id.imageView);
        btn_link = findViewById(R.id.btn_link);
        //mSaveBtn = findViewById(R.id.saveBtn);
        //mShareBtn = findViewById(R.id.shareBtn);
        //mWallBtn = findViewById(R.id.wallBtn);

        //getData
        String bytes = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String tanggal = getIntent().getStringExtra("tanggal");
        String description = getIntent().getStringExtra("description");
        final String get_link = getIntent().getStringExtra("link");

        //byte[] decodedString = Base64.decode(bytes, Base64.URL_SAFE );
        //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        //setdata
        tanggalTv.setText(tanggal);
        mTitleTv.setText(title);
        mDetailTv.setText(description.replace("_n","\n\n"));
        Picasso.get().load(bytes).into(mImageIv);
        btn_link.setMovementMethod(LinkMovementMethod.getInstance());
        btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(get_link));
                startActivity(intent);
            }
        });
        //mImageIv.setImageBitmap(decodedByte);

        //get image from imageview as bitmap
        //bitmap = ((BitmapDrawable)mImageIv.getDrawable()).getBitmap();

    }

    //handle onback pressed

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


