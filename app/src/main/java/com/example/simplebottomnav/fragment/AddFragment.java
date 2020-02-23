package com.example.simplebottomnav.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.support.v4.media.session.IMediaControllerCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.simplebottomnav.viewmodel.AddViewModel;
import com.example.simplebottomnav.R;

import java.nio.InvalidMarkException;

public class AddFragment extends Fragment {

    private AddViewModel mViewModel;
    private ImageView imageView;
    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_fragment, container, false);
        imageView=view.findViewById(R.id.imageView  );
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(AddViewModel.class);
        // TODO: Use the ViewModel
        imageView.setScaleX(mViewModel.scalePosition);
        imageView.setScaleY(mViewModel.scalePosition);
        final ObjectAnimator objectAnimatorX=ObjectAnimator.ofFloat(imageView,"scaleX",0,0);
        final ObjectAnimator objectAnimatorY=ObjectAnimator.ofFloat(imageView,"scaleY",0,0);

        objectAnimatorX.setDuration(500);

        objectAnimatorY.setDuration(500);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! objectAnimatorX.isRunning() ){
                objectAnimatorX.setFloatValues(imageView.getScaleX()+0.1f);
                objectAnimatorY.setFloatValues( imageView.getScaleY()+0.1f);
                mViewModel.scalePosition+=0.1;
                objectAnimatorX.start();
                objectAnimatorY.start();
                }
            }
        });
    }

}
