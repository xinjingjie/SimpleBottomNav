package com.example.simplebottomnav.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.bean.TotalPics;
import com.example.simplebottomnav.bean.User;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;

import java.util.List;

public class OtherUserViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private VolleySingleton volleySingleton;
    private MutableLiveData<User> foundUser = new MutableLiveData<>();
    private MutableLiveData<List<Picture>> allFoundUserPic = new MutableLiveData<>();

    public OtherUserViewModel(@NonNull Application application) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
    }


    public LiveData<User> getFoundUser(int uid) {
        new GetUserTask().execute(uid);
        return foundUser;
    }

    public LiveData<List<Picture>> getAllFoundUserPic(int uid) {
        getAllPic(uid);
        return allFoundUserPic;
    }

    class GetUserTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            int uid = (int) objects[0];
            getUserInfo(uid);
            return null;
        }
    }

    /*
    获取用户信息
     */
    private void getUserInfo(int uid) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                MainActivity.ServerPath + "user/getUserById?uid=" + uid,
                response -> {
                    User user = new Gson().fromJson(response, User.class);
                    foundUser.setValue(user);
                    Log.d("did", "foundUser::" + foundUser.toString());
                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    /*
    获取用户所有图片
     */
    private void getAllPic(int uid) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                MainActivity.ServerPath + "pic/getAll?uid=" + uid,
                response -> {
                    TotalPics totalPics = new Gson().fromJson(response, TotalPics.class);
                    allFoundUserPic.setValue(totalPics.getHits());
                    Log.d("did", "allFoundUserPic::" + totalPics.getTotal());
                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }
}
