package com.example.newsapp.ui.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.CustomNewsAdapter;
import com.example.newsapp.adapter.FilterCategoryAdapter;
import com.example.newsapp.adapter.FilterCountryAdapter;
import com.example.newsapp.adapter.FilterLanguageAdapter;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.model.CustomNews;
import com.example.newsapp.model.FilterData;
import com.example.newsapp.service.MyIntentService;
import com.example.newsapp.ui.activity.NewsActivity;
import com.example.newsapp.utils.NetworkHelper;
import com.example.newsapp.utils.QueryMaker;
import com.example.newsapp.viewmodel.APINewsViewModel;
import com.example.newsapp.viewmodel.LocalNewsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class HomeFragment extends Fragment implements CustomNewsAdapter.CustomNewsAdapterClickInterface {
    private static final String SHARED_PREFERENCE_FILTER_KEY = "100000";
    public static final String FILTER_CATEGORIES_KEY = "100001";
    public static final String FILTER_COUNTRIES_KEY = "100002";
    public static final String FILTER_LANGUAGES_KEY = "100003";
    public static final String FILTER_QUERIES_KEY = "100004";
    public static final String FILTER_MAIN_QUERY_KEY = "100005";

    public static String QUERY = "news?apikey=pub_2320f0868f525d0141226a957169cc43ec65&category=sports,health&q=madrid&country=gb";
    private static final String TAG = "MyTAG:HOME";
    public static final String INTENT_SERVICE_CLASS_BASE_URL = "base url";

    public static String SAVED_QUERY = "";

    public static boolean QUERY_RUN = false;
    public static boolean FIRST_RUN = true;
    private Map<String, String> mFilterMap;
    public static Map<String, Boolean> mSavedMap;

    private static List<CustomNews> mNewsList;
    private RecyclerView mRecyclerView;
    private APINewsViewModel mViewModel;
    private CustomNewsAdapter mAdapter;
    private Observer<List<CustomNews>> mObserver = null;
    private Intent mService;
    private LocalNewsViewModel mLocalVM;
    private TextView urlTextView;
    private InternetCheckerThread mInternetCheckerThread;
    private TextView queryTextView;
    private ConstraintLayout mLayout;

    public View mView;
    private SearchView mSearchView;
    private ImageView mFilterImageView;
    private GifImageView mLoadingGif;
    public static CustomNews mClickNews;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: called");
            String message = (String) intent.getSerializableExtra(MyIntentService.SERVICE_PAYLOAD);
            Log.d(TAG, "onReceive: " + message);
            if (FIRST_RUN) {
                setObserver();
                FIRST_RUN = false;
            }
        }
    };

    /*ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            // network available
        }

        @Override
        public void onLost(Network network) {
            // network unavailable
        }
    };*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: called");
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: called");
        init();
        new Thread(new ExampleRunnable()).start();
        if (mViewModel == null) {
            initViewModel();
        }
        if (mViewModel.mNewsLiveData == null) {
            mLoadingGif.setVisibility(View.VISIBLE);
            FIRST_RUN = true;
            apiCall();
        } else {
            setObserver();
        }

        //initAdapterProperty();
    }

    private void call() {
        if (mInternetCheckerThread == null) {
            mInternetCheckerThread = new InternetCheckerThread();
            mInternetCheckerThread.setContext(requireContext());
            mInternetCheckerThread.start();
            Message msg = Message.obtain();
            msg.arg1 = 3;
            mInternetCheckerThread.getMainHandler().sendMessage(msg);
        }

    }

    private void apiCall() {
        mFilterMap = getStoredFilterPattern();
        SAVED_QUERY = mFilterMap.get(FILTER_MAIN_QUERY_KEY);
        run(SAVED_QUERY);
    }

    public void setObserver() {
        Log.d(TAG, "storeLoadData: called");
        mObserver = customNews -> setLoadData(customNews);
        mViewModel.mNewsLiveData.observe(getViewLifecycleOwner(), mObserver);

    }

    private void setLoadData(List<CustomNews> customNews) {
        Log.d(TAG, "setLoadData: called");
        if (mAdapter == null || mNewsList == null) {
            initAdapterProperty();
        }
        int start = mNewsList.size();
        int count = customNews.size();
        Log.d(TAG, "setLoadData: mNewsList size : " + mNewsList.size());
        Log.d(TAG, "setLoadData: customNews size : " + customNews.size());
        if (customNews.size() == 10 && QUERY_RUN) {
            Log.d(TAG, "setLoadData: first run ");
            mNewsList.addAll(setSelected(customNews));
            mAdapter.setNews(mNewsList);
            mAdapter.notifyItemRangeInserted(start, count);
            smoothScroll(start);
            QUERY_RUN = false;
        } else {
            Log.d(TAG, "setLoadData: saved data");
            mNewsList.addAll(setSelected(customNews));
            Log.d(TAG, "setLoadData: saved data adapter size : " + mNewsList.size());
            mAdapter.setNews(mNewsList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            scroll(CustomNewsAdapter.SCROLL_POSITION);
        }
        mLayout.setBackgroundColor(getResources().getColor(R.color.fuchsia));
        mLoadingGif.setVisibility(View.GONE);
        Log.d(TAG, "setLoadData: size is " + customNews.size());
    }

    private List<CustomNews> setSelected(List<CustomNews> customNews) {
        for (CustomNews news : customNews) {
            try {
                Log.d(TAG, "setSelected: "+mSavedMap.get(news.getPubDate()));
                news.setSaved(mSavedMap.get(news.getPubDate()));
            } catch (NullPointerException e) {

            } finally {
            }
        }
        return customNews;
    }

    private void init() {
        Log.d(TAG, "init: called");
        mLayout = mView.findViewById(R.id.rootHome);
        mRecyclerView = mView.findViewById(R.id.homeRecyclerViewId);
        mSearchView = mView.findViewById(R.id.newsSearchViewId);
        mFilterImageView = mView.findViewById(R.id.filterImageViewId);
        urlTextView = mView.findViewById(R.id.finalQueryHomeTextView);
        queryTextView = mView.findViewById(R.id.initialQueryHomeTextView);
        mLoadingGif = mView.findViewById(R.id.homeLoadingGif);
        mLoadingGif.setVisibility(View.GONE);
        mSearchView.setOnClickListener(v -> mSearchView.setIconified(false));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        mFilterImageView.setOnClickListener(v -> {
            mFilterImageView.setColorFilter(0x686B68, PorterDuff.Mode.DARKEN);
            filterDialog();
        });
    }

    private void initAdapterProperty() {
        Log.d(TAG, "initAdapterProperty: called");
        initLocalVM();
        mNewsList = new ArrayList<>();
        mAdapter = new CustomNewsAdapter();
        mAdapter.setNews(mNewsList);
        mAdapter.setViewModel(mLocalVM);
        mAdapter.setContext(requireContext());
        mAdapter.setFragment(this);
        mAdapter.setClickInterface(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initViewModel() {
        Log.d(TAG, "initViewModel: called");
        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(APINewsViewModel.class);
    }

    public void run(String query) {
        Log.d(TAG, "run: called");
        if (NetworkHelper.isNetworkAvailable(requireActivity().getApplication())) {
            /*mInternetCheckerThread.quit();
            mInternetCheckerThread = null;*/
            Log.d(TAG, "run: internet is available");
            QUERY_RUN = true;
            Toast.makeText(requireContext(), "Loading data", Toast.LENGTH_SHORT).show();
            if (mService == null) {
                mService = new Intent(getContext(), MyIntentService.class);
            }
            Log.d(TAG, "run: hash code" + mViewModel.hashCode());
            mService.setData(Uri.parse(query));
            requireActivity().startService(mService);
        } else {
            Toast.makeText(requireContext(), "Internet not available", Toast.LENGTH_SHORT).show();
            if(mLoadingGif.getVisibility() == View.VISIBLE){
                mLoadingGif.setVisibility(View.GONE);
            }
        }
    }

    private void filterDialog() {
        Log.d(TAG, "filterDialog: filter dialog");

        AlertDialog.Builder dialog = new AlertDialog.Builder(requireView().getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.filter_layout, null);
        RecyclerView categoryRecycler = customLayout.findViewById(R.id.filterCategoryRecyclerViewId);
        RecyclerView countryRecycler = customLayout.findViewById(R.id.filterCountryRecyclerViewId);
        RecyclerView languageRecycler = customLayout.findViewById(R.id.filterLanguageRecyclerViewId);
        final TextView submitText = customLayout.findViewById(R.id.filterSubmitTextId);
        final TextView cancelText = customLayout.findViewById(R.id.filterCancelTextId);
        EditText queryEditText = customLayout.findViewById(R.id.filterKeywordEditTextId);
        mFilterMap = getStoredFilterPattern();
        FilterCategoryAdapter categoryAdapter = new FilterCategoryAdapter(requireContext(), mFilterMap.get(FILTER_CATEGORIES_KEY));
        FilterCountryAdapter countryAdapter = new FilterCountryAdapter(requireContext(), mFilterMap.get(FILTER_COUNTRIES_KEY));
        FilterLanguageAdapter languageAdapter = new FilterLanguageAdapter(requireContext(), mFilterMap.get(FILTER_LANGUAGES_KEY));

        queryEditText.setText(mFilterMap.get(FILTER_QUERIES_KEY).trim().replace("%20", " "));

        categoryRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecycler.setAdapter(categoryAdapter);
        countryRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        countryRecycler.setAdapter(countryAdapter);
        languageRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        languageRecycler.setAdapter(languageAdapter);

        dialog.setView(customLayout);
        AlertDialog alertDialog = dialog.create();
        submitText.setOnClickListener(v -> {
            alertDialog.dismiss();
            storeFilterPatter(categoryAdapter.getSelectedData(), countryAdapter.getSelectedData(),
                    languageAdapter.getSelectedData(), queryEditText.getText().toString().trim());
            setDataToTextView();
            mFilterImageView.setColorFilter(0x30272D, PorterDuff.Mode.DARKEN);
            apiCall();
        });
        cancelText.setOnClickListener(v -> {
            alertDialog.dismiss();
            mFilterImageView.setColorFilter(0x30272D, PorterDuff.Mode.DARKEN);
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private Map<String, String> getStoredFilterPattern() {
        Map<String, String> filterMap = new HashMap<>();
        Log.d(TAG, "getStoredFilterPattern: called");
        Context context = requireActivity();
        SharedPreferences sh = context.getSharedPreferences(SHARED_PREFERENCE_FILTER_KEY, Context.MODE_PRIVATE);

        filterMap.put(FILTER_CATEGORIES_KEY, sh.getString(FILTER_CATEGORIES_KEY, ""));
        filterMap.put(FILTER_QUERIES_KEY, sh.getString(FILTER_QUERIES_KEY, ""));
        filterMap.put(FILTER_COUNTRIES_KEY, sh.getString(FILTER_COUNTRIES_KEY, ""));
        filterMap.put(FILTER_LANGUAGES_KEY, sh.getString(FILTER_LANGUAGES_KEY, ""));
        filterMap.put(FILTER_MAIN_QUERY_KEY, sh.getString(FILTER_MAIN_QUERY_KEY, QUERY));

        return filterMap;
    }

    private void storeFilterPatter(List<FilterData> selectedCategories, List<FilterData> selectedCountries,
                                   List<FilterData> selectedLanguages, String query) {
        Log.d(TAG, "storeFilterPatter: called");
        Context context = requireActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILTER_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        Map<String, String> filterMap =
                QueryMaker.makeSpecificQuery(selectedCategories, selectedCountries, selectedLanguages, query);

        try {
            myEdit.putString(FILTER_LANGUAGES_KEY, filterMap.get(FILTER_LANGUAGES_KEY));
        } catch (Exception e) {
            myEdit.putString(FILTER_LANGUAGES_KEY, filterMap.get(""));
            Log.e(TAG, "storeFilterPatter: ", e);

        }
        try {
            myEdit.putString(FILTER_CATEGORIES_KEY, filterMap.get(FILTER_CATEGORIES_KEY));
        } catch (Exception e) {
            myEdit.putString(FILTER_CATEGORIES_KEY, filterMap.get(""));
            Log.e(TAG, "storeFilterPatter: ", e);
        }
        try {
            myEdit.putString(FILTER_COUNTRIES_KEY, filterMap.get(FILTER_COUNTRIES_KEY));
        } catch (Exception e) {
            myEdit.putString(FILTER_COUNTRIES_KEY, filterMap.get(""));
            Log.e(TAG, "storeFilterPatter: ", e);
        }
        try {
            myEdit.putString(FILTER_QUERIES_KEY, filterMap.get(FILTER_QUERIES_KEY));
        } catch (Exception e) {
            myEdit.putString(FILTER_QUERIES_KEY, filterMap.get(""));
            Log.e(TAG, "storeFilterPatter: ", e);
        }
        try {
            myEdit.putString(FILTER_MAIN_QUERY_KEY,
                    QueryMaker.makeQuery(selectedCategories, selectedCountries, selectedLanguages, query));
        } catch (Exception e) {
            myEdit.putString(FILTER_QUERIES_KEY, filterMap.get(""));
            Log.e(TAG, "storeFilterPatter: ", e);
        }

        myEdit.apply();
    }

    private void setDataToTextView() {
        Map<String, String> filtererMap = getStoredFilterPattern();
        urlTextView.setText(filtererMap.get(FILTER_MAIN_QUERY_KEY));
        queryTextView.setText(String.format("Language: %s", filtererMap.get(FILTER_LANGUAGES_KEY)));
        queryTextView.append(String.format("\nCountries: %s", filtererMap.get(FILTER_COUNTRIES_KEY)));
        queryTextView.append(String.format("\nCategories: %s", filtererMap.get(FILTER_CATEGORIES_KEY)));
        queryTextView.append(String.format("\nQuery: %s", filtererMap.get(FILTER_QUERIES_KEY)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");

        //mNewsList.clear();
        //mViewModel.mNewsLiveData.removeObservers();
        //LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mReceiver);

        if (mService != null) {
            requireActivity().stopService(mService);
            mService = null;
        }

    }

    private void initLocalVM() {
        mLocalVM = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LocalNewsViewModel.class);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), NewsActivity.class);
        mClickNews = mAdapter.getNews(position);
        startActivity(intent);
        Log.d(TAG, "onItemClick: ");
    }

    @Override
    public void onLongItemClick(int position) {
        Log.d(TAG, "onLongItemClick: " + mNewsList.get(position));
    }

    private void smoothScroll(final int position) {
        Log.d(TAG, "smoothScroll: called");
        mRecyclerView.post(() -> mRecyclerView.smoothScrollToPosition(position));
    }

    private void scroll(final int position) {
        Log.d(TAG, "scroll: called");
        mRecyclerView.post(() -> mRecyclerView.scrollToPosition(position));
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mReceiver, new IntentFilter(MyIntentService.SERVICE_MESSAGE));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: called");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: called");
        try {
            mViewModel.setAllNews(mAdapter.getNews());
        } catch (Exception e) {
            Log.e(TAG, "onDestroyView: ", e);
        }
        /*mRecyclerView.setAdapter(null);
        mAdapter = null;
        mRecyclerView = null;*/
        try {
            System.gc();
        } catch (Exception e) {
            Log.e(TAG, "onDestroyView: ", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called");
    }

    private void setSavedMap(LocalNewsViewModel lvm) {
        mSavedMap = new HashMap<>();
        Observer<List<NewsEntity>> localNewsObserver = newsEntities -> {
            Log.d(TAG, "setSavedMap: response size from db : " + newsEntities.size());
            mSavedMap.clear();
            for (NewsEntity entity : newsEntities) {
                mSavedMap.put(entity.getPubDate(), true);
            }
        };

        lvm.mSavedNewsList.observe(getViewLifecycleOwner(), localNewsObserver);
    }

    class ExampleRunnable implements Runnable {

        @Override
        public void run() {
            Log.d(TAG, "run: called");
            LocalNewsViewModel lvm = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                    .get(LocalNewsViewModel.class);
            lvm.getSavedNewsList();
            while (lvm.mSavedNewsList == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                getActivity().runOnUiThread(() -> {
                    setSavedMap(lvm);
                });
            } catch (Exception e) {
                Log.e(TAG, "run: ", e);
            }
        }
    }

    class InternetCheckerThread extends HandlerThread {
        private InternetHandler mainHandler;
        private static final String TAG = "TAG:Handler";
        private Context context;

        public void setContext(Context context) {
            this.context = context;
        }

        public InternetCheckerThread() {
            super("HandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
            // thread priority is start from -20 to 20
            // lesser the value the higher the thread priority is
        }

        @Override
        protected void onLooperPrepared() {
            Log.d(TAG, "onLooperPrepared: called");
            mainHandler = new InternetHandler();
        }

        public Handler getMainHandler() {
            Log.d(TAG, "getMainHandler: called");
            return mainHandler;
        }

        class InternetHandler extends Handler {

            @Override
            public void handleMessage(@NonNull Message msg) {
                for (int i = 0; i < msg.arg1; i++) {
                    SystemClock.sleep(1000);
                }
                try {
                    boolean available = NetworkHelper.isNetworkAvailable(requireActivity().getApplication());
                    while (!available) {
                        SystemClock.sleep(300);
                        available = NetworkHelper.isNetworkAvailable(requireActivity().getApplication());
                    }
                    apiCall();
                } catch (Exception e) {

                }
            }
        }
    }

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkConnectivity() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }


    }*/
}

/*

 */