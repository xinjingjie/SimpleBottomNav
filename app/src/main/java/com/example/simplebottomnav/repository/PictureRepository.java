package com.example.simplebottomnav.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.simplebottomnav.AppDatabase;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.dao.PicDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class PictureRepository {
    private LiveData<List<Picture>> picsLiveData;
    private PicDao picDao;
    private final static String TAG = "PictureRepository";
    public PictureRepository(Context context) {
        picDao = AppDatabase.getINSTANCE(context).getPicDao();
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_info",
                MODE_PRIVATE);
        picsLiveData = picDao.findAllById(sharedPreferences.getInt("UID", 0));
    }

    public LiveData<List<Picture>> getPicsLiveData() {
        return picsLiveData;
    }

    public void insertPics(Picture... pictures) {
        new addPic(picDao).execute(pictures);
    }

    public void reset() {
        new ResetId(picDao).execute();
    }

    public boolean deletePics(Picture... words) {
        try {
            return new DeletePictures(picDao).execute(words).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "deleteWord: GOING WRONG!");
            return false;
        }

    }

    public boolean updatePics(Picture... pictures) {
        try {
            return new UpdatePictures(picDao).execute(pictures).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "updateWord: GOING WRONG!");
            return false;
        }

    }

    public boolean deleteAllPics() {
        try {
            return new deleteAllPics(picDao).execute().get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "deleteAllWords:GOING WRONG! ");
            return false;
        }

    }


    public Picture findBackGroundPic(int uid) {
        try {
            return new FindBackGroundPic(picDao).execute(uid).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Picture findProfilePicture(int uid) {
        try {
            return new FindProfilePicture(picDao).execute(uid).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateProfilePic(String location, String uid) {
        try {
            Log.d(TAG, "updateProfilePic: didok");
            new UpdateProfilePic(picDao).execute(location, uid).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
//    public LiveData<List<Picture>> findSimilar(String str) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < str.length(); i++) {
//            sb.append(str.charAt(i));
//            sb.append("%");
//        }
//        return picDao.findSimilar("%" + sb);
//    }

    static class addPic extends AsyncTask<Picture, Void, Void> {
        private PicDao picDao;

        addPic(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Void doInBackground(Picture... pictures) {
            picDao.insertPics(pictures);
            return null;
        }
    }

    static class DeletePictures extends AsyncTask<Picture, Void, Boolean> {
        private PicDao picDao;

        DeletePictures(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Boolean doInBackground(Picture... pictures) {
            try {
                picDao.deletePics(pictures);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    static class UpdatePictures extends AsyncTask<Picture, Void, Boolean> {
        private PicDao picDao;

        UpdatePictures(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Boolean doInBackground(Picture... pictures) {
            try {
                picDao.updatePics(pictures);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    static class deleteAllPics extends AsyncTask<Void, Void, Boolean> {
        private PicDao picDao;

        deleteAllPics(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                picDao.deleteAll();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    static class ResetId extends AsyncTask<Void, Void, Void> {
        private PicDao picDao;

        ResetId(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            picDao.reset();
            return null;
        }
    }

    static class FindBackGroundPic extends AsyncTask<Integer, Void, Picture> {
        private PicDao picDao;

        FindBackGroundPic(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Picture doInBackground(Integer... integers) {

            return picDao.findBackGroundPic(integers[0]);
        }
    }

    static class FindProfilePicture extends AsyncTask<Integer, Void, Picture> {
        private PicDao picDao;

        FindProfilePicture(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Picture doInBackground(Integer... integers) {
            return picDao.findProfilePicture(integers[0]);
        }
    }

    static class UpdateProfilePic extends AsyncTask<String, Void, Void> {
        private PicDao picDao;

        UpdateProfilePic(PicDao picDao) {
            this.picDao = picDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            picDao.updateProfilePic(strings[0], Integer.parseInt(strings[1]));
            return null;
        }
    }

}
