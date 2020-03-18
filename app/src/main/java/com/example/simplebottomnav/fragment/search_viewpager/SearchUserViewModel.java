package com.example.simplebottomnav.fragment.search_viewpager;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.bean.User;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SearchUserViewModel extends AndroidViewModel {
    private VolleySingleton volleySingleton;
    private MutableLiveData<List<User>> searchUserLivaData = new MutableLiveData<>();

    public SearchUserViewModel(@NonNull Application application) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
    }

    public LiveData<List<User>> getSearchUserLivaData() {
        return searchUserLivaData;
    }

    public void setSearchUserLivaData(String key) {
        fetchData(key);
    }

    public void fetchData(String key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(key),
                response -> {
                    List<User> allSearchUser = new Gson().fromJson(response, new TypeToken<List<User>>() {
                    }.getType());
                    searchUserLivaData.setValue(allSearchUser);
                    Log.d("did", "fetchData: success,allSearchUserSize:" + allSearchUser.size());

                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    private String getUrl(String key) {
        Log.d("did", "getUrl: " + key);
        return "http://192.168.2.107:8080/api/user/getUserByKey?key=" + key;
    }

}
