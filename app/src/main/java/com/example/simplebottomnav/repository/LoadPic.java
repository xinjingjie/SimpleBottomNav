package com.example.simplebottomnav.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.bean.TotalPics;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

public class LoadPic {
    public final static int FIND_TYPE_CONTENT = 0;
    public final static int FIND_TYPE_TAG = 1;
    public final static int FIND_TYPE_USER = 2;
    public final static int FIND_TYPE_RECOMMEND = 3;
    public final static int NO_MORE_DATA = 0;
    public final static int CAN_LOAD_MORE = 1;
    public final static int NETWORK_ERROR = 2;
    public final static int INIT_STATE = 3;
    private static final String TAG = "did";
    private int perPage = 20;
    private int totalPage = 1;
    private int currentPage = 1;
    private boolean isNewQuery = true;
    private boolean isLoading = false;
    private String currentKey = new String();
    private MutableLiveData<Integer> loadStateLiveData = new MutableLiveData<>(INIT_STATE);
    private MutableLiveData<List<Picture>> photoLiveData = new MutableLiveData<>();
    private VolleySingleton volleySingleton;

    public LoadPic(Context context) {
        volleySingleton = VolleySingleton.getINSTANCE(context);

    }

    public MutableLiveData<List<Picture>> getPhotoLiveData() {
        return photoLiveData;

    }

    public void setPhotoLiveData(int type, String key) {
        fetchData(type, key);
    }

    public void resetQuery() {
        isNewQuery = true;
        isLoading = false;
        totalPage = 1;
        currentPage = 1;
        currentKey = "";
    }

    public LiveData<Integer> getLoadState() {
        return loadStateLiveData;
    }

    private void fetchData(int type, String key) {
        if (isLoading) return;
        Log.d(TAG, "fetchData: " + currentPage + totalPage + loadStateLiveData.getValue());
        if (currentPage > totalPage) {
            loadStateLiveData.setValue(NO_MORE_DATA);

            return;
        }
        isLoading = true;
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(type, key),
                response -> {
                    TotalPics totalPics = new Gson().fromJson(response, TotalPics.class);
                    totalPage = (int) Math.ceil((double) totalPics.getTotalHits() / perPage);
                    if (isNewQuery) {
                        photoLiveData.setValue(totalPics.getHits());
                    } else {
                        if (photoLiveData.getValue() != null) {
                            List<Picture> allPics = totalPics.getHits();
                            Collections.shuffle(allPics);
                            photoLiveData.getValue().addAll(allPics);
                            photoLiveData.setValue(photoLiveData.getValue());
                        }

                    }
                    loadStateLiveData.setValue(CAN_LOAD_MORE);
                    isLoading = false;
                    isNewQuery = false;
                    currentPage++;
                    Log.d("did", "fetchData: success,TotalHits:" + totalPics.getTotalHits());
                    Log.d(TAG, "fetchData: " + totalPics.getHits());
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("did", "onErrorResponse: " + error.toString());
                        loadStateLiveData.setValue(NETWORK_ERROR);
                        isLoading = false;
                    }
                }

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    private String getUrl(int type, String key) {
        Log.d("did", "getUrl: " + key);
//        return "https://pixabay.com/api/?key=14808073-70a71eb74f498799436435a14&q=" + key +
//                //"&order=latest" +
//                "&page=" + currentPage + "&per_page=" + perPage;
        switch (type) {
            case FIND_TYPE_RECOMMEND:
                return "http://192.168.2.107:8080/api/pic/getRecommend";
            case FIND_TYPE_CONTENT:
                return "http://192.168.2.107:8080/api/pic/getByContent?key=" + key;
            case FIND_TYPE_TAG:
                return "http://192.168.2.107:8080/api/pic/getByTag?key=" + key;
            // case FIND_TYPE_USER:
//                return "http://192.168.2.107:8080/api/pic/getByUser?key="+key;
//            break;
            default:
                return null;
        }


    }
}