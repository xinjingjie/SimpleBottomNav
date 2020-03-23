package com.example.simplebottomnav.repository;

import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.bean.JsonData;
import com.google.gson.Gson;

public class RelationUtil {
    private VolleySingleton volleySingleton;

    public RelationUtil(VolleySingleton volleySingleton) {
        this.volleySingleton = volleySingleton;
    }

    public void Follow(int uid, int beFollower_id) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                MainActivity.ServerPath + "relation/add?follower_id=" + uid + "&beFollower_id=" + beFollower_id + "&state=关注",
                response -> {
                    JsonData result = new Gson().fromJson(response, JsonData.class);
                    Log.d("did", "FOLLOW RESULT::" + result.getCode());
                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }
}
