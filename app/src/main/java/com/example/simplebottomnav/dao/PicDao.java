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
    @Query("SELECT * FROM PICTURE WHERE profile_picture is null order by update_time")
    LiveData<List<Picture>> findAll();

    //  List<Picture> findALL();
    @SuppressWarnings("AndroidUnresolvedRoomSqlReference")
    @Query("DELETE FROM sqlite_sequence;")
    void reset();

    @Query("SELECT * FROM PICTURE WHERE profile_picture='background'")
    Picture findBackGroundPic();

    @Query("SELECT * FROM PICTURE WHERE profile_picture='profilePicture'")
    Picture findProfilePicture();


}
