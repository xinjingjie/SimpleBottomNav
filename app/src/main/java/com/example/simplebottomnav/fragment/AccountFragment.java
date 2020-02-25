package com.example.simplebottomnav.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.simplebottomnav.R;
import com.example.simplebottomnav.viewmodel.AccountViewModel;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private ImageView imageView;
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        imageView = view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        // TODO: Use the ViewModel
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 0);
        objectAnimator.setDuration(1000);
        imageView.setRotation(mViewModel.rotationPosition);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !objectAnimator.isRunning() ) {
                    //Log.d("sssssssssssssss", "onClick: "+imageView.getRotation());
                    objectAnimator.setFloatValues(imageView.getRotation(), imageView.getRotation() + 120);
                    mViewModel.rotationPosition += 120;

                    objectAnimator.start();
                    // Log.d("sssssssssssssss", "onClick: "+imageView.getRotation());
                }
            }
        });
    }

}
