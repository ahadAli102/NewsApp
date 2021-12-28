package com.example.newsapp.utils;

import android.util.Log;

import com.example.newsapp.model.FilterData;
import com.example.newsapp.ui.fragment.HomeFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryMaker {
    private static final String TAG = "MyTAG:QueryMaker";
    private static String API_KEY = "news?apikey=pub_2320f0868f525d0141226a957169cc43ec65";
    //"&category=sports,health&q=madrid&country=gb";


    public static String makeQuery(List<FilterData> selectedCategories, List<FilterData> selectedCountries,
                           List<FilterData> selectedLanguages,String query) {
        StringBuilder categories = new StringBuilder();
        StringBuilder countries = new StringBuilder();
        StringBuilder languages = new StringBuilder();
        StringBuilder buildQuery = new StringBuilder();
        for (int i = 0; i < selectedCategories.size(); i++) {
            if(i == 0){
                categories.append("&category=");
            }
            categories.append(selectedCategories.get(i).code);
            if(i != (selectedCategories.size()-1)){
                categories.append(",");
            }
        }

        for (int i = 0; i < selectedCountries.size(); i++) {
            if(i == 0){
                countries.append("&country=");
            }
            countries.append(selectedCountries.get(i).code);
            if(i != (selectedCountries.size()-1)){
                countries.append(",");
            }
        }

        for (int i = 0; i < selectedLanguages.size(); i++) {
            if(i == 0){
                languages.append("&language=");
            }
            languages.append(selectedLanguages.get(i).code);
            if(i != (selectedLanguages.size()-1)){
                languages.append(",");
            }
        }

        String[] queries = query.split(" ");
        Log.d(TAG, "makeQuery: query");

        for (int i = 0; i < queries.length; i++) {
            if(i == 0){
                buildQuery.append("&q=");
            }
            buildQuery.append(queries[i]);
            if(i != (queries.length-1)){
                buildQuery.append("%20");
            }
        }

        Log.d(TAG, "makeQuery: categories : "+categories);
        Log.d(TAG, "makeQuery: countries : "+countries);
        Log.d(TAG, "makeQuery: languages : "+languages);
        Log.d(TAG, "makeQuery: queries : "+buildQuery);
        Log.d(TAG, "makeQuery: "+API_KEY+categories+countries+languages+buildQuery);
        return API_KEY+categories+countries+languages+buildQuery;

    }
    
    public static Map<String,String> makeSpecificQuery(List<FilterData> selectedCategories, List<FilterData> selectedCountries,
                                                       List<FilterData> selectedLanguages,String query){
        Map<String,String> filterMap = new HashMap<>();
        StringBuilder categories = new StringBuilder();
        StringBuilder countries = new StringBuilder();
        StringBuilder languages = new StringBuilder();
        StringBuilder buildQuery = new StringBuilder();
        for (int i = 0; i < selectedCategories.size(); i++) {
            categories.append(selectedCategories.get(i).code);
            if(i != (selectedCategories.size()-1)){
                categories.append(",");
            }
        }

        for (int i = 0; i < selectedCountries.size(); i++) {
            countries.append(selectedCountries.get(i).code);
            if(i != (selectedCountries.size()-1)){
                countries.append(",");
            }
        }

        for (int i = 0; i < selectedLanguages.size(); i++) {
            languages.append(selectedLanguages.get(i).code);
            if(i != (selectedLanguages.size()-1)){
                languages.append(",");
            }
        }

        String[] queries = query.split(" ");

        for (int i = 0; i < queries.length; i++) {
            buildQuery.append(queries[i]);
            if(i != (queries.length-1)){
                buildQuery.append("%20");
            }
        }

        Log.d(TAG, "makeSpecificQuery: categories : "+categories);
        Log.d(TAG, "makeSpecificQuery: countries : "+countries);
        Log.d(TAG, "makeSpecificQuery: languages : "+languages);
        Log.d(TAG, "makeSpecificQuery: queries : "+buildQuery);
        filterMap.put(HomeFragment.FILTER_LANGUAGES_KEY,languages.toString());
        filterMap.put(HomeFragment.FILTER_CATEGORIES_KEY,categories.toString());
        filterMap.put(HomeFragment.FILTER_COUNTRIES_KEY,countries.toString());
        filterMap.put(HomeFragment.FILTER_QUERIES_KEY,buildQuery.toString());
        return filterMap;
    }
}
