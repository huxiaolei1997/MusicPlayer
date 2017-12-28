package com.example.mystery.musicplayer;

import java.io.Serializable;

/**
 * Created by Mystery on 2017/12/10.
 */

public class Song implements Serializable {
    // 歌手
    private String singer;
    // 歌曲名
    private String songName;
    // 歌曲路径
    private String path;
    // 歌曲时间长度
    private int duration;
    // 歌曲大小
    private long size;

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "{" +
                "singer : '" + singer + '\'' +
                ", songName : '" + songName + '\'' +
                ", path : '" + path + '\'' +
                ", duration : " + duration +
                ", size : " + size +
                '}';
    }
}
