package com.example.simplebottomnav.fragment.home_viewpager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.repository.LoadPic;

import java.util.List;

public class SubscribedViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    LoadPic loadPic;

    public SubscribedViewModel(@NonNull Application application) {
        super(application);
        loadPic = new LoadPic(application.getApplicationContext());
    }

    public void setPhotoListLive(int type, String key) {
        loadPic.setPhotoLiveData(type, key);
    }

    public LiveData<List<Picture>> getPhotoLiveData() {
        return loadPic.getPhotoLiveData();
    }
}
