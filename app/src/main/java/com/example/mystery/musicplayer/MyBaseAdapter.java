package com.example.mystery.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mystery on 2017/12/19.
 */

public class MyBaseAdapter extends BaseAdapter {
    private List<SongFromInternet> songList;
    private LayoutInflater layoutInflater;
    private Context context;
    // private boolean result;
    // private HistorySQLOpenHelper historySQLOpenHelper;
    public MyBaseAdapter(Context context,List<SongFromInternet> songList){
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
        album.setText("  " + songList.get(position).getType());

        songName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyBaseAdapter", "MyBaseAdapter OnClick");
                Log.i("insert to database",  songList.get(position).getSongName() + ", " +  songList.get(position).getSinger() + ", " +  songList.get(position).getPath());

                // 查询要播放的歌曲是否已经保存到数据库中
                if (!findSongHistory(songList.get(position).getPath())) {
                    HistorySQLOpenHelper historySQLOpenHelper = new HistorySQLOpenHelper(context);
                    SQLiteDatabase db = historySQLOpenHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("song_name",songList.get(position).getSongName() );
                    values.put("singer", songList.get(position).getSinger());
                    values.put("path", songList.get(position).getPath());
                    db.insert("play_history", null, values);
                    db.close();
                }
                //findSong();
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
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private boolean findSongHistory(String path) {
        // 查询一首歌曲的信息
        HistorySQLOpenHelper  historySQLOpenHelper = new HistorySQLOpenHelper(context);
        SQLiteDatabase db = historySQLOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("play_history", null, "path=?", new String[]{path}, null, null, null);
        // 是否有下一条值
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }


}
