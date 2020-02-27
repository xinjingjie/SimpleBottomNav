package com.example.simplebottomnav.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.bean.PhotoItem;
import com.example.simplebottomnav.bean.Pixabay;
import com.google.gson.Gson;

import java.util.List;

public class LoadPic {
    private static final String TAG = "did";
    private MutableLiveData<List<PhotoItem>> photoLiveData = new MutableLiveData<>();
    private VolleySingleton volleySingleton;

    public LoadPic(Context context) {
        volleySingleton = VolleySingleton.getINSTANCE(context);

    }

    public MutableLiveData<List<PhotoItem>> getPhotoLiveData() {
        return photoLiveData;

    }

    public void setPhotoLiveData(String key) {
        fetchData(key);
    }

    public void fetchData(String key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(key),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        photoLiveData.setValue(new Gson().fromJson(response, Pixabay.class).getHits());
                        Log.d("did", "fetchData: success" + new Gson().fromJson(response, Pixabay.class).getTotalHits());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("did", "onErrorResponse: " + error.toString());

                    }
                }

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    private String getUrl(String key) {
        Log.d("did", "getUrl: " + key);
        return "https://pixabay.com/api/?key=14808073-70a71eb74f498799436435a14&q=" + key + "&image_type=photo";
    }
}
