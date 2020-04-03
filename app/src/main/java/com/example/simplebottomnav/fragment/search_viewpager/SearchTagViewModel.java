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
import com.example.simplebottomnav.bean.Tag;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SearchTagViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    VolleySingleton volleySingleton;
    private MutableLiveData<List<Tag>> searchTagLiveData = new MutableLiveData<List<Tag>>();

    public SearchTagViewModel(@NonNull Application application) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);

    }

    public LiveData<List<Tag>> getSearchTagLiveData() {
        return searchTagLiveData;
    }

    public void setSearchTagLiveData(String key) {
        fetchData(key);
    }

    public void fetchData(String key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(key),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Tag> totalTags = new Gson().fromJson(response, new TypeToken<List<Tag>>() {
                        }.getType());
                        searchTagLiveData.setValue(totalTags);

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
        return MainActivity.ServerPath + "tag/getByTag?key=" + key;

    }
}
