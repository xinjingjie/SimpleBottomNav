package com.example.simplebottomnav.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends ListAdapter<User, SearchUserAdapter.UserViewHolder> {


    public SearchUserAdapter() {
        super(new DiffUtil.ItemCallback<User>() {
            @Override
            public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.getUid() == newItem.getUid();
            }

            @Override
            public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(getItem(position).getProfile_picture())
                .into(holder.profile_image);
        holder.fans_number.setText(String.valueOf(getItem(position).getFans_number()));
        holder.username.setText(getItem(position).getUsername());
        holder.pics_number.setText(String.valueOf(getItem(position).getPic_number()));
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image;
        ImageButton addAttention;
        TextView username, fans_number, pics_number;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            addAttention = itemView.findViewById(R.id.addAttention);
            username = itemView.findViewById(R.id.username);
            fans_number = itemView.findViewById(R.id.fans_number);
            pics_number = itemView.findViewById(R.id.pics_number);
        }
    }
}
