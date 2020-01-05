package com.example.studycourse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.studycourse.Adapter.RecyclerviewAdapter;
import com.example.studycourse.MainActivity;
import com.example.studycourse.R;
import com.example.studycourse.Util.Course;
import com.example.studycourse.Util.HttpUtil;
import com.example.studycourse.dao.CourseDao;
import com.example.studycourse.databinding.ActivityCourseDetailsBinding;
import com.example.studycourse.room.CourseRoomDatabase;
import com.example.studycourse.vm.CourseViewHolder;
import com.squareup.picasso.Picasso;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.IOException;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseDetailsActivity extends AppCompatActivity {

    private CourseViewHolder viewHolder;
    private ActivityCourseDetailsBinding binding;

    private Tencent mtencent;
    private IUiListener qqShareListener;

    private Button bt_share;

    private SensorManager sensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private JCVideoPlayerStandard jcVideoPlayerStandard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String courseId = bundle.getString("idContent");

        viewHolder = new CourseViewHolder(getBaseContext(), courseId);
        binding.setViewModel(viewHolder);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        mtencent = Tencent.createInstance("101840549",CourseDetailsActivity.this);
        View.OnClickListener shareListener = (view->{
            qqShareListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Toast.makeText(CourseDetailsActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(CourseDetailsActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancel() {
                    Toast.makeText(CourseDetailsActivity.this,"取消分享",Toast.LENGTH_SHORT).show();
                }
            };
            Bundle qqParams = new Bundle();
            qqParams.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            qqParams.putString(QQShare.SHARE_TO_QQ_TITLE,"毕加索艺术鉴赏");
            qqParams.putString(QQShare.SHARE_TO_QQ_SUMMARY,"picasso");
            qqParams.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4");
            qqParams.putString(QQShare.SHARE_TO_QQ_APP_NAME,"在线学习");
            mtencent.shareToQQ(this,qqParams,qqShareListener);
        });
        bt_share.setOnClickListener(shareListener);
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
