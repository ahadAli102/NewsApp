package com.example.newsapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {NewsEntity.class},version = 2)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "news_database.db";
    public static volatile AppDatabase instance;
    public static final Object lock = new Object();
    public abstract NewsDao notesDao();

    public static AppDatabase getInstance(Context context) {
        if (instance==null){
            synchronized (lock){
                if (instance==null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}
