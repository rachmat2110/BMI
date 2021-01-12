package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

import com.example.myapplication.Models.list_trainer;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;

    ImageView profilImage;
    TextView nama_pengguna, chat;

    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID;
    int rating;

    private RecyclerView list_trainer;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        nama_pengguna = findViewById(R.id.nama_pengguna);
        profilImage = findViewById(R.id.profilImage);
        list_trainer = findViewById(R.id.list_trainer);
        chat = findViewById(R.id.chat);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                nama_pengguna.setText(documentSnapshot.getString("fName"));
                Picasso.get().load(documentSnapshot.getString("foto")).into(profilImage);

                firebaseFirestore.collection("Chatlist").document(userID).collection("Chatlist")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
                            @Override
                            public void onComplete(@NonNull Task< QuerySnapshot > task) {
                                if (task.isSuccessful()){
                                    ArrayList<String> jumlahChat = new ArrayList<String>();

                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        String jml_chat = (String) documentSnapshot.getData().get("jumlah");
                                        jumlahChat.add(jml_chat);
                                    }

                                    if (jumlahChat.toString().equals("") || jumlahChat.toString().equals("[]")){
                                        chat.setText("Chat dengan Trainer");
                                    }else{
                                        chat.setText("Chat dengan Trainer "+jumlahChat.toString());
                                    }
                                }
                            }
                        });
            }
        });

        LoadData();
    }

    private static class Holder extends RecyclerView.ViewHolder {

        View mView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setDetails(Context ctx, String deskripsi, String foto, String harga, String nama, String userID){
            TextView deskripsi_trainer = mView.findViewById(R.id.deskripsi_trainer);
            TextView nama_trainer = mView.findViewById(R.id.nama_trainer);
            ImageView foto_trainer = mView.findViewById(R.id.foto_trainer);
            TextView harga_trainer = mView.findViewById(R.id.harga_trainer);
            TextView ID = mView.findViewById(R.id.userID);

            deskripsi_trainer.setText(deskripsi);
            Picasso.get().load(foto).into(foto_trainer);
            harga_trainer.setText(harga);
            nama_trainer.setText(nama);
            ID.setText(userID);

        }

    }

    private void LoadData() {
        Query query = firebaseFirestore.collection("trainer").orderBy("harga", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions< com.example.myapplication.Models.list_trainer > options = new  FirestoreRecyclerOptions.Builder<list_trainer>()
                .setQuery(query, list_trainer.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<list_trainer, Holder>(options) {

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dashboard, parent, false);
                return new Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final Holder holder, int position, @NonNull final com.example.myapplication.Models.list_trainer model) {
                holder.setDetails(getApplicationContext(), model.getDeskripsi(), model.getFoto(), model.getHarga(), model.getNama(), model.getUserID());

                firebaseFirestore.collection("trainer").document(model.getUserID()).collection("selesai").whereEqualTo("ulasan","").get().addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            rating = task.getResult().size();

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

                            if (total_rating > 0.0){
                                TextView hasil_komentar = holder.mView.findViewById(R.id.hasil_komentar);
                                hasil_komentar.setText(total_rating + "  ( "+rating+" komentar"+" )");
                            }else{
                                TextView hasil_komentar = holder.mView.findViewById(R.id.hasil_komentar);
                                hasil_komentar.setText("Belum ada yang memberikan ulasan");
                            }

                        } else {
                            TextView hasil_komentar = holder.mView.findViewById(R.id.hasil_komentar);
                            hasil_komentar.setText("Belum ada yang memberikan ulasan");
                        }
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailTrainer.class);
                        intent.putExtra("foto", model.getFoto());
                        intent.putExtra("deskripsi", model.getDeskripsi());
                        intent.putExtra("harga", model.getHarga());
                        intent.putExtra("nama", model.getNama());
                        intent.putExtra("userID", model.getUserID());
                        DetailTrainer.userID = model.getUserID();
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };

        list_trainer.setHasFixedSize(true);
        list_trainer.setLayoutManager(new LinearLayoutManager(this));
        list_trainer.setAdapter(adapter);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public void ClickRiwayat (View view){
        //redirect activity to about us
        redirectActivity(this, RiwayatPengguna.class);
    }

    public void ClickChat (View view){
        //redirect activity to about us

        Chatlist.keterangan = "dari pengguna";
        redirectActivity(this, Chatlist.class);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //intialize intent
        Intent intent = new Intent(activity,aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
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
        startActivity(new Intent(getApplicationContext(), profil_user.class));
        super.onBackPressed();
    }

}
