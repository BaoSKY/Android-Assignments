package com.example.studycourse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.studycourse.R;

import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer;//媒体播放器
    private Button playButton;
    private Button replayButton;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private SeekBar seekBar;
    private Timer timer;

    private Button audio_return;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");

        audio_return = (Button) findViewById(R.id.audio_return);

        mediaPlayer = new MediaPlayer();
        //监听滚动条事件
        seekBar = (SeekBar) findViewById(R.id.playSeekBar1);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());

        /*
        playButton= (Button) findViewById(R.id.playButton1);
        playButton.setOnClickListener(new PalyListener());

         */

        //监听[重播]事件
        replayButton= (Button) findViewById(R.id.replayButton1);

        replayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer.reset();
                currentPosition = 0;
                audioplay();
            }
        });


        //replayButton.setOnClickListener(this);

        audio_return.setOnClickListener(this);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.audio_return:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                finish();

            /*
            case R.id.playButton:
                mediaPlayer.reset();
                currentPosition = 0;
                audioplay();
            case R.id.replayButton:
                currentPosition = mediaPlayer.getCurrentPosition();//记录播放的位置
                mediaPlayer.stop();//暂停状态
                //playButton.setText("暂停");
                timer.purge();//移除所有任务;

             */
        }
    }




    private class PalyListener implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()){
                /*
                case R.id.playButton:
                    mediaPlayer.reset();
                    audioplay();

                 */
                case R.id.replayButton1:
                    currentPosition = mediaPlayer.getCurrentPosition();//记录播放的位置
                    mediaPlayer.stop();//暂停状态
                    //playButton.setText("暂停");
                    timer.purge();//移除所有任务;
            }
        }
    }


    /*播放处理*/
    private void audioplay() {
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
            //mediaPlayer.setDataSource("http://123.207.6.140:8080/audio/audio.mp3");//设置mp3数据源
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();//数据缓冲
            /*监听缓存 事件，在缓冲完毕后，开始播放*/
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.seekTo(currentPosition);
                    //playButton.setText("播放");
                    seekBar.setMax(mediaPlayer.getDuration());
                }
            });
            //监听播放时回调函数
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!isSeekBarChanging){
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            },0,50);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "播放错误", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }
        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }
}
