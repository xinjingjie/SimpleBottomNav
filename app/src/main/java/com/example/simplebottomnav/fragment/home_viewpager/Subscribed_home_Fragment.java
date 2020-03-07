package com.example.simplebottomnav.fragment.home_viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.simplebottomnav.R;

public class Subscribed_home_Fragment extends Fragment {

    private SubscribedHomeViewModel mViewModel;

    public static Subscribed_home_Fragment newInstance() {
        return new Subscribed_home_Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.subscribed_home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SubscribedHomeViewModel.class);
        // TODO: Use the ViewModel
    }

}
