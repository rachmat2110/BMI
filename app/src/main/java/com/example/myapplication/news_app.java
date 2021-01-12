package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class news_app extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    EditText inputSearch;
    Button btn_search;
    private RecyclerView mFirestoreListt;

    private FirestoreRecyclerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_app);

        //inputSearch = findViewById(R.id.inputSearch);
        //btn_search = findViewById(R.id.btn_search);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreListt = findViewById(R.id.firestore_listt);

        LoadData();

    }


    private static class Holder extends RecyclerView.ViewHolder {

        //private TextView list_name;
        //private TextView list_desc;
        //private ImageView rImageView;

        View mView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            //list_name = itemView.findViewById(R.id.rTitleTv);
            //list_desc = itemView.findViewById(R.id.rDescriptionTv);
            //rImageView = itemView.findViewById(R.id.rImageView);
        }
        public void setDetails(Context ctx, String title, String image, String tanggal, String description, String link){
            TextView mTitleTv = mView.findViewById(R.id.rTitleTv);
            TextView mTanggalTv = mView.findViewById(R.id.rTanggalTv);
            TextView mdescriptionTv = mView.findViewById(R.id.rDescriptionTv);
            ImageView mImageIv = mView.findViewById(R.id.rImageView);
            TextView rLinkTv = mView.findViewById(R.id.rLinkTv);

            mdescriptionTv.setText(description);
            mTitleTv.setText(title);
            mTanggalTv.setText(tanggal);
            Picasso.get().load(image).into(mImageIv);
            rLinkTv.setText(link);

        }

    }

    public void LoadData(){
        //Query

        Query query = firebaseFirestore.collection("app").orderBy("no", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Model> options = new  FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        adapter = new FirestoreRecyclerAdapter< Model, Holder >(options) {
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single, parent, false);

                return new Holder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final Model model) {
                //holder.list_name.setText(model.getTitle());
                //holder.list_desc.setText(model.getImage());
                holder.setDetails(getApplicationContext(), model.getTitle(), model.getImage(), model.getTanggal(), model.getDescription(), model.getLink());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("description", model.getDescription());
                        intent.putExtra("tanggal", model.getTanggal());
                        intent.putExtra("link", model.getLink());
                        v.getContext().startActivity(intent);
                    }
                });

            }
        };

        mFirestoreListt.setHasFixedSize(true);
        mFirestoreListt.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreListt.setAdapter(adapter);

    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
