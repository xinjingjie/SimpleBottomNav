package com.example.simplebottomnav.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.repository.PictureRepository;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private PictureRepository repository;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        repository = new PictureRepository(application);
    }

    public LiveData<List<Picture>> getAllUserPic() {
        return repository.getPicsLiveData();
    }

    public void addPic(Picture... pictures) {
        repository.insertPics(pictures);
    }

    public void deletePic(Picture... pictures) {
        repository.deletePics(pictures);
    }

    public void deleteAllPics() {
        repository.deleteAllPics();
        repository.reset();
    }

    public Picture getProfilePic() {
        return repository.findProfilePicture();
    }

    public Picture getBackgroundPic() {
        return repository.findBackGroundPic();
    }
//    private VolleySingleton volleySingleton;
//    private MutableLiveData<List<Picture>> searchPhotoLiveData = new MutableLiveData<List<Picture>>();
//    public AccountViewModel(@NonNull Application application) {
//        super(application);
//        volleySingleton = VolleySingleton.getINSTANCE(application);
//    }
//
//
//    public LiveData<List<Picture>> getSearchPhotoLiveData() {
//        return searchPhotoLiveData;
//    }
//    public void setPhotoListLive(String key) {
//        fetchData(key);
//    }
//
//    public void fetchData(String key) {
//        StringRequest stringRequest = new StringRequest(
//                StringRequest.Method.GET,
//                getUrl(key),
//                response -> {
//                    TotalPics totalPics = new Gson().fromJson(response, TotalPics.class);
//                    searchPhotoLiveData.setValue(totalPics.getHits());
//                    Log.d("did", "fetchData: success,TotalHits:" + totalPics.getTotalHits());
//
//                },
//                error -> Log.d("did", "onErrorResponse: " + error.toString())
//
//        );
//        volleySingleton.getQueue().add(stringRequest);
//    }
//
//    private String getUrl(String key) {
//        Log.d("did", "getUrl: " + key);
//        return "http://192.168.2.107:8080/api/pic/getAll?uid=" + key;
//    }
    // TODO: Implement the ViewModel
}
