package com.example.newsapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsapp.R;
import com.example.newsapp.adapter.FavoriteNewsAdapter;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.ui.activity.NewsActivity;
import com.example.newsapp.viewmodel.LocalNewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private static final String TAG = "MyTAG:FavFrag:";
    private LocalNewsViewModel mViewModel;
    private List<NewsEntity> mSavedNews;
    private FavoriteNewsAdapter mAdapter;
    private SearchView mSearch;
    private Observer<List<NewsEntity>> mLocalNewsObserver;
    private RecyclerView mRecycler;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        if(mViewModel == null){
            initViewModel();
        }
        if(mViewModel.mSavedNewsList == null){
            loadNews();
            new Thread(new ExampleRunnable(),"MyDesignedThread").start();

        }
        else {
            setNewsListObserver();
        }

    }

    private void setNewsListObserver() {

        Log.d(TAG, "setNewsListObserver: called");

        mLocalNewsObserver = newsEntities -> {
            Log.d(TAG, "onChanged: response size from db : "+newsEntities.size());
            mSavedNews.clear();
            mSavedNews.addAll(newsEntities);
            if(mAdapter == null){
                initAdapterProperty();
            }
            if(mSavedNews.size()>0){
                mSavedNews.clear();
            }
            mSavedNews.addAll(newsEntities);
            mAdapter.setNews(mSavedNews);
            mAdapter.notifyDataSetChanged();
        };
        if(mViewModel == null){
            initViewModel();
        }
        mViewModel.mSavedNewsList.observe(getViewLifecycleOwner(),mLocalNewsObserver);
    }

    private void init(){
        mSearch = mView.findViewById(R.id.favoriteSearchViewId);
        mSearch.setOnClickListener(v -> mSearch.setIconified(false));
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        initAdapterProperty();
    }
    private void initAdapterProperty(){
        mSavedNews = new ArrayList<>();
        mAdapter = new FavoriteNewsAdapter();
        mAdapter.setNews(mSavedNews);
        mAdapter.setContext(requireContext());
        mAdapter.setFragment(this);
        mAdapter.setClickInterface(new FavoriteNewsAdapter.FavoriteNewsAdapterClickInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(requireContext(), NewsActivity.class);
                HomeFragment.mClickNews = mAdapter.getNews(position);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        mRecycler = mView.findViewById(R.id.favoriteRecyclerViewId);
        mRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecycler.setAdapter(mAdapter);
    }

    public void removeNews(NewsEntity news){
        mViewModel.deleteNews(news);
    }
    public void loadNews(){
        mViewModel.getSavedNewsList();
    }
    private void initViewModel(){
        mViewModel = new ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LocalNewsViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
        try{
            mViewModel.mSavedNewsList.removeObservers(getViewLifecycleOwner());
        }
        catch (Exception e){

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        //mViewModel.deleteNews(mAdapter.getUnsavedNewsList());
    }


    class ExampleRunnable implements Runnable{

        @Override
        public void run() {
            Log.d(TAG, "run: called");
            if(mViewModel.mSavedNewsList == null){
                Log.d(TAG, "run: before loop mViewModel.mSavedNewsList == null");
            }
            else{
                Log.d(TAG, "run: before loop mViewModel.mSavedNewsList != null");
            }
            while (mViewModel.mSavedNewsList == null){
                if(mViewModel.mSavedNewsList == null){
                    Log.d(TAG, "run loop: mViewModel.mSavedNewsList == null");
                }
                else{
                    Log.d(TAG, "run: loop: mViewModel.mSavedNewsList != null");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mViewModel.mSavedNewsList == null){
                            Log.d(TAG, "run: after loop mViewModel.mSavedNewsList == null");
                        }
                        else{
                            Log.d(TAG, "run: after loop mViewModel.mSavedNewsList != null");
                        }
                        setNewsListObserver();
                    }
                });
            }
            catch (Exception e){
                Log.e(TAG, "run: ", e);
            }
        }
    }

}