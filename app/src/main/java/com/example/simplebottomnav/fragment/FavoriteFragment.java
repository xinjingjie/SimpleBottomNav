package com.example.simplebottomnav.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.viewmodel.FavoriteViewModel;

import java.util.Random;

public class FavoriteFragment extends Fragment {

    private FavoriteViewModel mViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImageView imageView;
    String url1 = "https://cdn.pixabay.com/photo/2018/03/19/13/43/feedback-3240007_1280.jpg";
    String url2 = "http://192.168.2.107:8080/image/e3e7b1da86cdcd6426de1b2d4dda3a17/266de9db-2fd2-46e2-9d55-6b1fc18a90f3.gif";
    private static final String TAG = "FavoriteFragment";
    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        imageView = view.findViewById(R.id.imageView);
        swipeRefreshLayout = view.findViewById(R.id.swipeFav);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        // TODO: Use the ViewModel

        getFavPic();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFavPic();
            }
        });
    }

    private void getFavPic() {
        String url = new Random().nextBoolean() ? url1 : url2;
        Log.d(TAG, "getFavPic: " + url);
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if ( swipeRefreshLayout.isRefreshing() ) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if ( swipeRefreshLayout.isRefreshing() ) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        return false;
                    }
                }).into(imageView);
    }
}
