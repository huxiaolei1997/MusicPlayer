package com.example.mystery.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mystery on 2017/12/26.
 */

public class PlayHistoryAdapter extends BaseAdapter {
    private List<PlayHistory> songList;
    private LayoutInflater layoutInflater;
    private Context context;

    public PlayHistoryAdapter(Context context,List<PlayHistory> songList){
        this.context = context;
        this.songList = songList;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_view, null);
        final TextView songName = (TextView) convertView.findViewById(R.id.song_name);
        TextView singer = (TextView) convertView.findViewById(R.id.singer);
        TextView album = (TextView) convertView.findViewById(R.id.album);

        songName.setText((position + 1) + ". " + songList.get(position).getSongName());
        singer.setText("  " + songList.get(position).getSinger());
        if (songList.get(position).getPath().substring(0, 4).equals("http")) {
            album.setText("  " + "在线音乐");
        } else {
            album.setText("  " + "本地音乐");
        }


        songName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PlayHistoryAdapter", "PlayHistoryAdapter");
                String path = songList.get(position).getPath();
                Intent intent = new Intent();
                intent.setClass(context, PlayActivity.class);

                // 向PlayActivy中传递播放路径，歌手，歌曲名，歌曲id，歌曲总数信息
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                bundle.putInt("totalSongs", songList.size());
                intent.putExtra("path", path);
                intent.putExtra("singer", songList.get(position).getSinger());
                intent.putExtra("songName", songList.get(position).getSongName());
                intent.putExtra("flag", "history");
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
