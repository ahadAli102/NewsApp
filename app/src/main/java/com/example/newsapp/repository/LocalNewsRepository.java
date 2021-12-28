package com.example.newsapp.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.newsapp.database.AppDatabase;
import com.example.newsapp.database.NewsEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalNewsRepository {
    private static final String TAG = "TAG:LocalRepo:";

    private static final Integer SUCCESSFUL = 200;
    public static LocalNewsRepository ourInstance;
    private AppDatabase appDatabase;
    private LiveData<List<NewsEntity>> mSavedNewsList;
    private LiveData<Integer> mDeleteMessage;
    private LiveData<Integer> mInsertMessage;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static LocalNewsRepository getInstance(Context context){
        return ourInstance= new LocalNewsRepository(context);
    }

    private LocalNewsRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    public LiveData<List<NewsEntity>> getSavedNewsList(){
        Log.d(TAG, "getSavedNewsList: called");
        mSavedNewsList = appDatabase.notesDao().allNews();
        /*if(appDatabase == null){
            Log.d(TAG, "getSavedNewsList: appDatabase is null");
        }
        Log.d(TAG, "getSavedNewsList: response size : "+appDatabase.notesDao().allNews().getValue().size());
        Log.d(TAG, "getSavedNewsList: saved size : "+mSavedNewsList.getValue().size());*/
        return mSavedNewsList;
    }

    public LiveData<Integer> deleteNews(NewsEntity entity){
        Log.d(TAG, "deleteNews: called");
        appDatabase.notesDao().deleteNews(entity);
        MutableLiveData<Integer> data = new MutableLiveData<>();
        data.postValue(SUCCESSFUL);
        mDeleteMessage = data;
        Log.d(TAG, "deleteNews: "+mDeleteMessage.getValue());
        return mDeleteMessage;
    }
    public LiveData<Integer> deleteNews(List<NewsEntity> entities){
        Log.d(TAG, "deleteNews: called");
        appDatabase.notesDao().deleteNews(entities);
        MutableLiveData<Integer> data = new MutableLiveData<>();
        data.postValue(SUCCESSFUL);
        mDeleteMessage = data;
        return mDeleteMessage;
    }

    public LiveData<Integer> insertNews(NewsEntity entity){
        Log.d(TAG, "insertNews: called");
        appDatabase.notesDao().insertNews(entity);
        MutableLiveData<Integer> data = new MutableLiveData<>();
        data.postValue(SUCCESSFUL);
        mInsertMessage = data;
        return mDeleteMessage;
    }
    public void insertNews(List<NewsEntity> entities){
        Log.d(TAG, "insertNews: called");
        appDatabase.notesDao().insertAllNews(entities);
    }



}
