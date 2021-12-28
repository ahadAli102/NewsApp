package com.example.newsapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(NewsEntity NewsEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNews(List<NewsEntity> newsEntities);

    @Delete
    void deleteNews(NewsEntity newsEntity);

    @Delete
    void deleteNews(List<NewsEntity> newsEntities);

    /*@Query("SELECT * from news WHERE id= :id")
    NewsEntity getNewsById(int id);*/

    @Query("DELETE from news")
    int deleteAllNews();

    /*@Query("DELETE from news WHERE customNews= :customNews")
    int deleteSingleNews(CustomNews customNews);*/

    @Query("SELECT * from news ORDER BY date DESC")
    LiveData<List<NewsEntity>> allNews();

    @Query("SELECT count(*) from news")
    int getCount();




}
