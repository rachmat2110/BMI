package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.list_chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {

    public static String user_id = "";
    public static String keterangan = "";
    public static String kembali = "";

    EditText text_send;
    TextView username, online;
    CircleImageView profile_image;
    LinearLayout btn_send;
    ImageButton img_send;
    RecyclerView recycler_view;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID, nama_sender, nama_receiver, foto_trainer, foto_pengguna, userid;
    int rating;
    boolean notify = false;

    ChatAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = findViewById(R.id.username);
        profile_image = findViewById(R.id.profile_image);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        img_send = findViewById(R.id.img_send);
        online = findViewById(R.id.online);
        recycler_view = findViewById(R.id.recycler_view);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        mAdapter = new ChatAdapter(userID);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(mAdapter);

        if (keterangan.equals("dari pengguna")){
            DocumentReference documentReference = fStore.collection("trainer").document(user_id);
            documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    username.setText(snapshot.getString("nama"));
                    Picasso.get().load(snapshot.getString("foto")).into(profile_image);
                    nama_receiver = snapshot.getString("nama");
                    foto_trainer = snapshot.getString("foto");
                    online.setText(snapshot.getString("status_chat"));
//                    userid = snapshot.getString("");

                    ChatAdapter.keterangan = snapshot.getString("foto");
                }
            });


            DocumentReference documentReference_ = fStore.collection("users").document(userID);
            documentReference_.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    nama_sender = snapshot.getString("fName");
                    foto_pengguna = snapshot.getString("foto");
                }
            });
        }else  if (keterangan.equals("dari trainer")){
            DocumentReference documentReference = fStore.collection("users").document(user_id);
            documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    username.setText(snapshot.getString("fName"));
                    Picasso.get().load(snapshot.getString("foto")).into(profile_image);
                    nama_receiver = snapshot.getString("fName");
                    foto_pengguna = snapshot.getString("foto");
                    online.setText(snapshot.getString("status_chat"));
//                    userid = snapshot.getString("userID");

                    ChatAdapter.keterangan = snapshot.getString("foto");
                }
            });

            DocumentReference documentReference_ = fStore.collection("trainer").document(userID);
            documentReference_.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    nama_sender = snapshot.getString("nama");
                    foto_trainer = snapshot.getString("foto");
                }
            });
        }

        test();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(userID, user_id, msg);
                    test();
