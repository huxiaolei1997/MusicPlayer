package com.example.mystery.musicplayer;

/**
 * 这个实体类是用来存放播放历史歌曲的信息的
 * Created by Mystery on 2017/12/26.
 */

public class PlayHistory {
    private long id;
    private String songName;
    private String singer;
    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "{" +
                "id : " + id +
                ", songName : '" + songName + '\'' +
                ", singer : '" + singer + '\'' +
                ", path : '" + path + '\'' +
                '}';
    }
}
