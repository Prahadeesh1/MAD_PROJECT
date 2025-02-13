package com.sp.mad_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    private OnUserClickListener listener;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public interface OnUserClickListener{
        void onUserClick(User user);
    }

    public UserAdapter(Context context, List<User> userList,OnUserClickListener listener){
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_username, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getName());
        Glide.with(holder.userAvatar.getContext()).load(user.getAvatarUrl()).into(holder.userAvatar);

        holder.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.onUserClick(user);
            }
            Intent intent = new Intent(context, chat.class);
            intent.putExtra("userName", user.getName());
            intent.putExtra("userAvatar", user.getAvatarUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView userAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userAvatar = itemView.findViewById(R.id.user_avatar);
        }
    }
}
