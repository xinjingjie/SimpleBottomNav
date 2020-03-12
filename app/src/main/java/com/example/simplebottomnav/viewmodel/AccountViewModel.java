package com.example.simplebottomnav.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.bean.TotalPics;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    VolleySingleton volleySingleton;
    private MutableLiveData<List<Picture>> searchPhotoLiveData = new MutableLiveData<List<Picture>>();

    public AccountViewModel(@NonNull Application application) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
    }


    public LiveData<List<Picture>> getSearchPhotoLiveData() {
        return searchPhotoLiveData;
    }

    public void setPhotoListLive(String key) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                fetchData(key);
            }
        };
        handler.postDelayed(runnable, 5000);


    }

    public void fetchData(String key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(key),
                response -> {
                    TotalPics totalPics = new Gson().fromJson(response, TotalPics.class);
                    searchPhotoLiveData.setValue(totalPics.getHits());
                    Log.d("did", "fetchData: success,TotalHits:" + totalPics.getTotalHits());

                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    private String getUrl(String key) {
        Log.d("did", "getUrl: " + key);
        return "http://192.168.2.107:8080/api/pic/getALl?uid=" + key;
    }
    // TODO: Implement the ViewModel
}
