package com.example.mystery.musicplayer;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mystery on 2017/12/10.
 */

public class HomeFragment extends Fragment {
    private List<Song> localSongList;
    private TextView tv_local_music;
    private TextView tv_history;
    private List<PlayHistory> listOfHistory;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i("HomeFragment1", "HomeFragment1");
        final View view = inflater.inflate(R.layout.home, container, false);
        // 点击获取本地音乐并显示在我的界面里面
        tv_local_music = ((TextView) view.findViewById(R.id.localmusic));
        // 点击获取播放历史并显示在我的界面里面
        tv_history = ((TextView) view.findViewById(R.id.tv_history));

        tv_local_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 获取本地歌曲列表
                            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
                            localSongList = FindLocalMusic.getLocalMusic(getContext());
                            Log.i("localSongList", localSongList.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(getContext(), localSongList);
                                    ListView listView = (ListView) view.findViewById(R.id.lv_local_music);
                                    listView.setAdapter(localMusicAdapter);
                                }
                            });
                        }
                    });
                    thread.start();
            }
        });

        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 获取播放历史
                        listOfHistory = findSong();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PlayHistoryAdapter playHistoryAdapter = new PlayHistoryAdapter(getContext(), listOfHistory);
                                ListView listView = (ListView) view.findViewById(R.id.lv_local_music);
                                listView.setAdapter(playHistoryAdapter);
                            }
                        });
                    }
                });
                thread.start();
            }
        });
        return view;
    }


    private List<PlayHistory> findSong() {
        Log.i("HomeFragment", "findSong()");
        List<PlayHistory> listOfHistory = new ArrayList<PlayHistory>();
        HistorySQLOpenHelper  historySQLOpenHelper = new HistorySQLOpenHelper(getContext());
        SQLiteDatabase db = historySQLOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("play_history", null, null, null, null, null, "id desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PlayHistory playHistory = new PlayHistory();
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String songName = cursor.getString(cursor.getColumnIndex("song_name"));
                String singer = cursor.getString(cursor.getColumnIndex("singer"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                playHistory.setId(id);
                playHistory.setSongName(songName);
                playHistory.setSinger(singer);
                playHistory.setPath(path);
                listOfHistory.add(playHistory);
            }
        }
        cursor.close();
        db.close();
        Log.i("获取数据库的播放歌曲历史：", listOfHistory.toString());
        return listOfHistory;
    }
}
