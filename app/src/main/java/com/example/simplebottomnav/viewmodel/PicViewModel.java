package com.example.simplebottomnav.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.bean.JsonData;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.repository.LoadPic;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PicViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    LoadPic loadPic;
    boolean isToScrollTop = true;
    SavedStateHandle savedStateHandle;
    VolleySingleton volleySingleton;

    public PicViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
        loadPic = new LoadPic(application.getApplicationContext());
        if (!handle.contains("NEEDLOAD")) {
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences(MainActivity.login_shpName, Context.MODE_PRIVATE);
            handle.set("UID", sharedPreferences.getInt("UID", 0));
            handle.set("username", sharedPreferences.getString("username", null));
            handle.set("NEEDLOAD", true);
        }
        this.savedStateHandle = handle;
    }


    public LiveData<List<Picture>> getPhotoListLive() {
        return loadPic.getPhotoLiveData();
    }

    /*
    获取savedStateHandle
     */
    public SavedStateHandle getSavedStateHandle() {
        return savedStateHandle;
    }

    /*
    savedStateHandle保存数据
     */
    public void save(String str, String content) {
        savedStateHandle.set(str, content);
    }

    /*
    把savedStateHandle保存到sharePreferences里
     */
    public void saveAll() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(MainActivity.login_shpName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        Set<String> set = savedStateHandle.keys();
        for (String s : set) {
            String key = s;
            editor.putString(key, (String) savedStateHandle.getLiveData(key).getValue());
        }

        editor.apply();
    }

    public void deleteAll() {
        Set<String> set = savedStateHandle.keys();
        List<String> list = new ArrayList();
        for (String s : set) {
            String key = s;
            switch (key) {
                case "UID":
                case "username":
                case "NEEDLOAD":
                    break;
                default:
                    list.add(key);
                    break;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            savedStateHandle.remove(list.get(i));
        }

    }

    public void setPhotoListLive(int type, String key) {
        loadPic.setPhotoLiveData(type, key);
    }

    public void setToScrollTop(boolean toScrollTop) {
        isToScrollTop = toScrollTop;
    }

    public boolean getIsToScrollTop() {
        return isToScrollTop;
    }

    public void resetData() {
        isToScrollTop = true;
        loadPic.resetQuery();
    }

    public LiveData<Integer> getDataState() {
        return loadPic.getLoadState();
    }

    //    public void fetchData(final VolleyCallBack callback){
//        StringRequest stringRequest=new StringRequest(
//                StringRequest.Method.GET,
//                "https://pixabay.com/api/?key=14808073-70a71eb74f498799436435a14&q=flower",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        List<PhotoItem> list= new Gson().fromJson(response,Pixabay.class).getHits();
//                        photoListLive.setValue(list);
//                        callback.onSuccess(list);
//                        Log.d("did", "onResponse: ss"+list.size());
//                        Log.d("did", "onSuccess:ssss "+response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("did", "onErrorResponse: "+error.toString());
//
//                    }
//                }
//        );
//        VolleySingleton.getINSTANCE(getApplication()).getQueue().add(stringRequest);
//    }
    public void uploadData() {
        if (!savedStateHandle.contains("HAVADATA")) {
            return;
        }

        Set<String> set = savedStateHandle.keys();
        Map<Integer, Integer> upList = new HashMap<>();
        for (String s : set) {
            String key = s;
            String value = savedStateHandle.getLiveData(key).getValue().toString();
            Log.d("SavedStateHandle", "onResume: " + key + ":" + value);
            switch (key) {
                case "UID":
                case "username":
                case "HAVADATA":
                case "NEEDLOAD":
                    break;
                default:
                    upList.put(Integer.parseInt(key), Integer.parseInt(value));
                    break;
            }
        }
        for (Map.Entry<Integer, Integer> entry : upList.entrySet()) {
            StringRequest stringRequest = new StringRequest(
                    StringRequest.Method.GET,
                    MainActivity.ServerPath + "pic/addLike?pid=" + entry.getKey() + "&user_id=" + entry.getValue(),
                    response -> {
                        JsonData jsonData = new Gson().fromJson(response, JsonData.class);
                        String result = jsonData.getMsg();
                        Log.d("addLike", "onPause: " + result);
                    },
                    error -> {
                        Log.d("addLike", "onPause: ");
                    }

            );
            volleySingleton.getQueue().add(stringRequest);
        }
        deleteAll();
    }


}
