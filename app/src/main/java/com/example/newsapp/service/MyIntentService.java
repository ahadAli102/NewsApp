package com.example.newsapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.newsapp.ui.fragment.HomeFragment;
import com.example.newsapp.viewmodel.APINewsViewModel;

import java.io.IOException;

public class MyIntentService extends IntentService {
    public static final String SERVICE_PAYLOAD = "SERVICE_PAYLOAD";
    public static final String SERVICE_MESSAGE = "SERVICE_MESSAGE";
    private static final String TAG = "TAG:Service";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        String data = uri.toString();
        Log.d(TAG, "onHandleIntent: query : "+data);
        APINewsViewModel.getNews(data);
        sendMessageToUI("Done");

    }

    private void sendMessageToUI(String data) {
        Intent intent=new Intent(SERVICE_MESSAGE);
        intent.putExtra(SERVICE_PAYLOAD,data);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
    }
}
