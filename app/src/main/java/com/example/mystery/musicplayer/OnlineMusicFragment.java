package com.example.mystery.musicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mystery on 2017/12/10.
 */

public class OnlineMusicFragment extends Fragment {
    // 用来存放不同类型音乐的list
    private List<SongFromInternet> listBySort;
    // 英文
    private TextView tv_english;
    // 流行音乐
    private TextView tv_pop;
    // 民谣
    private TextView tv_ballad;
    // 轻音乐
    private TextView tv_light_music;
    // 经典音乐
    private TextView tv_classic;
    // 古典音乐
    private TextView tv_classical;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.online_music, container, false);
        tv_english = ((TextView) view.findViewById(R.id.english));
        tv_ballad = ((TextView) view.findViewById(R.id.ballad));
        tv_classic = ((TextView) view.findViewById(R.id.classic));
        tv_classical = ((TextView) view.findViewById(R.id.classical));
        tv_light_music = ((TextView) view.findViewById(R.id.light_music));
        tv_pop = ((TextView) view.findViewById(R.id.pop));

        // 点击获取所有的英文歌曲
        tv_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sort = "english";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtils.getSongBySort(sort);
                        findBySort(result);
                        Log.i("resultenglish", result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getContext(), listBySort);
                                ListView listView = (ListView) view.findViewById(R.id.lv_sort);
                                listView.setAdapter(myBaseAdapter);
                            }
                        });
                    }
                });
                thread.start();
            }
        });

        // 点击获取所有的流行音乐
        tv_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sort = "pop";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtils.getSongBySort(sort);
                        findBySort(result);
                        Log.i("resultpop", result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getContext(), listBySort);
                                ListView listView = (ListView) view.findViewById(R.id.lv_sort);
                                listView.setAdapter(myBaseAdapter);
                            }
                        });
                    }
                });
                thread.start();

            }
        });

        // 点击获取所有的轻音乐
        tv_light_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sort = "lightmusic";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtils.getSongBySort(sort);
                        findBySort(result);
                        Log.i("resultlightmusic", result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getContext(), listBySort);
                                ListView listView = (ListView) view.findViewById(R.id.lv_sort);
                                listView.setAdapter(myBaseAdapter);
                            }
                        });
                    }
                });
                thread.start();

            }
        });

        // 点击获取所有的民谣
        tv_ballad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sort = "ballad";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtils.getSongBySort(sort);
                        findBySort(result);
                        Log.i("resultballad", result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getContext(), listBySort);
                                ListView listView = (ListView) view.findViewById(R.id.lv_sort);
                                listView.setAdapter(myBaseAdapter);
                            }
                        });
                    }
                });
                thread.start();

            }
        });

        // 点击获取所有的古典音乐
        tv_classical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sort = "classical";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtils.getSongBySort(sort);
                        findBySort(result);
                        Log.i("resultclassical", result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getContext(), listBySort);
                                ListView listView = (ListView) view.findViewById(R.id.lv_sort);
                                listView.setAdapter(myBaseAdapter);
                            }
                        });
                    }
                });
                thread.start();

            }
        });

        // 点击获取所有的经典歌曲
        tv_classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sort = "classic";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtils.getSongBySort(sort);
                        findBySort(result);
                        Log.i("resultclassic", result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getContext(), listBySort);
                                ListView listView = (ListView) view.findViewById(R.id.lv_sort);
                                listView.setAdapter(myBaseAdapter);
                            }
                        });
                    }
                });
                thread.start();

            }
        });

        return view;
    }

    // 根据分类找出歌曲
    public void findBySort(String result) {
        JSONArray jsonArray = JSON.parseArray(result);
        JSONObject jsonObject = null;
        listBySort = new ArrayList<SongFromInternet>();

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
            listBySort.add(songFromInternet);
        }
    }
}
