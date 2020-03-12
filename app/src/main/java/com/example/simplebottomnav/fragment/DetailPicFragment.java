package com.example.simplebottomnav.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shimmerLayout.setShimmerColor(0x55FFFFFF);
        shimmerLayout.setShimmerAngle(0);
        shimmerLayout.startShimmerAnimation();


        if ( getArguments() != null ) {
            picture = getArguments().<Picture>getParcelable("Detail_Pic");
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
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if ( Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                } else {
                                    savePhoto();
                                }
                                break;
                            default:

                        }
                    }
                }).show();
                return true;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if ( grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    savePhoto();
                } else {
                    Toast.makeText(requireContext(), "保存失败！请给予权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void savePhoto() {
        try {
            if ( new SavePic().execute().get() ) {
                Toast.makeText(requireContext(), "保存成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "保存失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class SavePic extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Log.d("TAG", "savePhoto: " + picture.getLocation());
                URL url = new URL(picture.getLocation());
                InputStream inputStream = url.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                Uri saveUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
                OutputStream outputStream = requireContext().getContentResolver().openOutputStream(saveUri);
                Boolean saveResult = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                return saveResult;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
