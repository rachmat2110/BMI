package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.list_komentar;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Ulasan extends AppCompatActivity {

    private static final String TAG = "TAG" ;
    public static String userID = "";

    ImageView foto_trainer_ulasan;
    TextView nama_trainer, tot_rating;
    int rating ;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView list_ulasan;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulasan);

        foto_trainer_ulasan = findViewById(R.id.foto_trainer_ulasan);
        nama_trainer = findViewById(R.id.nama_trainer);
        list_ulasan = findViewById(R.id.list_ulasan);
        tot_rating = findViewById(R.id.tot_rating);
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("trainer").document(userID).collection("selesai").whereEqualTo("ulasan","").get().addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    rating = task.getResult().size();
                } else {
                    Toast.makeText(Ulasan.this, "TIdak ada perhitungan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final DocumentReference documentReference = firebaseFirestore.collection("trainer").document(userID);
        documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                nama_trainer.setText(documentSnapshot.getString("nama"));
                Picasso.get().load(documentSnapshot.getString("foto")).into(foto_trainer_ulasan);
            }
        });

        LoadData();
        itungkomentar();
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
                                if (documentSnapshot.getData().get("rating").equals("Menunggu diberikan Rating")){
                                    Double total_ = 0.0;
                                }else {
                                    String total1 = (String) documentSnapshot.getData().get("rating");
                                    Float total_ = Float.parseFloat(total1);
                                    total.add(total_);
                                }
                            }

                            Double sum = 0.0;
                            for( Float i : total) {
                                sum += i;
                            }

                            Double totharga = Double.valueOf(sum.intValue());
                            Double total_rating;
                            total_rating = totharga / rating;

                            tot_rating.setText(total_rating + "  ( "+rating+" komentar"+" )");
                        }else{
                            Log.d(TAG, "Error ", task.getException());
                        }
                    }
                });
    }

    private static class Holder extends RecyclerView.ViewHolder {

        View mView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setDetails(Context ctx, String foto_pengguna, String nama_pemesan, String rating, String komentar){
            TextView deskripsi_trainer = mView.findViewById(R.id.nama_pemesan);
            TextView nama_trainer = mView.findViewById(R.id.rating);
            ImageView foto_trainer = mView.findViewById(R.id.rImageView);
            TextView harga_trainer = mView.findViewById(R.id.komentar);

            deskripsi_trainer.setText(nama_pemesan);
            Picasso.get().load(foto_pengguna).into(foto_trainer);
            harga_trainer.setText(komentar);
            nama_trainer.setText(rating);

        }

    }

    private void LoadData() {
        Query query = firebaseFirestore.collection("trainer").document(userID).collection("selesai").whereEqualTo("ulasan","");
        FirestoreRecyclerOptions< list_komentar > options = new  FirestoreRecyclerOptions.Builder<list_komentar>()
                .setQuery(query, list_komentar.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<list_komentar, Ulasan.Holder >(options) {
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_komentar, parent, false);
                return new Ulasan.Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull list_komentar model) {
                holder.setDetails(getApplicationContext(), model.getFoto_pengguna(), model.getNama_pemesan(), model.getRating(), model.getKomentar());
            }
        };
        list_ulasan.setHasFixedSize(true);
        list_ulasan.setLayoutManager(new LinearLayoutManager(this));
        list_ulasan.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DetailTrainer.class));
        super.onBackPressed();
    }
}
