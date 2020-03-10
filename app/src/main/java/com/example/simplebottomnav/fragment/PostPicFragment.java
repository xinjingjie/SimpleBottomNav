package com.example.simplebottomnav.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplebottomnav.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostPicFragment extends Fragment {
    private ImageView imageView;

    public PostPicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.post_pic, container, false);
        imageView = view.findViewById(R.id.post_pic);
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String x = getArguments().getString("IMAGEURL");
        Log.d("TAG", "onActivityCreated: " + x);
        imageView.setImageBitmap(BitmapFactory.decodeFile(x));

    }
}
