package com.example.simplebottomnav.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.bean.Comment;
import com.example.simplebottomnav.bean.JsonData;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.repository.PictureRepository;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CommentsViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private PictureRepository repository;
    VolleySingleton volleySingleton;
    private String uploadResult = "";
    private MutableLiveData<List<Comment>> allComments = new MutableLiveData<>();

    public CommentsViewModel(@NonNull Application application) {
        super(application);
        repository = new PictureRepository(application);
        volleySingleton = VolleySingleton.getINSTANCE(application);
    }

    public Picture getProfilePic(int uid) {
        return repository.findProfilePicture(uid);
    }

    public void setAllComments(int pid) {
        getAllCommentsByPid(pid);
    }

    public LiveData<List<Comment>> getAllComments() {
        return allComments;
    }

    public void getAllCommentsByPid(int pid) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                MainActivity.ServerPath + "comment/getAllByPid?pid=" + pid,
                response -> {
                    List<Comment> totalComments = new Gson().fromJson(response, new TypeToken<List<Comment>>() {
                    }.getType());
                    allComments.setValue(totalComments);
                    Log.d("did", "getAllCommentsByPid: SIZE:" + totalComments.size());
                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }

    public String uploadComment(int pid, int uid, String username, String content) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                MainActivity.ServerPath + "pic/addComments?pid=" + pid + "&uid=" + uid + "&username=" + username + "&content=" + content,
                response -> {
                    JsonData jsonData = new Gson().fromJson(response, JsonData.class);
                    String result = jsonData.getMsg();
                    uploadResult = result;
                    Log.d("uploadComment", "uploadComment: " + result);

                },
                error -> {
                    Log.d("uploadComment", "uploadComment: ");
                }
        );
        volleySingleton.getQueue().add(stringRequest);
        return uploadResult;
    }



}
