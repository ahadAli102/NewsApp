package com.example.newsapp.utils;

import android.util.Log;

import com.example.newsapp.model.CustomNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonConverter {
    private static final String TAG = "MyTAG:JsonConverter";

    @SuppressWarnings("ConstantConditions")
    public static List<CustomNews> convert(String jsonData){
        //Log.d(TAG, "convert: "+jsonData);
        List<CustomNews> customNewsList = new ArrayList<>();
        try {
            JSONObject mainResponse = new JSONObject(jsonData);
            JSONArray results = mainResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                CustomNews news = new CustomNews();
                //Log.d(TAG, "convert: result : "+(i+1));
                JSONObject result = results.getJSONObject(i);
                try{
                    news.setTitle(result.getString("title"));
                }
                catch (Exception e){
                    Log.e(TAG, "convert: ", e);
                }
                try{
                    news.setLink(result.getString("link"));
                }
                catch (Exception e){
                    Log.e(TAG, "convert: ", e);
                }
                try {
                    JSONArray keywords = result.getJSONArray("keywords");
                    for (int j = 0; j < keywords.length(); j++) {
                        news.setKeywords(news.getKeywords()+","+keywords.getString(j));
                    }
                }

                catch (JSONException e){
                    Log.e(TAG, "convert: keywords: ",e );
                }
                catch (Exception e){
                    Log.e(TAG, "convert: keywords: ",e );
                }
                try {
                    JSONArray creators = result.getJSONArray("creator");
                    for (int j = 0; j < creators.length(); j++) {
                        news.setKeywords(news.getCreator()+","+creators.getString(j));
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, "convert: keywords: ",e );
                }
                catch (Exception e){
                    Log.e(TAG, "convert: creator: ",e );
                }

                try{
                    if(result.getString("image_url") != null){
                        news.setImageUrl(result.getString("image_url"));
                    }
                }
                catch (Exception e){
                    Log.e(TAG, "convert: ", e);
                }

                try{
                    if(result.getString("video_url") != null){
                        news.setVideoUrl(result.getString("video_url"));
                    }
                }
                catch (Exception e){
                    Log.e(TAG, "convert: ", e);
                }

                try{
                    news.setContent(result.getString("content"));
                }
                catch (Exception e){
                    try{
                        news.setContent(result.getString("full_description"));
                    }
                    catch (Exception ex){
                        news.setContent("Sorry No content for this news");
                    }
                }


                try{
                    news.setDescription(result.getString("description"));
                }
                catch (Exception e){
                    Log.e(TAG, "convert: ", e);
                }
                try{
                    news.setPubDate(result.getString("pubDate"));
                }
                catch (Exception e){
                    Log.e(TAG, "convert: ", e);
                }
                try{
                    news.setSourceId(result.getString("source_id"));
                }
                catch (Exception e){
                    Log.e(TAG, "convert: ", e);
                }
                try{
                    news.setNextPage(mainResponse.getInt("nextPage"));
                }
                catch (Exception e){
                    news.setNextPage(null);
                }
                //Log.d(TAG, "convert: name : "+news.getTitle());
                customNewsList.add(news);
            }

        } catch (JSONException e) {
            Log.e(TAG, "convert: ", e);
        }
        return customNewsList;
    }
    private static CustomNews getDummyNews(){
        CustomNews news = new CustomNews();
        news.setTitle("yo");
        news.setTitle("yo");
        news.setTitle("yo");
        return news;
    }
}
