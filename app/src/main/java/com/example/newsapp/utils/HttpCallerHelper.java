package com.example.newsapp.utils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpCallerHelper {
  public static final String TAG = "MyTAG:Http";
  public static final String BASE_URL = "https://newsdata.io/api/1/";

  public static String getData(String query) throws IOException {
    Log.d(TAG, "getData: called");
    InputStream inputStream=null;
    String address = BASE_URL+query;
    Log.d(TAG, "getData: url : "+address);
    try {
      URL url=new URL(address);
      HttpURLConnection connection= (HttpURLConnection) url.openConnection();
      connection.setReadTimeout(15000);
      connection.setConnectTimeout(10000);
      connection.setDoInput(true);
      connection.setRequestMethod("GET");
      connection.connect();
      int responseCode=connection.getResponseCode();
      if(responseCode!=200){
        throw new Exception("Error: Got response code: "+responseCode);
      }
      inputStream=connection.getInputStream();
      return readStream(inputStream);
    } catch (Exception e) {
      Log.e(TAG, "getData: ", e);
    }
    finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return null;
  }

  private static String readStream(InputStream stream) throws IOException {
    Log.d(TAG, "readStream: called");
    byte[] buffer = new byte[2048];
    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
    BufferedOutputStream out = null;
    try {
      int length = 0;
      out = new BufferedOutputStream(byteArray);
      while ((length = stream.read(buffer)) > 0) {
        out.write(buffer, 0, length);
      }
      out.flush();
      return byteArray.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }
}
