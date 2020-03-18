package com.example.simplebottomnav.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import io.supercharge.shimmerlayout.ShimmerLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPicFragment extends Fragment {
    ShimmerLayout shimmerLayout;
    PhotoView photoView;
    Picture picture;
    RequestBuilder requestBuilder;
    Button button;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public DetailPicFragment() {
        // Required empty public constructor
    }

    public static DetailPicFragment newInstance(Bundle bundle) {
        DetailPicFragment fragment = new DetailPicFragment();
        //Bundle args = new Bundle();
        //   args.putString("param", text);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.detail_pic, container, false);

        photoView = view.findViewById(R.id.Detail_Pic);
        shimmerLayout = view.findViewById(R.id.Shimmer_detail_pic);
        button = view.findViewById(R.id.change_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shimmerLayout.setShimmerColor(0x55FFFFFF);
        shimmerLayout.setShimmerAngle(0);
        shimmerLayout.startShimmerAnimation();
        button.setVisibility(View.GONE);

        if ( getArguments() != null ) {
            picture = getArguments().<Picture>getParcelable("Detail_Pic");
            if (getArguments().getBoolean("isProfile")) {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        SharedPreferences preference = Objects.requireNonNull(getActivity()).getSharedPreferences("login_info",
//                                MODE_PRIVATE);
//                        int uid=preference.getInt("UID",0);
//                        String url="http://192.168.2.107:8080/api/user/updateProfilePic";
//                        new PostPicFragment.PostPicTask()
                        MainActivity mainActivity = (MainActivity) requireActivity();
                        mainActivity.setType("PROFILE");
                        mainActivity.selectFromGalley();
                    }
                });

            }
        } else {
            Log.d("DetailPicFragment", "getArguments IS NULL");
        }

        // Log.d("did", "onActivityCreated: " + getArguments().<PhotoItem>getParcelable("Detail_Pic").getLargeImageURL());
        requestBuilder = Glide.with(requireContext())
                .load(picture.getLocation())
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
                });
        requestBuilder.into(photoView);
        final String[] items = new String[]{"保存图片"};
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                            } else {
                                savePhoto();
                            }
                            break;
                        default:

                    }
                }).show();
                return true;
            }
        });

    }



    private void savePhoto() {
        try {
            if (!"false".equals(new SavePic().execute(picture.getLocation(), requireContext()).get())) {
                Toast.makeText(requireContext(), "保存成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "保存失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static class SavePic extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            try {
                String location = objects[0].toString();
                Context context = (Context) objects[1];
                Log.d("TAG", "savePhoto: " + location);
                URL url = new URL(location);
                InputStream inputStream = url.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                Uri saveUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
                Log.d("TAG", "doInBackground: " + saveUri);
                OutputStream outputStream = context.getContentResolver().openOutputStream(saveUri);
                Boolean saveResult = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                if (saveResult) {
                    return saveUri.toString();
                } else
                    return "false";
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
