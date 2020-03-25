package com.example.simplebottomnav.fragment.search_viewpager;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.bean.User;
import com.example.simplebottomnav.repository.RelationUtil;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SearchUserViewModel extends AndroidViewModel {
    private VolleySingleton volleySingleton;
    private MutableLiveData<List<User>> searchUserLivaData = new MutableLiveData<>();
    SharedPreferences preferences;
    SharedPreferences relationPreference;

    public SearchUserViewModel(@NonNull Application application) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
        preferences = application.getSharedPreferences(MainActivity.login_shpName, Context.MODE_PRIVATE);
        relationPreference = application.getSharedPreferences(MainActivity.relation_prefName, Context.MODE_PRIVATE);

    }

    public void addFollow(int beFollower_id) {
        int uid = preferences.getInt("UID", 0);
        SharedPreferences.Editor editor = relationPreference.edit();
        editor.putString("" + uid, "关注");
        editor.apply();
        SharedPreferences.Editor editor2 = preferences.edit();
        editor2.putInt("sub_number", preferences.getInt("sub_number", 0) + 1);
        editor2.apply();
        new RelationUtil(volleySingleton).Follow(uid, beFollower_id);
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
        return MainActivity.ServerPath + "user/getUserByKey?key=" + key;
    }

    public boolean isFollowed(int uid) {
        return relationPreference.contains("" + uid);
    }
}
