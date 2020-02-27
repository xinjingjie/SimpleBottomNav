package com.example.simplebottomnav.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simplebottomnav.bean.PhotoItem;
import com.example.simplebottomnav.repository.LoadPic;

import java.util.List;

public class PicViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    LoadPic loadPic;

    public PicViewModel(@NonNull Application application) {
        super(application);
        loadPic = new LoadPic(application.getApplicationContext());

    }

    public LiveData<List<PhotoItem>> getPhotoListLive() {
        return loadPic.getPhotoLiveData();
    }

    public void setPhotoListLive(String key) {
        loadPic.setPhotoLiveData(key);
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



}
