package com.example.simplebottomnav.viewmodel;

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
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.bean.TotalPics;
import com.example.simplebottomnav.repository.PictureRepository;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    //    private PictureRepository repository;
//    private LiveData<List<Picture>> allUserPic = new MutableLiveData<>();
//    public AccountViewModel(@NonNull Application application) {
//        super(application);
//        repository = new PictureRepository(application);
//        allUserPic = repository.getPicsLiveData();
//    }
//
//    public LiveData<List<Picture>> getAllUserPic() {
//        return allUserPic;
//    }
//
//    public void addPic(Picture... pictures) {
//        repository.insertPics(pictures);
//    }
//
//    public void deletePic(Picture... pictures) {
//        repository.deletePics(pictures);
//    }
//
//    public void deleteAllPics() {
//        repository.deleteAllPics();
//        repository.reset();
//    }
//
//    public Picture getProfilePic(int uid) {
//        return repository.findProfilePicture(uid);
//    }
//
//    public void updateProfilePic(String location, String uid) {
//        repository.updateProfilePic(location, uid);
//    }
//
//    public Picture getBackgroundPic(int uid) {
//        return repository.findBackGroundPic(uid);
//    }
    private PictureRepository repository;
    private VolleySingleton volleySingleton;
    private SharedPreferences sharedPreferences;
    private MutableLiveData<List<Picture>> allUserPic = new MutableLiveData<List<Picture>>();
    public AccountViewModel(@NonNull Application application) {
        super(application);
        repository = new PictureRepository(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
        sharedPreferences = application.getSharedPreferences(MainActivity.login_shpName, Context.MODE_PRIVATE);
    }


    public LiveData<List<Picture>> getAllUserPic() {
        fetchData(String.valueOf(sharedPreferences.getInt("UID", 0)));
        return allUserPic;
    }

    public void fetchData(String key) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                getUrl(key),
                response -> {
                    TotalPics totalPics = new Gson().fromJson(response, TotalPics.class);
                    allUserPic.setValue(totalPics.getHits());
                    Log.d("did", "fetchData: success,TotalHits:" + totalPics.getTotalHits());

                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    private String getUrl(String key) {
        Log.d("did", "getUrl: " + key);
        return MainActivity.ServerPath + "pic/getAll?uid=" + key;
    }

    public Picture getProfilePic(int uid) {
        return repository.findProfilePicture(uid);
    }

    public Picture getBackgroundPic(int uid) {
        return repository.findBackGroundPic(uid);
    }

    public void updateProfilePic(String location, String uid) {
        repository.updateProfilePic(location, uid);
    }
    // TODO: Implement the ViewModel
}
