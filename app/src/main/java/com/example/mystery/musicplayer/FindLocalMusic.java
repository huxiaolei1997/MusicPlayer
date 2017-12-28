package com.example.mystery.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mystery on 2017/12/10.
 * 用来扫描本地音乐
 */

public class FindLocalMusic {
    public static List<Song> getLocalMusic(Context context) {
        Log.i("getLocalMusic", "getLocalMusic()正在运行");
        List<Song> musicList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setSongName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if (song.getSize() > 1000 * 800) {
                    if (song.getSongName().contains("-")) {
                        String[] str = song.getSongName().split("-");
                        song.setSinger(str[0]);
                        song.setSongName(str[1]);
                    }
                    musicList.add(song);
                }
            }
            cursor.close();
        }
        return musicList;
    }

    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return "0" + time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return "0" + time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }
}
