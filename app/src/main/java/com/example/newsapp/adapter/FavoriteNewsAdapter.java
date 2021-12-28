package com.example.newsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.newsapp.R;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.model.CustomNews;
import com.example.newsapp.ui.fragment.FavoriteFragment;
import com.example.newsapp.utils.CacheImageManager;
import com.example.newsapp.utils.DateTimeConverter;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class FavoriteNewsAdapter extends RecyclerView.Adapter<FavoriteNewsAdapter.FavoriteNewsHolder>{
    private static final String TAG = "TAG:Adapter";
    private List<NewsEntity> mNews;
    private List<NewsEntity> mMainNews;
    private Context mContext;
    private FavoriteFragment mFragment;
    private FavoriteNewsAdapterClickInterface mClickInterface;

    public static int SCROLL_POSITION;

    public void setNews(List<NewsEntity> mNews) {
        this.mNews = mNews;
        mMainNews = new ArrayList<>(mNews);
        Log.d(TAG, "setNews: "+mNews.size());
    }

    public List<NewsEntity> getNewsList(){
        return mMainNews;
    }
    public List<NewsEntity> getUnsavedNewsList(){
        List<NewsEntity> entities = new ArrayList<>();
        for (NewsEntity entity : mMainNews) {
            if(!entity.isSave()){
                entities.add(entity);
            }
        }
        return mMainNews;
    }

    public void setFragment(FavoriteFragment mFragment){
        this.mFragment = mFragment;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public CustomNews getNews(int position){
        CustomNews news = new CustomNews();
        news.setImageUrl(mNews.get(position).getImageUrl());
        //news.setImage(mNews.get(position).getImage().);
        news.setTitle(mNews.get(position).getTitle());
        news.setLink(mNews.get(position).getLink());
        news.setContent(mNews.get(position).getContent());
        news.setPubDate(mNews.get(position).getPubDate());
        news.setContent(mNews.get(position).getCreator());
        return news;
    }

    public void setClickInterface(FavoriteNewsAdapterClickInterface mClickInterface) {
        this.mClickInterface = mClickInterface;
    }

    @NonNull
    @Override
    public FavoriteNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_news,parent,false);
        return new FavoriteNewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteNewsHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: position : "+position);
        SCROLL_POSITION = position;
        NewsEntity news = mNews.get(position);
        holder.titleTextView.setText(news.getTitle());
        if (news.getSourceId() == null) {
            holder.publisherTextView.setText("Not mentioned");
        } else {
            holder.publisherTextView.setText(news.getSourceId().toUpperCase());
        }
        if (news.getPubDate() == null) {
            holder.publishedDateTextView.setText("Not mentioned");
        } else {
            holder.publishedDateTextView.setText((DateTimeConverter.convert(news.getPubDate(),1)));
        }
        if (news.isSave()) {
            holder.savedImageView.setImageResource(R.drawable.ic_favorite_home);
        } else {
            holder.savedImageView.setImageResource(R.drawable.ic_favorite);
        }

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float pxWidth = displayMetrics.widthPixels;
        int targetSetWidth = Math.round(pxWidth);

        try{
            Bitmap bitmap= CacheImageManager.getImage(mContext,mNews.get(position));
            if (bitmap == null) {
                Glide.with(mContext)
                        .asBitmap()
                        .load(mNews.get(position).getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                float w = resource.getWidth();
                                float h = resource.getHeight();
                                float targetHeight = pxWidth*(h/w);
                                int targetSetHeight = Math.round(targetHeight);
                                holder.newsImageView.setImageBitmap(resource);
                                CacheImageManager.putImage(mContext,mNews.get(position),resource);
                                holder.newsImageView.getLayoutParams().width = targetSetWidth;
                                holder.newsImageView.getLayoutParams().height = targetSetHeight;
                                Log.d(TAG, "onBindViewHolder position height : "+position+": "+h+" width : "+w);
                                Log.d(TAG, "onBindViewHolder target position height : "+position+": "+targetSetHeight+" width : "+ targetSetWidth);
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) { }
                        });
            }else {
                holder.newsImageView.setImageBitmap(bitmap);
            }

        }catch (Exception e){

            e.printStackTrace();
        }
        finally {
            holder.mGif.setVisibility(View.GONE);
        }

        holder.savedImageView.setOnClickListener(v -> {
            news.setSave(false);
            mFragment.removeNews(news);
        });

        holder.publishedDateTextView.setText(mNews.get(position).getPubDate());
        
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public Filter getFilter(){
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence ch) {
            List<NewsEntity> filterPost = new ArrayList<>();
            final String filterPattern = ch.toString().toLowerCase();
            if(ch==null || ch.length()==0){
                filterPost.addAll(mMainNews);
            }
            else {
                for(NewsEntity m : mMainNews){
                    String title = String.valueOf(m.getTitle()).toLowerCase();
                    if(title.contains(filterPattern)){
                        filterPost.add(m);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filterPost;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mNews.clear();
            mNews.addAll((List<NewsEntity>)results.values);
            notifyDataSetChanged();
        }
    };

    class FavoriteNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView publisherTextView,publishedDateTextView,titleTextView;
        private ImageView newsImageView;
        private ImageView savedImageView;
        private GifImageView mGif;
        public FavoriteNewsHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.singleNewsTitleTextViewId);
            publisherTextView = itemView.findViewById(R.id.singleNewsPublisherId);
            publishedDateTextView = itemView.findViewById(R.id.singleNewsPublishedDateId);
            newsImageView = itemView.findViewById(R.id.singleNewsImageVIewId);
            savedImageView = itemView.findViewById(R.id.favoriteImageViewId);
            mGif = itemView.findViewById(R.id.singleNewsGif);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickInterface.onItemClick(getBindingAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mClickInterface.onLongItemClick(getBindingAdapterPosition());
            return false;
        }
    }

    public interface FavoriteNewsAdapterClickInterface{
        void onItemClick(int position);
        void onLongItemClick(int position);
    }

}
