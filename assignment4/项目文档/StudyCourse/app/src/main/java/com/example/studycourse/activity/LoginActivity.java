package com.example.studycourse.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studycourse.MainActivity;
import com.example.studycourse.R;
import com.example.studycourse.Util.Constants;
import com.example.studycourse.Util.HttpUtil;
import com.example.studycourse.Util.RegexUtil;
import com.example.studycourse.Util.SharedPreferencesUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private Tencent mTencent;
    private ImageView qq_login;
    private IUiListener qq_login_listener;
    private EditText et_username;
    private EditText et_password;
    private Button bt_login;
    private SharedPreferencesUtil spu;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        bt_login=findViewById(R.id.bt_login);
        qq_login = (ImageView) findViewById(R.id.qq_login);

        bt_login.setOnClickListener(this);

        spu=SharedPreferencesUtil.getInstance(getApplicationContext());

        mTencent = Tencent.createInstance("101840549",LoginActivity.this);
        View.OnClickListener qqClickListener = (view)->{
            qq_login_listener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    org.json.JSONObject jsonObject = (org.json.JSONObject) o;
                    String openid = jsonObject.optString("openid");
                    String token,expires_in;
                    try{
                        token = jsonObject.getString("access_token");
                        expires_in = jsonObject.getString("expires_in");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    QQToken qqToken = mTencent.getQQToken();
                    mTencent.setOpenId(openid);
                    mTencent.setAccessToken(token,expires_in);
                    UserInfo userinfo = new UserInfo(LoginActivity.this,qqToken);
                    userinfo.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            org.json.JSONObject json = (org.json.JSONObject) o;
                            String nickname = json.optString("nickname");
                            String figureurl = json.optString("figureurl_qq_2");
                            spu.setLogin(true);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onError(UiError uiError) {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                @Override
                public void onError(UiError uiError) {

                }
                @Override
                public void onCancel() {

                }
            };
            mTencent.login(LoginActivity.this,"all",qq_login_listener);
        };
        qq_login.setOnClickListener(qqClickListener);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcast.LESSON_ADVERTISEMENT");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        String channelId = "subscribe";
        String channelName = "课程消息";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId, channelName, importance);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://123.207.6.140:8080/getCourseAdvertisement";
                while(true){
                    HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Intent intent = new Intent("com.example.broadcast.LESSON_ADVERTISEMENT");
                            intent.putExtra("data", responseData);
                            localBroadcastManager.sendBroadcast(intent);
                            Log.d(TAG, "onResponse: 发送广播");
                        }
                    });

                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_login:
                login();

                break;
        }
    }

    private void login(){
        String user=et_username.getText().toString().trim();
        //判断是否输入了邮箱
        if(TextUtils.isEmpty(user)){
            Toast.makeText(this,R.string.email_hint,Toast.LENGTH_SHORT).show();
            return;
        }
        //判断邮箱格式是否正确
        if(!RegexUtil.isEmail(user)){
            Toast.makeText(this,R.string.error_password_format,Toast.LENGTH_SHORT).show();
            return;
        }

        //判断是否输入了密码
        String pass=et_password.getText().toString().trim();
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,R.string.password_hint,Toast.LENGTH_SHORT).show();
            return;
        }
        //判断密码长度
        if(pass.length()<6||pass.length()>15){
            Toast.makeText(this,R.string.error_password_length,Toast.LENGTH_SHORT).show();
            return;
        }

        String address = "http://123.207.6.140:8080/login";
        RequestBody requestBody = new FormBody.Builder()
                .add("mail", user)
                .add("password", pass)
                .build();
        HttpUtil.sendOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                showResponse(responseData);
            }
        });

        /*
        if(Constants.EMAIL.equals(user)&&Constants.PASSWORD.equals(pass)){
            spu.setLogin(true);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            finish();
        }else{
            Toast.makeText(this,R.string.error_email_password,Toast.LENGTH_SHORT).show();
        }
         */
    }

    //android规定耗时操作如http请求不能在主线程里操作，必须给他开一个子线程
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(response);
                if(response.equals("登录成功")){
                    spu.setLogin(true);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,R.string.error_email_password,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            String data = intent.getStringExtra("data");
            Log.d(TAG, "onReceive: 接收广播");
            try {
                JSONObject jsonObject = new JSONObject(data);

                String courseName = jsonObject.getString("name");
                String courseTeacher = jsonObject.getString("teacher");
                String courseSchool = jsonObject.getString("school");
                String text = courseSchool + "新开课程：" + courseName + ", 专业名师 " + courseTeacher
                        + " 领衔主讲。";
                Log.d(TAG, "onReceive: text:" + text);
                Notification notification = new NotificationCompat.Builder(context, "subscribe")
                        .setContentTitle("E-Learning 课程提醒")
                        .setContentText(text)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.audio)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.audio))
                        .build();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, notification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
