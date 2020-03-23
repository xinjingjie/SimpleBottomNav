package com.example.simplebottomnav.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.bean.TotalPics;
import com.example.simplebottomnav.fragment.DetailPicFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class FetchUserPics {
    private VolleySingleton volleySingleton;
    private List<Picture> allUserPics = new ArrayList<>();
    private Application application;
    private int uid;
    private String username;
    private PictureRepository repository;
    private SharedPreferences preference;

    public FetchUserPics(Application application) {
        volleySingleton = VolleySingleton.getINSTANCE(application);
        repository = new PictureRepository(application);
        this.application = application;
        preference = Objects.requireNonNull(application.getApplicationContext()).getSharedPreferences("login_info",
                MODE_PRIVATE);
    }

    public List<Picture> getAllUserPics() {
        return allUserPics;
    }

    public void setAllUserPics() {
        uid = preference.getInt("UID", 0);
        username = preference.getString("username", null);
        fetchData(uid);
        final Handler handler = new Handler();
        Runnable runnable = () -> saveAllUserPics();
        handler.postDelayed(runnable, 2000);
    }

    private void fetchData(int key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(key),
                response -> {
                    TotalPics totalPics = new Gson().fromJson(response, TotalPics.class);
                    allUserPics = totalPics.getHits();
                    Log.d("did", "fetchData: success,TotalHits:" + totalPics.getTotalHits());
                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    private String getUrl(int key) {
        Log.d("did", "getUrl: " + key);
        return "http://192.168.2.107:8080/api/pic/getAll?uid=" + key;
    }

    private void saveAllUserPics() {
        if (getAllUserPics().size() != 0) {
            Log.d("TAG", "saveAllUserPics: " + getAllUserPics().size());
            for (int i = 0; i < getAllUserPics().size(); i++) {
                try {
                    Picture picture = allUserPics.get(i);
                    String path = new DetailPicFragment.SavePic().execute(picture.getLocation(), application.getApplicationContext()).get();
                    Picture insertPicture = new Picture(uid, username,
                            picture.getUpdate_time(),
                            path, picture.getLikes(),
                            picture.getComments(),
                            picture.getContent(),
                            picture.getTags(),
                            null);
                    repository.insertPics(insertPicture);

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            SharedPreferences.Editor editor = preference.edit();
            editor.putBoolean("isNeedDownLoad", false);
            editor.apply();
        }

        try {
            String backgroundPic = preference.getString("background_image", "http://192.168.2.107:8080/backgroundPic/donut.jpg");
            String profilePic = preference.getString("profile_picture", "http://192.168.2.107:8080/profilePicture/logo.png");
            Log.d("saveAllUserPics", "saveAllUserPics: " + profilePic);
            String backgroundPicPath = new DetailPicFragment.SavePic().execute(backgroundPic, application.getApplicationContext()).get();
            Picture backGroundPic = new Picture(uid, username, null, backgroundPicPath, 0, 0, null, null, "background");
            String profilePicPath = new DetailPicFragment.SavePic().execute(profilePic, application.getApplicationContext()).get();
            Picture profilePicture = new Picture(uid, username, null, profilePicPath, 0, 0, null, null, "profilePicture");
            repository.insertPics(backGroundPic, profilePicture);
            SharedPreferences.Editor editor = preference.edit();
            editor.putBoolean("isNeedDownLoad", false);
            editor.apply();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
