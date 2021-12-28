package com.example.newsapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.repository.LocalNewsRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalNewsViewModel extends AndroidViewModel {
    private static final String TAG = "TAG:LocalVM:";
    public LiveData<List<NewsEntity>> mSavedNewsList;
    public LiveData<Integer> mDeleteMessage;
    public LiveData<Integer> mInsertMessage;
    private LocalNewsRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public LocalNewsViewModel(@NonNull Application application) {
        super(application);
        mRepository = LocalNewsRepository.getInstance(application.getApplicationContext());
    }

    public void insertNews(List<NewsEntity> entities){
        Log.d(TAG, "insertNews: called");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mRepository.insertNews(entities);
            }
        });
    }

    public void deleteNews(NewsEntity entity){
        Log.d(TAG, "deleteNews: called");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDeleteMessage = mRepository.deleteNews(entity);
            }
        });
    }
    public void deleteNews(List<NewsEntity> entities){
        Log.d(TAG, "deleteNews: called");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDeleteMessage = mRepository.deleteNews(entities);
            }
        });
    }

    public void insertNews(NewsEntity entity){
        Log.d(TAG, "insertNews: called");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mInsertMessage = mRepository.insertNews(entity);
            }
        });
    }

    public void getSavedNewsList(){
        Log.d(TAG, "getSavedNewsList: called");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mSavedNewsList = mRepository.getSavedNewsList();
            }
        });
    }
}
