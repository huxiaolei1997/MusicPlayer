package com.example.mystery.musicplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends Activity{
    private List<Song> localSongList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // OnlineMusicFragment onlineMusicFragment = new OnlineMusicFragment();
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentManager fragmentManager  = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main, homeFragment);
                    fragmentTransaction.commit();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(getApplicationContext(), localSongList);
                                    ListView listView = (ListView) findViewById(R.id.lv_local_music);
                                    listView.setAdapter(localMusicAdapter);
                                }
                            });
                        }
                    }, 100);
                    return true;
                case R.id.navigation_onlinemusic:
                    OnlineMusicFragment onlineMusicFragment = new OnlineMusicFragment();
                    FragmentManager fragmentManager1  = getFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.main, onlineMusicFragment);
                    fragmentTransaction1.commit();

                    return true;
                case R.id.navigation_search:
                    SearchFragment searchFragment = new SearchFragment();
                    FragmentManager fragmentManager2  = getFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.main, searchFragment);
                    fragmentTransaction2.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localSongList = FindLocalMusic.getLocalMusic(getApplicationContext());
        Log.i("localSongList", localSongList.toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(getApplicationContext(), localSongList);
                        ListView listView = (ListView) findViewById(R.id.lv_local_music);
                        listView.setAdapter(localMusicAdapter);
                    }
                });
            }
        }, 100);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager  = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main, homeFragment);
        fragmentTransaction.commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
