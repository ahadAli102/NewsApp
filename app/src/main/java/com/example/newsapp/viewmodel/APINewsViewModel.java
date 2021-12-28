package com.example.newsapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newsapp.model.CustomNews;
import com.example.newsapp.repository.APINewsRepository;

import java.util.List;

public class APINewsViewModel extends AndroidViewModel {
    private static final String TAG = "TAG:VM:";
    private APINewsRepository mRepository;
    public static LiveData<List<CustomNews>> mNewsLiveData;
    public APINewsViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "APINewsViewModel: called");
        mRepository = new APINewsRepository();
    }

    public static void getNews(String query){
      Log.d(TAG, "getNews: called");
      mNewsLiveData = APINewsRepository.getNews(query);
    }

    public static void setAllNews(List<CustomNews> customNewsList){
      Log.d(TAG, "setAllNews: called");
      APINewsRepository.setAllNews(customNewsList);
    }
}
