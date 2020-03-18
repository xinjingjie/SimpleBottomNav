package com.example.simplebottomnav;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.dao.PicDao;

@Database(entities = {Picture.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PicDao getPicDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "database")
                    .build();
        }
        return INSTANCE;
    }
}
