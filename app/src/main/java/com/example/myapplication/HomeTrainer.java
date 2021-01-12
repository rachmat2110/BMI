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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.list_riwayat_trainer;
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

public class HomeTrainer extends AppCompatActivity {

    public static String user_trainer = "";
    //    LinearLayout proses;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    DrawerLayout drawerLayout;

    ImageView profilImage;
    TextView nama_pengguna, chat;

    private RecyclerView list_riwayat_trainer;
    private FirestoreRecyclerAdapter adapter_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_trainer);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        list_riwayat_trainer = findViewById(R.id.list_riwayat_trainer);
        drawerLayout = findViewById(R.id.drawer_layout);
        nama_pengguna = findViewById(R.id.nama_pengguna);
        profilImage = findViewById(R.id.profilImage);
        chat = findViewById(R.id.chat);

        RiwayatTrainer.id_trainer = userId;
        DetailGagalTrainer.id_trainer = userId;

        final DocumentReference documentReference = fStore.collection("trainer").document(userId);
        documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                nama_pengguna.setText(documentSnapshot.getString("nama"));
                Picasso.get().load(documentSnapshot.getString("foto")).into(profilImage);

                fStore.collection("Chatlist").document(userId).collection("Chatlist")
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
                                        chat.setText("Chat dengan Pemesan");
                                    }else{
                                        chat.setText("Chat dengan Pemesan "+jumlahChat.toString());
                                    }
                                }
                            }
                        });
            }
        });

        LoadProses();

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public void ClickRiwayat (View view){
        //redirect activity to about us
        redirectActivity(this, RiwayatTrainer.class);
        finish();
    }

    public void ClickChat (View view){
        //redirect activity to about us
        Chatlist.keterangan = "dari trainer";
        redirectActivity(this, Chatlist.class);
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

    public class Holder extends RecyclerView.ViewHolder {

        View mView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDetails(Context ctx, String nama_pemesan, String foto_pengguna, String no_hp, String tanggal_pemesan, String userID){
            TextView nama_trainer_ = mView.findViewById(R.id.nama_pengguna);
            TextView status_ = mView.findViewById(R.id.hp_pengguna);
            ImageView foto_trainer_ = mView.findViewById(R.id.foto_pengguna);
            TextView tanggal_pemesanan_ = mView.findViewById(R.id.tanggal_pengguna);

            status_.setTextColor(0xFF9D9D9D);
            nama_trainer_.setText(nama_pemesan);
            Picasso.get().load(foto_pengguna).into(foto_trainer_);
            status_.setText(no_hp);
            tanggal_pemesanan_.setText(tanggal_pemesan);
            String user = userID;

        }
    }

    private void LoadProses() {
        Query query =  fStore.collection("trainer").document(userId).collection("proses");
        FirestoreRecyclerOptions<list_riwayat_trainer> options = new  FirestoreRecyclerOptions.Builder<list_riwayat_trainer>()
                .setQuery(query, com.example.myapplication.Models.list_riwayat_trainer.class)
                .build();

        adapter_1 = new FirestoreRecyclerAdapter<list_riwayat_trainer, HomeTrainer.Holder >(options) {

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses_trainer, parent, false);
                return new HomeTrainer.Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final com.example.myapplication.Models.list_riwayat_trainer model) {
                holder.setDetails(getApplicationContext(), model.getNama_pemesan(), model.getFoto_pengguna(), model.getNo_hp(), model.getTanggal_pemesan(), model.getUserID());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailProsesTrainer.class);
                        intent.putExtra("userID", model.getUserID());
                        v.getContext().startActivity(intent);
                    }
                });
            }

        };
        list_riwayat_trainer.setHasFixedSize(true);
        list_riwayat_trainer.setLayoutManager(new LinearLayoutManager(this));
        list_riwayat_trainer.setAdapter(adapter_1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_1.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter_1.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
