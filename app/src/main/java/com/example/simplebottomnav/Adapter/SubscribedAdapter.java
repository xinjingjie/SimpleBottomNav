package com.example.simplebottomnav.Adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class SubscribedAdapter extends ListAdapter<Picture, PicAdapter.PicViewHolder> {
    public SubscribedAdapter() {
        super(new DiffUtil.ItemCallback<Picture>() {
            @Override
            public boolean areItemsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                return oldItem.getP_id() == newItem.getP_id();

            }
        });
    }

    @NonNull
    @Override
    public PicAdapter.PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepic_cell_normal, parent, false);

        return new PicAdapter.PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicAdapter.PicViewHolder holder, int position) {
        final ShimmerLayout shimmerLayout = holder.shimmerLayout;
        shimmerLayout.setShimmerColor(0x55FFFFFF);
        shimmerLayout.setShimmerAngle(0);
        shimmerLayout.startShimmerAnimation();
        holder.likeText.setText(String.valueOf(getItem(position).getLikes()));
        holder.picDescription.setText(getItem(position).getContent());
        holder.messageText.setText(String.valueOf(getItem(position).getComments()));
        holder.userName.setText(getItem(position).getUsername());
        Glide.with(holder.imageView)
                .load(getItem(position).getProfile_picture()
                ).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(holder.userButton);
//            holder.likeButton.setImageResource(R.drawable.ic_favorite_border_gray_24dp);
        Glide.with(holder.itemView)
                .load(getItem(position).getLocation())
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
        holder.shimmerLayout.setOnClickListener(v -> {
            Log.d("TAG", "onClick: ");
            Bundle bundle = new Bundle();
            bundle.putBoolean("isProfile", false);
            bundle.putParcelable("Detail_Pic", getItem(holder.getAdapterPosition()));
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homeFragment_to_detailPicFragment2, bundle);
            //   navController.navigate(R.id.action_searchFragment_to_detailPicFragment2, bundle);

        });
    }

}
