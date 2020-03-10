package com.example.simplebottomnav.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.simplebottomnav.R;
import com.example.simplebottomnav.viewmodel.ExploreViewModel;

public class NotifyFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImageView imageView;
    String url1 = "https://cdn.pixabay.com/photo/2018/03/19/13/43/feedback-3240007_1280.jpg";
    private static final String TAG = "FavoriteFragment";

    public static NotifyFragment newInstance() {
        return new NotifyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notify_fragment, container, false);
        imageView = view.findViewById(R.id.imageView);
        swipeRefreshLayout = view.findViewById(R.id.swipeFav);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExploreViewModel.class);
        // TODO: Use the ViewModel
//
//        getFavPic();
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getFavPic();
//            }
//        });
    }
//
//    private void getFavPic() {
//        Glide.with(this)
//                .load(url1)
//                .placeholder(R.drawable.ic_launcher_background)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        if ( swipeRefreshLayout.isRefreshing() ) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        if ( swipeRefreshLayout.isRefreshing() ) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                        return false;
//                    }
//                }).into(imageView);
//    }
}