//                    updatejumlah();
                }else{
                    Toast.makeText(Chat.this, "You cant send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        fStore.collection("Chatlist").document(userID).collection("Chatlist").document(user_id)
                .update("jumlah", "")
                .addOnCompleteListener(new OnCompleteListener< Void >() {
                    @Override
                    public void onComplete(@NonNull Task< Void > task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendMessage(final String sender, final String receiver, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String tanggal = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

        Map<String, Object> chat = new HashMap<>();
        chat.put("sender", sender);
        chat.put("receiver", receiver);
        chat.put("message", message);
        chat.put("Tanggal", FieldValue.serverTimestamp());
        chat.put("isseen", false);

        fStore.collection("Chat").document(userID+", "+user_id).collection("Chat").add(chat);
        fStore.collection("Chat").document(user_id+", "+userID).collection("Chat").add(chat);

        Map<String, Object> chatlist_ = new HashMap<>();
        chatlist_.put("id", sender);
        chatlist_.put("username", nama_sender);
        chatlist_.put("pesan_terakhir", "["+nama_sender+"] : "+text_send.getText().toString());
        chatlist_.put("jumlah", "");
        if (keterangan.equals("dari pengguna")){
            chatlist_.put("foto", foto_pengguna);
        }else {
            chatlist_.put("foto", foto_trainer);
        }
        fStore.collection("Chatlist").document(receiver).collection("Chatlist").document(sender).set(chatlist_);

        Map<String, Object> chatlist = new HashMap<>();
        chatlist.put("id", receiver);
        chatlist.put("username", nama_receiver);
        chatlist.put("pesan_terakhir", "[Anda] : "+text_send.getText().toString());
        chatlist.put("jumlah", String.valueOf(rating));
        if (keterangan.equals("dari pengguna")){
            chatlist.put("foto", foto_trainer);
        }else {
            chatlist.put("foto", foto_pengguna);
        }
        fStore.collection("Chatlist").document(sender).collection("Chatlist").document(receiver).set(chatlist);

        DocumentReference docIdRef = fStore.collection("Chatlist").document(sender).collection("Chatlist").document(receiver);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
            @Override
            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()){
                        Map<String, Object> chatlist = new HashMap<>();
                        chatlist.put("id", user_id);
                        chatlist.put("username", nama_receiver);
                        chatlist.put("pesan_terakhir", "[Anda] : "+text_send.getText().toString());
                        chatlist.put("jumlah", String.valueOf(rating));
                        //foto trainer
                        if (keterangan.equals("dari pengguna")){
                            chatlist.put("foto", foto_pengguna);
                        }else {
                            chatlist.put("foto", foto_trainer);
                        }

                        fStore.collection("Chatlist").document(userID).collection("Chatlist").document(user_id).set(chatlist);
                    }
                }
            }
        });

        DocumentReference docIdRef_ = fStore.collection("Chatlist").document(receiver).collection("Chatlist").document(sender);
        docIdRef_.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
            @Override
            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()){
                        Map<String, Object> chatlist = new HashMap<>();
                        chatlist.put("id", userID);
                        chatlist.put("username", nama_sender);
                        chatlist.put("pesan_terakhir", "["+nama_sender+"] : "+text_send.getText().toString());
                        chatlist.put("jumlah", String.valueOf(rating));
                        //foto pengguna
                        if (keterangan.equals("dari pengguna")){
                            chatlist.put("foto", foto_trainer);
                        }else {
                            chatlist.put("foto", foto_pengguna);
                        }

                        fStore.collection("Chatlist").document(user_id).collection("Chatlist").document(userID).set(chatlist);
                    }
                }
            }
        });

        if (online.getText().toString().equals("offline")){
            fStore.collection("Chat").document(userID+", "+user_id).collection("Chat")
                    .get().addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
                @Override
                public void onComplete(@NonNull Task< QuerySnapshot > task) {
                    int unread = 0;
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        list_chat chat = documentSnapshot.toObject(list_chat.class);
                        if (chat.getReceiver().equals(user_id) && !chat.isIsseen()){
                            unread++;
                        }
                    }

                    fStore.collection("Chatlist").document(user_id).collection("Chatlist").document(userID)
                            .update("jumlah", String.valueOf(unread))
                            .addOnCompleteListener(new OnCompleteListener< Void >() {
                                @Override
                                public void onComplete(@NonNull Task< Void > task) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else if (online.getText().toString().equals("online")){
            fStore.collection("Chatlist").document(user_id).collection("Chatlist").document(userID)
                    .update("jumlah", "")
                    .addOnCompleteListener(new OnCompleteListener< Void >() {
                        @Override
                        public void onComplete(@NonNull Task< Void > task) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void getData(){
        fStore.collection("Chat").document(user_id+", "+userID).collection("Chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< QuerySnapshot > task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.getId());
                            }
                            Log.d("TAG", list.toString());
                            seenMessage((ArrayList) list); // *** new ***
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void seenMessage(ArrayList list){

        for (int k = 0; k < list.size(); k++) {
            fStore.collection("Chat").document(user_id+", "+userID).collection("Chat").document((String) list.get(k))
                    .update("isseen", true).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("isseen", "Value Updated");

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(Chat.this, "Error In Updating Details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void test() {
        fStore.collection("Chat").document(userID+", "+user_id).collection("Chat").orderBy("Tanggal")
                .addSnapshotListener(this, new EventListener< QuerySnapshot >() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            //an error has occured
                        }else{
                            List< list_chat > messages = queryDocumentSnapshots.toObjects(list_chat.class);

                            mAdapter.setData(messages);
                            recycler_view.smoothScrollToPosition(mAdapter.getItemCount());
                        }
                    }
                });
    }

    private void status (String status){


            fStore.collection("users").document(userID)
                    .update("status_chat", status)
                    .addOnCompleteListener(new OnCompleteListener< Void >() {
                        @Override
                        public void onComplete(@NonNull Task< Void > task) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            fStore.collection("trainer").document(userID)
                    .update("status_chat", status)
                    .addOnCompleteListener(new OnCompleteListener< Void >() {
                        @Override
                        public void onComplete(@NonNull Task< Void > task) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        status("offline");
        if (keterangan.equals("dari pengguna")){
            if (kembali.equals("dari proses")){
                startActivity(new Intent(getApplicationContext(), RiwayatPengguna.class));
                finish();
            }
            else if (kembali.equals("dari chatlist")){
                startActivity(new Intent(getApplicationContext(), Chatlist.class));
                finish();
            }
        }else if (keterangan.equals("dari trainer")){
            if (kembali.equals("dari proses")){
                startActivity(new Intent(getApplicationContext(), RiwayatTrainer.class));
                finish();
            }else {
                startActivity(new Intent(getApplicationContext(), Chatlist.class));
                finish();
            }
        }
    }
}
