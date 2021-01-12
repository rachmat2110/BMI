package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.list_chatlist;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class Chatlist extends AppCompatActivity {

    public static String keterangan = "";

    private RecyclerView recycler_view;

    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID_1;
    String user_id;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        recycler_view = findViewById(R.id.recycler_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID_1 = firebaseAuth.getCurrentUser().getUid();

        LoadData();
    }

    private static class Holder extends RecyclerView.ViewHolder {

        View mView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setDetails(Context ctx, String id, String username, String pesan_terakhir, String jumlah, String foto){
            TextView username_ = mView.findViewById(R.id.username);
            TextView pesan_terakhir_ = mView.findViewById(R.id.pesan_terakhir);
            TextView jumlah_ = mView.findViewById(R.id.jumlah);
            ImageView profile_image = mView.findViewById(R.id.profile_image);

            username_.setText(username);
            pesan_terakhir_.setText(pesan_terakhir);
            jumlah_.setText(jumlah);
            Picasso.get().load(foto).into(profile_image);
        }
    }

    private void LoadData() {
        Query query = firebaseFirestore.collection("Chatlist").document(userID_1).collection("Chatlist");

        FirestoreRecyclerOptions< list_chatlist > options = new  FirestoreRecyclerOptions.Builder<list_chatlist>()
                .setQuery(query, list_chatlist.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<list_chatlist, Chatlist.Holder >(options) {

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat, parent, false);
                return new Chatlist.Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final Holder holder, int position, @NonNull final list_chatlist model) {
                holder.setDetails(getApplicationContext(), model.getId(), model.getUsername(), model.getPesan_terakhir(), model.getJumlah(), model.getFoto());

                if (model.getJumlah().equals("") || model.getJumlah().equals("0")){
                    holder.itemView.findViewById(R.id.jumlah).setVisibility(View.GONE);
                }

                user_id = model.getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Chat.class);
//                        intent.putExtra("UserID", model.getUserID());
                        Chat.keterangan = keterangan;
                        Chat.kembali = "dari chatlist";
                        Chat.user_id = model.getId();
                        v.getContext().startActivity(intent);
                    }
                });
            }

        };

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);
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
