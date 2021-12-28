package com.example.newsapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import com.example.newsapp.R;

public class StartActivity extends AppCompatActivity {
    UiHandlerThread mainHandlerThread = new UiHandlerThread();
    private static final String TAG = "MyTAG:Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandlerThread.setContext(this);
        mainHandlerThread.start();
        setContentView(R.layout.activity_start);
        Message msg = Message.obtain();
        msg.arg1 = 3;
        mainHandlerThread.getMainHandler().sendMessage(msg);
        /*new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                SystemClock.sleep(1000);
            }
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }).start();*/

    }

    class UiHandlerThread extends HandlerThread {
        private UiHandler mainHandler;
        private static final String TAG = "TAG:Handler";
        private Activity context;

        public void setContext(Activity context) {
            this.context = context;
        }

        public UiHandlerThread() {
            super("HandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
            // thread priority is start from -20 to 20
            // lesser the value the higher the thread priority is
        }

        @Override
        protected void onLooperPrepared() {
            Log.d(TAG, "onLooperPrepared: called");
            mainHandler = new UiHandler();
        }

        public Handler getMainHandler() {
            Log.d(TAG, "onLooperPrepared: called");
            return mainHandler;
        }

        class UiHandler extends Handler {

            @Override
            public void handleMessage(@NonNull Message msg) {
                for (int i = 0; i < msg.arg1; i++) {
                    SystemClock.sleep(1000);
                }
                try {
                    runOnUiThread(() -> {
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                        finish();
                    });
                } catch (Exception e) {

                }
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        //mainHandlerThread.setContext(null);
        mainHandlerThread.quit(); // this will remove all messages except the message that is running
        //mainHandlerThread.quitSafely(); // this will execute all messages and then exit
    }
}