package com.example.studycourse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.studycourse.Adapter.RecyclerviewAdapter;
import com.example.studycourse.MainActivity;
import com.example.studycourse.R;
import com.example.studycourse.Util.Course;
import com.example.studycourse.Util.HttpUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseDetailsActivity extends AppCompatActivity {
    private TextView tv_course_id;
    private TextView tv_teacher;
    private TextView tv_school;
    private TextView tv_date;
    private TextView tv_description;
    private ImageView iv_course;

    private SensorManager sensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private JCVideoPlayerStandard jcVideoPlayerStandard;

    String s1="http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";
    String imageUrl="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578133533988&di=d815f628421ccc1f925c542b74ff6718&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F08%2F21%2F1057b91163eff67.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        tv_course_id=(TextView) findViewById(R.id.tv_course_id);
        tv_teacher=(TextView) findViewById(R.id.tv_teacher);
        tv_school=(TextView) findViewById(R.id.tv_school);
        tv_date=(TextView) findViewById(R.id.tv_date);
        tv_description=(TextView) findViewById(R.id.tv_description);
        iv_course = (ImageView) findViewById(R.id.iv_course);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String course = bundle.getString("idContent");

        String address = "http://123.207.6.140:8080/getCourseById";
        RequestBody requestBody = new FormBody
                .Builder()
                .add("id",course)
                .build();
        HttpUtil.sendOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);
                showResponse(responseData);
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.parseObject(response);

                String course = jsonObject.getString("name");
                String teacher = jsonObject.getString("teacher");
                String school = jsonObject.getString("school");
                String time = jsonObject.getString("time");
                String info = jsonObject.getString("info");
                String image = jsonObject.getString("imageUrl");

                tv_course_id.setText(course);
                tv_teacher.setText(teacher);
                tv_school.setText(school);
                tv_date.setText(time);
                tv_description.setText(info);
                Picasso.get()
                        .load(image)
                        .into(iv_course);

                jcVideoPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.jiecao_Player);
                //添加视频缩略图
                ImageView thumbImageView = jcVideoPlayerStandard.thumbImageView;
                //使用Glide添加
                Glide.with(CourseDetailsActivity.this).load(imageUrl).into(thumbImageView );
                //配置jiecaovideoplayer
                jcVideoPlayerStandard.setUp(s1,jcVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"视频标题");
            }
        });
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

    public void onReturnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
