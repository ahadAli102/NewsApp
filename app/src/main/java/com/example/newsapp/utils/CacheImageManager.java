package com.example.newsapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.newsapp.database.NewsEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheImageManager {
    private static final String TAG = "NyTAG:Cache";

    public static Bitmap getImage(Context context, NewsEntity news){
        String fileName=context.getCacheDir()+"/"+news.getImageUrl();
        File file=new File(fileName);
        Bitmap bitmap=null;
        try {
            bitmap= BitmapFactory.decodeStream(new FileInputStream(file));
            Log.d(TAG, "getImage: "+news.getImageUrl());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void putImage(Context context, NewsEntity news, Bitmap bitmap){
        String fileName=context.getCacheDir()+"/"+news.getImageUrl();
        File file=new File(fileName);
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
            Log.d(TAG, "putImage: "+news.getImageUrl());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
