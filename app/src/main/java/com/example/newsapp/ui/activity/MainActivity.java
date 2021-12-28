package com.example.newsapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.newsapp.R;
import com.example.newsapp.ui.fragment.FavoriteFragment;
import com.example.newsapp.ui.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTAG:Main";
    private static final String FRAGMENT_HOME = "HOME";
    private static final String FRAGMENT_OTHER = "SAVED";
    private NavController mNavController;
    private BottomNavigationView mBottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mBottomNavigationView = findViewById(R.id.bottomNavigationId);
        mNavController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mBottomNavigationView,mNavController);

        /*mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favoriteFragment:
                        viewFragment(new HomeFragment(), FRAGMENT_HOME);
                        return true;
                }
                return false;
            }
        });*/
    }

    public void switchToFragmentHome() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.homeFragment, new HomeFragment()).commit();
    }
    public void switchToFavoriteHome() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.favoriteFragment, new FavoriteFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d(TAG, "onBackPressed: called");
        int selectedItemId = mBottomNavigationView.getSelectedItemId();
        Log.d(TAG, "onBackPressed: selected item : "+selectedItemId);
        Log.d(TAG, "onBackPressed: home item : "+R.id.homeFragment);
        Log.d(TAG, "onBackPressed: saved item : "+R.id.favoriteFragment);
        if (R.id.homeFragment != selectedItemId) {
            setHomeItem(MainActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public static void setHomeItem(Activity activity) {
        Log.d(TAG, "setHomeItem: home");
        BottomNavigationView bottomNavigationView =
                activity.findViewById(R.id.bottomNavigationId);
        bottomNavigationView.setSelectedItemId(R.id.homeFragment);


        int selectedItemId = bottomNavigationView.getSelectedItemId();
        MenuItem selectedItem = bottomNavigationView.getMenu().findItem(selectedItemId);
    }
}