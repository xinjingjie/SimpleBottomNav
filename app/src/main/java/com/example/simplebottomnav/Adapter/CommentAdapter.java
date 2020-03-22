package com.example.simplebottomnav.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Comment;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends ListAdapter<Comment, CommentAdapter.CommentViewHolder> {

    public CommentAdapter() {
        super(new DiffUtil.ItemCallback<Comment>() {
            @Override
            public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(getItem(position).getProfile_picture())
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_photo_gray_24dp)
                .into(holder.userProfile);
        holder.comment.setText(getItem(position).getContent());
        holder.username.setText(getItem(position).getUsername());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.postTime.setText(sdf.format(getItem(position).getCreate_time()));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("uid", getItem(position).getUid());
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_detailCommentsFragment_to_otherUserFragment, bundle);
            }
        };
        holder.userProfile.setOnClickListener(listener);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userProfile;
        TextView username, comment, postTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            userProfile = itemView.findViewById(R.id.userprofile);
            comment = itemView.findViewById(R.id.comment);
            postTime = itemView.findViewById(R.id.posttime);
        }
    }
}
