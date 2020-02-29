package com.example.simplebottomnav.Adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.simplebottomnav.bean.PhotoItem;

import de.hdodenhof.circleimageview.CircleImageView;
import io.supercharge.shimmerlayout.ShimmerLayout;


public class PicAdapter extends ListAdapter<PhotoItem, PicAdapter.PicViewHolder> {
    String current;

    public PicAdapter(String current) {
        super(new DiffUtil.ItemCallback<PhotoItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull PhotoItem oldItem, @NonNull PhotoItem newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull PhotoItem oldItem, @NonNull PhotoItem newItem) {
                return oldItem.getId() == newItem.getId();
//                        &&oldItem.getComments()==newItem.getComments()
//                        &&oldItem.getLargeImageUR().equals(newItem.getLargeImageUR())
//                        &&oldItem.getLikes()==newItem.getLikes()
//                        &&oldItem.getTags().equals(newItem.getTags())
//                        &&oldItem.getWebformatURL().equals(newItem.getWebformatURL());
            }
        });
        this.current = current;
    }

    @NonNull
    @Override
    public PicViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_pic_cell, parent, false);
        final PicViewHolder holder = new PicViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
                Bundle bundle = new Bundle();
                bundle.putParcelable("Detail_Pic", getItem(holder.getAdapterPosition()));
                NavController navController = Navigation.findNavController(view);
                ;
                switch (current) {
                    case "homeFragment":
                        navController.navigate(R.id.action_homeFragment_to_detailPicFragment2, bundle);
                        break;
                    case "searchPicFragment":
                    case "searchTagFragment":
                    case "searchUserFragment":
                        navController.navigate(R.id.action_searchFragment_to_detailPicFragment2, bundle);
                        break;

                }
//               DetailPicFragment detailPicFragment = DetailPicFragment.newInstance(bundle);
//
//                FragmentTransaction transaction = fragment.getChildFragmentManager().beginTransaction();
//                transaction.add(R.id.fragment,detailPicFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PicViewHolder holder, int position) {
        final ShimmerLayout shimmerLayout = holder.shimmerLayout;
        if ( shimmerLayout == null ) {
            Log.d("did", "onBindViewHolder: is null");
        }
        shimmerLayout.setShimmerColor(0x55FFFFFF);
        shimmerLayout.setShimmerAngle(0);
        shimmerLayout.startShimmerAnimation();
        if ( current.equals("homeFragment") ) {
            holder.likeText.setText(String.valueOf(getItem(position).getLikes()));
            holder.picDescription.setText(getItem(position).getTags());
            holder.messageText.setText(String.valueOf(getItem(position).getComments()));
            holder.userName.setText(getItem(position).getUser());
            Glide.with(holder.imageView)
                    .load(getItem(position)
                            .getUserImageURL()
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
            holder.likeButton.setImageResource(R.drawable.ic_favorite_border_gray_24dp);
            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.likeButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                }
            });
        } else {
            holder.imageView.getLayoutParams().height = getItem(position).getWebformatHeight();
            holder.imageView.getLayoutParams().width = getItem(position).getWebformatWidth();
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.function_layout.setVisibility(View.GONE);
            holder.userName.setVisibility(View.GONE);
            holder.userButton.setVisibility(View.GONE);
        }

        Glide.with(holder.itemView)
                .load(getItem(position).getWebformatURL())
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
    }

    static class PicViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ShimmerLayout shimmerLayout;
        ImageButton likeButton, messageButton;
        CircleImageView userButton;
        TextView likeText, messageText, picDescription, userName;
        ConstraintLayout function_layout;
        public PicViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerLayout = itemView.findViewById(R.id.shimmersw);
            imageView = itemView.findViewById(R.id.imageView);
            likeButton = itemView.findViewById(R.id.likeButton);
            messageButton = itemView.findViewById(R.id.messageButton);
            likeText = itemView.findViewById(R.id.likeText);
            messageText = itemView.findViewById(R.id.messageText);
            picDescription = itemView.findViewById(R.id.pic_description);
            function_layout = itemView.findViewById(R.id.function_layout);
            userButton = itemView.findViewById(R.id.userbotton);
            userName = itemView.findViewById(R.id.user_name);
        }
    }
}
