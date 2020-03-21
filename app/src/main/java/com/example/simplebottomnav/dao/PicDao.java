package com.example.simplebottomnav.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simplebottomnav.bean.Picture;

import java.util.List;

@Dao
public interface PicDao {
    @Insert
    void insertPics(Picture... pictures);

    @Update
    void updatePics(Picture... pictures);

    @Delete
    void deletePics(Picture... pictures);

    @Query("DELETE  FROM PICTURE")
    void deleteAll();

    //WHERE profile_picture==null order by update_time
    @Query("SELECT * FROM PICTURE WHERE profile_picture is null and user_id=:uid order by update_time")
    LiveData<List<Picture>> findAllById(int uid);

    //  List<Picture> findALL();
    @SuppressWarnings("AndroidUnresolvedRoomSqlReference")
    @Query("DELETE FROM sqlite_sequence;")
    void reset();

    @Query("SELECT * FROM PICTURE WHERE profile_picture='background' AND user_id=:uid")
    Picture findBackGroundPic(int uid);

    @Query("SELECT * FROM PICTURE WHERE profile_picture='profilePicture' AND user_id=:uid")
    Picture findProfilePicture(int uid);

    @Query("UPDATE PICTURE SET location=:location WHERE profile_picture='profilePicture' AND user_id=:uid")
    void updateProfilePic(String location, int uid);
}
