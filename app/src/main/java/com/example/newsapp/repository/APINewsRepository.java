package com.example.newsapp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.newsapp.model.CustomNews;
import com.example.newsapp.utils.HttpCallerHelper;
import com.example.newsapp.utils.JsonConverter;

import java.io.IOException;
import java.util.List;

public class APINewsRepository {

    //https://newsdata.io/api/1/news?apikey=pub_2320f0868f525d0141226a957169cc43ec65&q=top&country=gb
    //https://newsdata.io/api/1/
    //news?apikey=pub_2320f0868f525d0141226a957169cc43ec65&q=top&country=gb
    public static MutableLiveData<List<CustomNews>> mNewsMutableLiveData;
    private static final String TAG = "TAG:Repo";

    public static MutableLiveData<List<CustomNews>> getNews(String query){
        Log.d(TAG, "getNews: called");
        if(mNewsMutableLiveData == null){
            mNewsMutableLiveData = new MutableLiveData<>();
        }
        String jsonString = null;
        try {
            jsonString = HttpCallerHelper.getData(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mNewsMutableLiveData.postValue(JsonConverter.convert(jsonString));
        return mNewsMutableLiveData;
    }

    public static void setAllNews(List<CustomNews> customNewsList){
        Log.d(TAG, "setAllNews: called");
        if(mNewsMutableLiveData == null){
            mNewsMutableLiveData = new MutableLiveData<>();
        }
        Log.d(TAG, "setAllNews: called list size: "+customNewsList.size());
        mNewsMutableLiveData.setValue(customNewsList);
    }
}
