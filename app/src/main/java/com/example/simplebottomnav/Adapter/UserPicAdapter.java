package com.example.simplebottomnav.Adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class UserPicAdapter extends ListAdapter<Picture, UserPicAdapter.UserPicViewHolder> {


    public UserPicAdapter() {
        super(new DiffUtil.ItemCallback<Picture>() {
            @Override
            public boolean areItemsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                return oldItem.getP_id() == newItem.getP_id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public UserPicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final UserPicAdapter.UserPicViewHolder holder = new UserPicAdapter.UserPicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_pic_cell, parent, false));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("Detail_Pic", getItem(holder.getAdapterPosition()));
//                NavController navController = Navigation.findNavController(v);
//                navController.navigate(R.id.action_searchFragment_to_detailPicFragment2, bundle);
//
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserPicViewHolder holder, int position) {
        final ShimmerLayout shimmerLayout = holder.shimmerLayout;
        shimmerLayout.setShimmerColor(0x55FFFFFF);
        shimmerLayout.setShimmerAngle(0);
        shimmerLayout.startShimmerAnimation();
        Glide.with(holder.itemView)
                .load(getItem(position).getLocation())
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_photo_gray_24dp)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        shimmerLayout.stopShimmerAnimation();
                        return false;
                    }
                })
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Picture picture = new Picture();
                Log.d("TAG", "onClick: " + getItem(position).getLocation());
                picture.setLocation(getItem(position).getLocation());
                bundle.putParcelable("Detail_Pic", picture);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_accountFragment_to_detailPicFragment2, bundle);
            }
        });
    }

    static class UserPicViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ShimmerLayout shimmerLayout;

        public UserPicViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerLayout = itemView.findViewById(R.id.search_shimmer);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
