package com.example.newsapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.newsapp.R;
import com.example.newsapp.model.CustomNews;
import com.example.newsapp.ui.fragment.HomeFragment;
import com.example.newsapp.utils.DateTimeConverter;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = "MyTAG:News";
    private TextView mTitleText, mDetailsText,mPublisherText, mJournalistText,mDateText;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        CustomNews news = HomeFragment.mClickNews;
        Log.d(TAG, "onCreate: image url : " + news.getImageUrl());
        Log.d(TAG, "onCreate: description : " + news.getContent());
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float pxHeight = displayMetrics.heightPixels;
        float pxWidth = displayMetrics.widthPixels;
        int targetSetWidth = Math.round(pxWidth);
        mTitleText = findViewById(R.id.newsTitleId);
        mDetailsText = findViewById(R.id.newsDetailsId);
        mPublisherText = findViewById(R.id.newsPublisherId);
        mJournalistText = findViewById(R.id.newsJournalistId);
        mDateText = findViewById(R.id.newsDateId);
        mImageView = findViewById(R.id.newsImageId);

        mTitleText.setText(news.getTitle());
        mDetailsText.setText(news.getContent());
        if( news.getContent() == null){
            mDetailsText.setText(news.getDescription());
            Log.d(TAG, "onCreate: description : "+news.getDescription());
        }else if(news.getContent().equalsIgnoreCase("null") ){
            mDetailsText.setText(news.getDescription());
            Log.d(TAG, "onCreate: description : "+news.getDescription());
        }
        mDetailsText.setText(news.getContent());
        mPublisherText.setText("Source: "+news.getSourceId());
        mDateText.setText("Published at "+DateTimeConverter.convert(news.getPubDate(),1));
        Log.d(TAG, "onCreate: Title: "+news.getTitle());
        Log.d(TAG, "onCreate: Source: "+news.getSourceId());
        Log.d(TAG, "onCreate: Published at "+DateTimeConverter.convert(news.getPubDate(),1));
        Log.d(TAG, "onCreate: Publisher at "+news.getCreator());
        Log.d(TAG, "onCreate: Content: "+news.getContent());
        try{
            if(news.getCreator().trim().isEmpty() || (news.getCreator().trim().equals(""))){
                mJournalistText.setVisibility(View.GONE);
            }
            else{
                mJournalistText.setText("Publisher: "+news.getCreator());
            }
        }
        catch (Exception e){
            mJournalistText.setVisibility(View.GONE);
        }
        try {
            Bitmap image = news.getImage();
            mImageView.setImageBitmap(image);
            float w = image.getWidth();
            float h = image.getHeight();
            float targetHeight = pxWidth * (h / w);
            int targetSetHeight = Math.round(targetHeight);
            mImageView.getLayoutParams().width = targetSetWidth;
            mImageView.getLayoutParams().height = targetSetHeight;
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);

        }

        Glide.with(this)
                .asBitmap()
                .load(news.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        float w = resource.getWidth();
                        float h = resource.getHeight();
                        float targetHeight = pxWidth * (h / w);
                        int targetSetHeight = Math.round(targetHeight);
                        mImageView.setImageBitmap(resource);
                        mImageView.getLayoutParams().width = targetSetWidth;
                        mImageView.getLayoutParams().height = targetSetHeight;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }


}