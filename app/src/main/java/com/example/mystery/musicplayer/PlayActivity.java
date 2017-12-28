package com.example.mystery.musicplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mystery on 2017/12/23.
 */

public class PlayActivity extends Activity {
    private MusicOnlineService.MyBinder myBinder;
    private Intent intent;
    private MyConn conn;
    private Thread mThread;
    private SeekBar mSeekBar;
    private TextView songTime;
    private TextView now_play_time;
    private TextView tv_pause;
    private Drawable drawablePlay;
    // 播放路径
    private String path;
    // 歌曲名
    private String songName;
    // 歌手名
    private String singer;
    // 歌曲id
    private int id;
    // 歌曲总数
    private int totalSongs;
    // 用来判断当前是播放或者暂停状态，从而更新相应的播放图标或暂停图标
    boolean flag = true;
    // 本地歌曲
    private List<Song> localMusicList;
    // 历史歌曲
    private List<PlayHistory> playHistoryList;
    // flag，用来区分当前播放的本地歌曲还是历史歌曲
    private String localOrHistory;
    private TextView singer_songname;
    // private boolean seekBar_flag = false;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
                case 100:
                    int currentPosition = (Integer) msg.obj;
                    mSeekBar.setProgress(currentPosition);
                    now_play_time.setText("" + FindLocalMusic.formatTime(currentPosition));
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("onCreate", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
       // HistorySQLOpenHelper personSQLOpenHelper = new HistorySQLOpenHelper(getApplicationContext());
       // db = personSQLOpenHelper.getWritableDatabase();
        mSeekBar = ((SeekBar) findViewById(R.id.seekBar1));
        songTime = ((TextView) findViewById(R.id.song_time));
        now_play_time = ((TextView) findViewById(R.id.now_play_time));
        tv_pause = ((TextView) findViewById(R.id.bt_pause));
        drawablePlay = getResources().getDrawable(R.drawable.ic_media_play);
        drawablePlay.setBounds(0, 0, drawablePlay.getMinimumWidth(), drawablePlay.getMinimumHeight());

//        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                mThread.interrupt();
//                if (mThread.isInterrupted()) {
//                    myBinder.pauses();
//                }
//                seekBar_flag = true;
//                Log.i("进度条", "开始拖动进度条");
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.i("进度条", "进度条的状态改变了, " + progress);
//                if (seekBar_flag) {
//                    myBinder.setPosition(progress);
//
//                }
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.i("进度条", "停止拖动进度条");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        now_play_time.setText("" + FindLocalMusic.formatTime(myBinder.getCurrentPosition()));
//                        myBinder.plays(path);
//                        initSeekBar();
//                        UpdateProgress();
//                    }
//                });
//            }
//        });

        // 获取intent传来的值
        Intent it = getIntent();
        path = it.getStringExtra("path");
        songName = it.getStringExtra("songName");
        singer = it.getStringExtra("singer");
        localOrHistory = it.getStringExtra("flag");
        Bundle bundle = it.getExtras();
        id = bundle.getInt("id");
        totalSongs = bundle.getInt("totalSongs");
        conn = new MyConn();
        intent = new Intent(this, MusicOnlineService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新UI视图必须在UI线程里面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        singer_songname = ((TextView) findViewById(R.id.singer_songname));
                        singer_songname.setText(singer + "-" +songName);
                        //System.out.println("音乐时长是：" + FindLocalMusic.formatTime(myBinder.getMusicWidth()) + ", " + myBinder.getMusicWidth());
                        //songTime.setText(FindLocalMusic.formatTime(myBinder.getMusicWidth()));
                    }
                });
                myBinder.plays(path);
                songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
                // System.out.println("音乐时长是：" + FindLocalMusic.formatTime(myBinder.getMusicWidth()) + ", " + myBinder.getMusicWidth());
                initSeekBar();
                UpdateProgress();
                // Toast.makeText(PlayActivity.this, id + ", " + totalSongs, Toast.LENGTH_LONG).show();
            }
        }, 100);
    }



    private class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MusicOnlineService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void initSeekBar() {
        Log.i("调用initSeekBar之前", "调用initSeekBar之前");
        int musicWidth = myBinder.getMusicWidth();
        Log.i("获取music的长度", String.valueOf(musicWidth));
        mSeekBar.setMax(musicWidth);
        Log.i("调用initSeekBar之后", "调用initSeekBar之后");
    }

    // 更新音乐播放的进度
    private void UpdateProgress() {
        Log.i("调用UpdateProgress之前", "调用UpdateProgress之前");
        mThread = new Thread() {
            @Override
            public void run() {
                while(!interrupted()) {
                    int currentPosition = myBinder.getCurrentPosition();
//                    if (currentPosition == myBinder.getMusicWidth()) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // 设置图标为将要播放的状态
//                                tv_pause.setCompoundDrawables(null, drawablePlay, null, null);
//                                tv_pause.setText("播放");
//                                flag = false;
//                            }
//                        });
//                    }
                    Log.i("currentPosition", String.valueOf(currentPosition));
                    Message message = Message.obtain();
                    message.obj = currentPosition;
                    //message.arg1 = myBinder.getMusicWidth();
                    message.what = 100;
                    handler.sendMessage(message);
                }
            }
        };
        mThread.start();
        Log.i("调用UpdateProgress之后", "调用UpdateProgress之后");
    }

    @Override
    protected void onDestroy() {
        Log.i("PlayMainActivity", "onDestroy()");
        if (mThread != null && !mThread.isInterrupted()) {
            mThread.interrupt();
        }
        unbindService(conn);
        super.onDestroy();
    }

    // 开始播放音乐
    public void playMusicOnline(View view) {
        Log.i("PlayActivity", "开始播放音乐");
        Log.i("musicPath", path);
        if (path != null) {
            myBinder.plays(path);
            initSeekBar();
            UpdateProgress();
        } else {
            Toast.makeText(this, "没有选中要播放的歌曲", Toast.LENGTH_SHORT).show();
        }

    }

    // 暂停播放音乐
    public void pauseMusicOnline(View view) {
        Log.i("PlayActivity", "暂停播放音乐");
        // localMusicList = FindLocalMusic.getLocalMusic(getApplicationContext());
        // Log.i("本地所有歌曲",localMusicList.toString());
        if (flag) {
            // 设置图标为将要播放的状态
            Log.i("播放暂停", "111");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_pause.setCompoundDrawables(null, drawablePlay, null, null);
                    tv_pause.setText("播放");
                }
            });
            flag = false;
        } else {
            // 设置图标为将要暂停的状态
            Log.i("播放暂停", "222");
            final Drawable drawablePause = getResources().getDrawable(R.drawable.ic_media_pause);
            drawablePause.setBounds(0, 0, drawablePause.getMinimumWidth(), drawablePause.getMinimumHeight());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_pause.setCompoundDrawables(null, drawablePause, null, null);
                    tv_pause.setText("暂停");
                }
            });
            flag = true;
        }
        myBinder.pauses();
    }

    // 重新播放音乐
    public void replyMusicOnline(View view) {
        Log.i("PlayActivity", "重新播放音乐");
        myBinder.replays();
    }

    // 上一首
    public void playPrevious(View view) {
        Log.i("PlayActivity：", "上一首：" + id + ", " + totalSongs);
        // 判断list是否越界，如果越界则获取最后一首歌的播放路径，否则获取上一首歌的播放路径
        if (localOrHistory.equals("local")) {
            Log.i("localOrHistory", "localMusicList上一首");
            localMusicList = FindLocalMusic.getLocalMusic(getApplicationContext());
            if (id >= 1) {
                path = localMusicList.get(id - 1).getPath();
                singer = localMusicList.get(id - 1).getSinger();
                songName = localMusicList.get(id - 1).getSongName();

                mThread.interrupt();
                if (mThread.isInterrupted()) {
                    myBinder.stops();
                }

                singer_songname = ((TextView) findViewById(R.id.singer_songname));
                singer_songname.setText(singer + "-" + songName);

                myBinder.plays(path);
                songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
                initSeekBar();
                UpdateProgress();
                id = id - 1;
            } else {
                // 从末尾开始播放
                id = totalSongs;
                path = localMusicList.get(totalSongs - 1).getPath();
                singer = localMusicList.get(totalSongs - 1).getSinger();
                songName = localMusicList.get(totalSongs - 1).getSongName();

                mThread.interrupt();
                if (mThread.isInterrupted()) {
                    myBinder.stops();
                }

                singer_songname = ((TextView) findViewById(R.id.singer_songname));
                singer_songname.setText(singer + "-" + songName);

                myBinder.plays(path);
                songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
                initSeekBar();
                UpdateProgress();
            }

        } else if (localOrHistory.equals("history")){
            Log.i("localOrHistory", "playHistoryList上一首");
            playHistoryList = findSong();
            if (id >= 1) {
                path = playHistoryList.get(id - 1).getPath();
                singer = playHistoryList.get(id - 1).getSinger();
                songName = playHistoryList.get(id - 1).getSongName();

                mThread.interrupt();
                if (mThread.isInterrupted()) {
                    myBinder.stops();
                }

                singer_songname = ((TextView) findViewById(R.id.singer_songname));
                singer_songname.setText(singer + "-" + songName);

                myBinder.plays(path);
                songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
                initSeekBar();
                UpdateProgress();
                id = id - 1;
            } else {
                // 从末尾开始播放
                id = totalSongs;
                path = playHistoryList.get(totalSongs - 1).getPath();
                singer = playHistoryList.get(totalSongs - 1).getSinger();
                songName = playHistoryList.get(totalSongs - 1).getSongName();

                mThread.interrupt();
                if (mThread.isInterrupted()) {
                    myBinder.stops();
                }

                singer_songname = ((TextView) findViewById(R.id.singer_songname));
                singer_songname.setText(singer + "-" + songName);

                myBinder.plays(path);
                songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
                initSeekBar();
                UpdateProgress();
            }
        }

    }

    // 下一首
    public void playNext(View view) {
      Log.i("PlayActivity", "下一首：" + id + ", " + totalSongs);
      if(localOrHistory.equals("local")) {
          Log.i("localOrHistory", "localMusicList下一首");
          localMusicList = FindLocalMusic.getLocalMusic(getApplicationContext());
          // 判断list是否越界，如果越界则获取第一首歌的播放路径，否则获取下一首歌的播放路径
          if (id <= totalSongs - 2) {
              path = localMusicList.get(id + 1).getPath();
              singer = localMusicList.get(id + 1).getSinger();
              songName = localMusicList.get(id + 1).getSongName();

              mThread.interrupt();
              if (mThread.isInterrupted()) {
                  myBinder.stops();
              }

              singer_songname = ((TextView) findViewById(R.id.singer_songname));
              singer_songname.setText(singer + "-" + songName);

              myBinder.plays(path);
              songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
              initSeekBar();
              UpdateProgress();
              id = id + 1;
          } else {
              // 从头开始播放
              id = 0;
              path = localMusicList.get(0).getPath();
              singer = localMusicList.get(0).getSinger();
              songName = localMusicList.get(0).getSongName();

              mThread.interrupt();
              if (mThread.isInterrupted()) {
                  myBinder.stops();
              }

              singer_songname = ((TextView) findViewById(R.id.singer_songname));
              singer_songname.setText(singer + "-" + songName);

              myBinder.plays(path);
              songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
              initSeekBar();
              UpdateProgress();
          }
      } else if (localOrHistory.equals("history")) {
          Log.i("localOrHistory", "playHistoryList下一首");
          playHistoryList = findSong();
          // 判断list是否越界，如果越界则获取第一首歌的播放路径，否则获取下一首歌的播放路径
          if (id <= totalSongs - 2) {
              path = playHistoryList.get(id + 1).getPath();
              singer = playHistoryList.get(id + 1).getSinger();
              songName = playHistoryList.get(id + 1).getSongName();

              mThread.interrupt();
              if (mThread.isInterrupted()) {
                  myBinder.stops();
              }

              singer_songname = ((TextView) findViewById(R.id.singer_songname));
              singer_songname.setText(singer + "-" + songName);

              myBinder.plays(path);
              songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
              initSeekBar();
              UpdateProgress();
              id = id + 1;
          } else {
              // 从头开始播放
              id = 0;
              path = playHistoryList.get(0).getPath();
              singer = playHistoryList.get(0).getSinger();
              songName = playHistoryList.get(0).getSongName();

              mThread.interrupt();
              if (mThread.isInterrupted()) {
                  myBinder.stops();
              }

              singer_songname = ((TextView) findViewById(R.id.singer_songname));
              singer_songname.setText(singer + "-" + songName);

              myBinder.plays(path);
              songTime.setText("" + FindLocalMusic.formatTime(myBinder.getMusicWidth()));
              initSeekBar();
              UpdateProgress();
          }
      }


    }

    // 停止播放音乐
    public void stopMusicOnline(View view) {
        mThread.interrupt();
        if (mThread.isInterrupted()) {
            myBinder.stops();
        }
    }

    // 查找播放历史
    private List<PlayHistory> findSong() {
        Log.i("HomeFragment", "findSong()");
        List<PlayHistory> listOfHistory = new ArrayList<PlayHistory>();
        HistorySQLOpenHelper  historySQLOpenHelper = new HistorySQLOpenHelper(getApplicationContext());
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
