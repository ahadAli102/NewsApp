package com.example.newsapp.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.newsapp.R;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.model.CustomNews;
import com.example.newsapp.ui.fragment.HomeFragment;
import com.example.newsapp.utils.DateTimeConverter;
import com.example.newsapp.utils.NetworkHelper;
import com.example.newsapp.viewmodel.LocalNewsViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class CustomNewsAdapter extends RecyclerView.Adapter<CustomNewsAdapter.CustomNewsHolder>{
    private static final String TAG = "MyTAG:Adapter";
    private List<CustomNews> mNews;
    private List<CustomNews> mMainNews;
    private Context mContext;
    private HomeFragment mFragment;
    private CustomNewsAdapterClickInterface mClickInterface;
    private LocalNewsViewModel mViewModel;

    public static int SCROLL_POSITION;

    public void setNews(List<CustomNews> mNews) {
        this.mNews = mNews;
        mMainNews = new ArrayList<>(mNews);
        Log.d(TAG, "setNews: "+mNews.size());
    }
    public List<CustomNews> getNews(){
        return mMainNews;
    }

    public void setViewModel(LocalNewsViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    private NewsEntity getSavedNewsToEntity(CustomNews news){
        Date date = new Date();
        NewsEntity entity = new NewsEntity();
        entity.setTitle(news.getTitle());
        entity.setLink(news.getLink());
        entity.setVideoUrl(news.getVideoUrl());
        entity.setDescription(news.getDescription());
        entity.setContent(news.getContent());
        entity.setPubDate(news.getPubDate());
        entity.setImageUrl(news.getImageUrl());
        entity.setSourceId(news.getSourceId());
        entity.setId(DateTimeConverter.convert(news.getPubDate()));
        entity.setDate(date);
        entity.setSave(news.isSaved());
        return entity;
    }

    public void setFragment(HomeFragment mFragment){
        this.mFragment = mFragment;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public CustomNews getNews(int position){
        return mNews.get(position);
    }

    public void setClickInterface(CustomNewsAdapterClickInterface mClickInterface) {
        this.mClickInterface = mClickInterface;
    }

    @NonNull
    @Override
    public CustomNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_news,parent,false);
        return new CustomNewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomNewsHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: position : "+position);
        SCROLL_POSITION = position;
        CustomNews news = mNews.get(position);
        holder.titleTextView.setText(news.getTitle());
        if (news.getSourceId() == null) {
            holder.publisherTextView.setText("Not mentioned");
        } else {
            holder.publisherTextView.setText(news.getSourceId().toUpperCase());
        }
        if (news.getPubDate() == null) {
            holder.publishedDateTextView.setText("Not mentioned");
        } else {
            holder.publishedDateTextView.setText(DateTimeConverter.convert(news.getPubDate(),1));
        }
        if (news.isSaved()) {
            holder.savedImageView.setImageResource(R.drawable.ic_favorite_home);
        } else {
            holder.savedImageView.setImageResource(R.drawable.ic_favorite);
        }
        if(news.getImageUrl() == null  || news.getImageUrl().equals("null")){
            news.setImageUrl("https://cdn.pixabay.com/photo/2016/02/01/00/56/news-1172463_960_720.jpg");
        }
        //Log.d(TAG, "onBindViewHolder: position image position : "+position+" url : "+news.getImageUrl());
        try{
            if(news.getImage() == null){
                Glide.with(mContext)
                        .asBitmap()
                        .load(news.getImageUrl())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.newsImageView.setImageBitmap(resource);
                                news.setImage(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

            }
            else {
                holder.newsImageView.setImageBitmap(news.getImage());
            }
        }
        catch (Exception e){
            Log.e(TAG, "onBindViewHolder: ", e);
        }
        finally {
            holder.mGif.setVisibility(View.GONE);
        }

        holder.savedImageView.setOnClickListener(v -> {
            news.setSaved(!news.isSaved());
            if (news.isSaved()) {
                mFragment.mSavedMap.put(news.getPubDate(),true);
                mViewModel.insertNews(getSavedNewsToEntity(news));
                holder.savedImageView.setImageResource(R.drawable.ic_favorite_home);
            } else {
                mFragment.mSavedMap.remove(news.getPubDate());
                mViewModel.deleteNews(getSavedNewsToEntity(news));
                holder.savedImageView.setImageResource(R.drawable.ic_favorite);
            }
        });

        //Log.d(TAG, "onBindViewHolder: position : "+position+" == "+(mNews.size()-1));
        if(position == (mMainNews.size()-1)){
            if(mNews.get(position).getNextPage()!=null){
                Log.d(TAG, "onBindViewHolder: "+mNews.get(position).getNextPage());
                mFragment.run(mFragment.mFilterMap.get(HomeFragment.FILTER_MAIN_QUERY_KEY)+"&page="+mNews.get(position).getNextPage());
                Log.d(TAG, "onBindViewHolder: hit query : "+mFragment.mFilterMap.get(HomeFragment.FILTER_MAIN_QUERY_KEY)+"&page="+mNews.get(position).getNextPage());
            }
        }
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
            List<CustomNews> filterPost = new ArrayList<>();
            final String filterPattern = ch.toString().toLowerCase();
            if(ch==null || ch.length()==0){
                filterPost.addAll(mMainNews);
            }
            else {
                for(CustomNews m : mMainNews){
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
            mNews.addAll((List<CustomNews>)results.values);
            notifyDataSetChanged();
        }
    };

    class CustomNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView publisherTextView,publishedDateTextView,titleTextView;
        private ImageView newsImageView;
        private ImageView savedImageView;
        private GifImageView mGif;
        public CustomNewsHolder(@NonNull View itemView) {
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

    public interface CustomNewsAdapterClickInterface{
        void onItemClick(int position);
        void onLongItemClick(int position);
    }

}
