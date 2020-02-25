package com.example.simplebottomnav.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.PhotoItem;
import com.github.chrisbanes.photoview.PhotoView;

import io.supercharge.shimmerlayout.ShimmerLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPicFragment extends Fragment {
    ShimmerLayout shimmerLayout;
    PhotoView photoView;

    public DetailPicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_pic, container, false);

        photoView = view.findViewById(R.id.Detail_Pic);
        shimmerLayout = view.findViewById(R.id.Shimmer_detail_pic);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shimmerLayout.setShimmerColor(0x55FFFFFF);
        shimmerLayout.setShimmerAngle(0);
        shimmerLayout.startShimmerAnimation();
        Log.d("did", "onActivityCreated: " + getArguments().<PhotoItem>getParcelable("Detail_Pic").getLargeImageURL());
        Glide.with(requireContext())
                .load(getArguments().<PhotoItem>getParcelable("Detail_Pic").getLargeImageURL())
                .placeholder(R.drawable.ic_photo_gray_24dp)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                       if ( shimmerLayout.isRefreshing()){
                        shimmerLayout.stopShimmerAnimation();
//                       }
                        return false;
                    }
                })
                .into(photoView);
    }
}
