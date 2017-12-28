package com.example.mystery.musicplayer;

/**
 * Created by Mystery on 2017/12/19.
 */

public class SongFromInternet {
    private long id;
    private String songName;
    private String singer;
    private String type;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "id : " + id +
                ", songName : '" + songName + '\'' +
                ", singer : '" + singer + '\'' +
                ", type : '" + type + '\'' +
                ", path : '" + path + '\'' +
                '}';
    }
}
