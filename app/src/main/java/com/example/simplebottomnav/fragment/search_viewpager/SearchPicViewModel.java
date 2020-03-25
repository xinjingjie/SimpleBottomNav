package com.example.simplebottomnav.fragment.search_viewpager;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.bean.TotalPics;
import com.example.simplebottomnav.repository.LoadPic;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;

import java.util.List;

public class SearchPicViewModel extends AndroidViewModel {

    VolleySingleton volleySingleton;
    private MutableLiveData<List<Picture>> searchPhotoLiveData = new MutableLiveData<List<Picture>>();

    public SearchPicViewModel(@NonNull Application application) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
    }


    public LiveData<List<Picture>> getSearchPhotoLiveData() {
        return searchPhotoLiveData;
    }

    public void setSearchPhotoLiveData(int type, String key) {
        fetchData(type, key);
    }

    public void fetchData(int type, String key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(type, key),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TotalPics totalPics = new Gson().fromJson(response, TotalPics.class);
                        searchPhotoLiveData.setValue(totalPics.getHits());
                        Log.d("did", "fetchData: success,TotalHits:" + totalPics.getTotalHits());

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

    private String getUrl(int type, String key) {
        Log.d("did", "getUrl: " + key);
        switch (type) {
            case LoadPic.FIND_TYPE_RECOMMEND:
                return MainActivity.ServerPath + "pic/getRecommend";
            case LoadPic.FIND_TYPE_CONTENT:
                return MainActivity.ServerPath + "pic/getByContent?key=" + key;
            case LoadPic.FIND_TYPE_TAG:
                return MainActivity.ServerPath + "pic/getByTag?key=" + key;
            // case FIND_TYPE_USER:
//                return "http://192.168.2.107:8080/api/pic/getByUser?key="+key;
//            break;
            default:
                return null;
        }
    }
}