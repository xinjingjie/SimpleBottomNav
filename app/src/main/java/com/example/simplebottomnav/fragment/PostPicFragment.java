package com.example.simplebottomnav.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplebottomnav.R;
import com.example.simplebottomnav.repository.UploadServerUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostPicFragment extends Fragment {
    private ImageView imageView;
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    private Button submit_button;
    private EditText contents, tags;

    public PostPicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.post_pic, container, false);
        imageView = view.findViewById(R.id.post_pic);
        contents = view.findViewById(R.id.pic_contents);
        submit_button = view.findViewById(R.id.submit_button);
        tags = view.findViewById(R.id.pic_tags);
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        String x = getArguments().getString("IMAGEURL");
        super.onActivityCreated(savedInstanceState);
        if (isAndroidQ) {
            imageView.setImageURI(Uri.parse(x));
        } else {
            assert getArguments() != null;
            Log.d("TAG", "onActivityCreated: " + x);
            imageView.setImageBitmap(BitmapFactory.decodeFile(x));
        }
        SharedPreferences preference = getActivity().getSharedPreferences("login_info",
                MODE_PRIVATE);
        int uid = preference.getInt("UID", 0);
        Log.d("button", "onActivityCreated: " + uid);
        if (uid == 0) {
            submit_button.setEnabled(false);
        }
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pic_contents = contents.getText().toString();
                String pic_tags = tags.getText().toString();

                try {
                    if (isAndroidQ) {
                        submit_button.setEnabled(false);
                        String result = (String) new PostPicTask().execute(getRealPathFromUri(requireContext(), Uri.parse(x)), uid, pic_contents, pic_tags).get();
                        Log.d("TAG", "onClick: " + result);
                        if (!("false".equals(result))) {
                            Toast.makeText(requireActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "发布失败", Toast.LENGTH_SHORT).show();
                            submit_button.setEnabled(true);
                        }
                    } else {
                        submit_button.setEnabled(false);
                        String result = (String) new PostPicTask().execute(x, uid, pic_contents, pic_tags).get();
                        Log.d("TAG", "onClick: " + result);
                        if (!("false".equals(result))) {
                            Toast.makeText(requireActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "发布失败", Toast.LENGTH_SHORT).show();
                            submit_button.setEnabled(true);

                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    static class PostPicTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            String uid = objects[1].toString();
            String contents = objects[2].toString();
            String tags = objects[3].toString();
            Map<String, String> map = new HashMap<String, String>();
            Log.d("TAG", "doInBackground: " + path);
            map.put("uid", uid);
            map.put("contents", contents);
            map.put("tags", tags);
            try {
                return (UploadServerUtil.upLoadFilePost("http://192.168.2.107:8080/api/pic/uploadFile", map, new File(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "false";
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preference = getActivity().getSharedPreferences("login_info",
                MODE_PRIVATE);
        int uid = preference.getInt("UID", 0);
        Log.d("button", "onActivityCreated: " + uid);
        if (uid != 0) {
            submit_button.setEnabled(true);
        }
    }


}
