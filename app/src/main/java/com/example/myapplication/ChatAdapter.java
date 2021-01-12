package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.list_chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static String keterangan = "";

    public static final int MSG_TYPE_LEFT = 2;
    public static final int MSG_TYPE_RIGHT = 1;

    private List< list_chat > mChat;
    String currentUserId;

    FirebaseUser fuser;
    SimpleDateFormat format = new SimpleDateFormat("hh:mm a, dd-MM-yy");

    ChatAdapter(String currentUserId){
        this.currentUserId = currentUserId;
        mChat = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatBubble;

        if(viewType == MSG_TYPE_RIGHT){
            chatBubble = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_right, parent, false);
        }else{
            chatBubble = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_left, parent, false);
        }

        return new ViewHolder(chatBubble);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        list_chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        Picasso.get().load(keterangan).into(holder.profile_image);

        if( chat.getTanggal() != null) {
            holder.txt_tanggal.setText(format.format(chat.getTanggal()));
        }else{
            holder.txt_tanggal.setText(format.format(new Date()));
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public void setData(List<list_chat> messages) {
        this.mChat = messages;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;
        public TextView txt_tanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
//            txt_seen = itemView.findViewById(R.id.txt_seen);
            txt_tanggal = itemView.findViewById(R.id.txt_tanggal);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("TAG","LIST CHAT"+mChat.size());
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            Log.d("TAG", "check data firestore 3 "+ mChat.size());
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
