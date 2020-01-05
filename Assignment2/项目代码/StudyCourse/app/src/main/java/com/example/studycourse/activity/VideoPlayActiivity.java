package com.example.studycourse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.studycourse.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayActiivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private JCVideoPlayerStandard jcVideoPlayerStandard;

    String imageUrl="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578133533988&di=d815f628421ccc1f925c542b74ff6718&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F08%2F21%2F1057b91163eff67.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_actiivity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");

        jcVideoPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        //添加视频缩略图
        ImageView thumbImageView = jcVideoPlayerStandard.thumbImageView;
        //使用Glide添加
        Glide.with(this).load(imageUrl).into(thumbImageView );
        //配置jiecaovideoplayer
        jcVideoPlayerStandard.setUp(url,jcVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"视频标题");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()){
            return;
        }
        super.onBackPressed();
    }
}
