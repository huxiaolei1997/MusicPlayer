package com.example.mystery.musicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Mystery on 2017/12/10.
 */

public class SearchFragment extends Fragment {
    private List<SongFromInternet> songFromInternetList;
    //private TextView tv_songName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final  View view = inflater.inflate(R.layout.search, container, false);
        SearchView mSearchView = ((SearchView) view.findViewById(R.id.searchSong));
        //tv_songName = ((TextView) view.findViewById(R.id.song_name));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                final String querySort = query;
                Log.i("sort",query);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtils.getSongBySort(querySort);
                        Log.i("result", result);
                        if (result.equals("[]")) {
                            System.out.println("resultresult");
                           // Log.i("查询的结果为空", "查询的结果为空");
                            Looper.prepare();
                            Toast.makeText(getActivity(), "查询结果为空", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else {
                            JSONArray jsonArray = JSON.parseArray(result);
                            JSONObject jsonObject = null;
                            songFromInternetList = new ArrayList<SongFromInternet>();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                jsonObject = JSON.parseObject(jsonArray.get(i).toString());
                                SongFromInternet songFromInternet = new SongFromInternet();
                                // System.out.println(jsonObject.toString());
                                // 无序遍历jsonObject
                                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {

                                    //System.out.println(entry.getKey() + ", " + entry.getValue());
                                    if (entry.getKey().equals("id")) {
                                        // Log.i("id", entry.getKey());
                                        songFromInternet.setId(Long.parseLong(entry.getValue().toString()));
                                    }
                                    if (entry.getKey().equals("singer")) {
                                        songFromInternet.setSinger(entry.getValue().toString());
                                    }
                                    if (entry.getKey().equals("songName")) {
                                        songFromInternet.setSongName(entry.getValue().toString());
                                    }
                                    if (entry.getKey().equals("type")) {
                                        songFromInternet.setType(entry.getValue().toString());
                                    }
                                    if (entry.getKey().equals("path")) {
                                        songFromInternet.setPath(entry.getValue().toString());
                                    }

                                }
                                songFromInternetList.add(songFromInternet);

                                //Iterator iterator = jsonObject;
                            }

                            Log.i("abc", songFromInternetList.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getContext(),songFromInternetList);
                                    ListView listView = (ListView) view.findViewById(R.id.lv);
                                    listView.setAdapter(myBaseAdapter);
                                }
                            });
                        }
                    }
                });
                thread.start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }
}
