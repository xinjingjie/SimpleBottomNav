package com.example.simplebottomnav.fragment.search_viewpager;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.bean.PhotoItem;
import com.example.simplebottomnav.bean.Pixabay;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;

import java.util.List;

public class SearchPicViewModel extends AndroidViewModel {
    VolleySingleton volleySingleton;
    private MutableLiveData<List<PhotoItem>> searchPhotoLiveData = new MutableLiveData<List<PhotoItem>>();
    public SearchPicViewModel(@NonNull Application application) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
    }


    public MutableLiveData<List<PhotoItem>> getSearchPhotoLiveData() {
        return searchPhotoLiveData;
    }

    public void setPhotoListLive(String key) {
        fetchData(key);
    }

    public void fetchData(String key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(key),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Pixabay pixabay = new Gson().fromJson(response, Pixabay.class);
                        searchPhotoLiveData.setValue(pixabay.getHits());
                        Log.d("did", "fetchData: success,TotalHits:" + pixabay.getTotalHits());

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
        return "https://pixabay.com/api/?key=14808073-70a71eb74f498799436435a14&q=" + key + "&per_page=" + 20;
    }
    // TODO: Implement the ViewModel
}
