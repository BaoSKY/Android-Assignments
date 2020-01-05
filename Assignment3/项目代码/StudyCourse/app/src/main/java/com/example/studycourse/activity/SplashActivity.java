package com.example.studycourse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.example.studycourse.MainActivity;
import com.example.studycourse.R;
import com.example.studycourse.Util.SharedPreferencesUtil;


/**
 * 启动页面
 * 三秒后进入登录界面
 **/

public class SplashActivity extends AppCompatActivity {

    private SharedPreferencesUtil spu;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    private void next() {
        Intent intent = null;
        if(spu.isLogin()){
            intent = new Intent(this, MainActivity.class);
        }else{
            intent = new Intent(this,LoginActivity.class);
        }

        //intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        spu= SharedPreferencesUtil.getInstance(getApplicationContext());

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                handler.sendEmptyMessage(0);
            }
        },3000);
    }
}