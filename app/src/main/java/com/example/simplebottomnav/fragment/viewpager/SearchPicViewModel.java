package com.example.simplebottomnav.fragment.viewpager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simplebottomnav.bean.PhotoItem;
import com.example.simplebottomnav.repository.LoadPic;

import java.util.List;

public class SearchPicViewModel extends AndroidViewModel {
    LoadPic loadPic;

    public SearchPicViewModel(@NonNull Application application) {
        super(application);
        loadPic = new LoadPic(application.getApplicationContext());
    }


    public LiveData<List<PhotoItem>> getPhotoListLive() {
        return loadPic.getPhotoLiveData();
    }

    public void setPhotoListLive(String key) {
        loadPic.setPhotoLiveData(key);
    }
    // TODO: Implement the ViewModel
}
