package com.example.mystery.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Mystery on 2017/12/23.
 */

public class MusicOnlineService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer mediaPlayer;
    private boolean flag = true;
    class MyBinder extends Binder {
        // 播放音乐
        public void plays(String path) {
            play(path);
        }

        // 暂停播放
        public void pauses() {
            pause();
        }

        // 重新播放
        public void replays() {
            replay();
        }

        // 停止播放
        public void stops() {
            stop();
        }

        // 获取当前的播放进度
        public int getCurrentPosition() {
            return getCurrentProgress();
        }
        // 获取音乐文件的长度
        public int getMusicWidth() {
            Log.i("getMusicWidth", String.valueOf(getMusicLength()));
            return getMusicLength();
        }

//        public void setPosition(int position) {
//            mediaPlayer.seekTo(position);
//        }
    }
    @Override
    public void onCreate() {
        Log.i("MusicService", "onCreate()");
        super.onCreate();
        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        am.setMicrophoneMute(true);
        // 插入耳机之后就不使用扬声器外放
        am.setSpeakerphoneOn(false);
    }


    public void play(String path) {
        try {
            if (mediaPlayer == null) {
                Log.i(TAG, "开始播放音乐");
                mediaPlayer = new MediaPlayer();
                if (path.substring(0, 4).equals("http")) {
                    mediaPlayer.setDataSource(MusicOnlineService.this, Uri.parse(path));
                } else {
                    // path = "/storage/emulated/0/storage/emulated/0/qqmusic/为爱痴狂-金志文.mp3";
                    File file = new File(path);
                    path = file.getAbsolutePath();
                    mediaPlayer.setDataSource(path);
                }
                mediaPlayer.prepare();
                Log.i("歌曲时长是：", String.valueOf(mediaPlayer.getDuration()));
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            } else {
                int position = getCurrentProgress();
                mediaPlayer.seekTo(position);
                try {
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 暂停音乐
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Log.i(TAG, "播放暂停");
            mediaPlayer.pause();
        } else if (mediaPlayer != null && (!mediaPlayer.isPlaying())) {
            mediaPlayer.start();
        }
    }

    // 重新播放音乐
    public void replay() {
        if (mediaPlayer != null) {
            Log.i(TAG, "重新开始播放");
            mediaPlayer.seekTo(0);
            try {
                mediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
    }

    // 停止音乐
    public void stop() {
        if (mediaPlayer != null) {
            Log.i(TAG, "停止播放");
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        } else {
            Toast.makeText(getApplicationContext(), "已停止", Toast.LENGTH_LONG).show();
        }
    }

    // 获取资源文件的长度
    public int getMusicLength() {
        Log.i("getMusicLength", "getMusicLength");
        if (mediaPlayer != null) {
            Log.i("getDuration", String.valueOf(mediaPlayer.getDuration()));
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    // 获取当前进度
    public int getCurrentProgress() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Log.i(TAG, "获取当期进度");
            int position = mediaPlayer.getCurrentPosition();
            Log.i(TAG, "当前进度是： " + position);
            return mediaPlayer.getCurrentPosition();
        } else if (mediaPlayer != null && (!mediaPlayer.isPlaying())) {
            return mediaPlayer.getCurrentPosition();
        }
        return  0;
    }

    @Override
    public void onDestroy() {
        Log.i("MusicService", "onDestroy()");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MusicService", "onBind()");
        return new MusicOnlineService.MyBinder();
    }


}
