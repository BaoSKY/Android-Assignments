package com.example.studycourse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.studycourse.Adapter.RecyclerviewAdapter;
import com.example.studycourse.Util.Course;
import com.example.studycourse.Util.HttpUtil;
import com.example.studycourse.Util.SharedPreferencesUtil;
import com.example.studycourse.activity.LoginActivity;
import com.example.studycourse.activity.SearchActivity;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferencesUtil spu;
    private RecyclerView recyclerView;
    private List<Course> courseList = new ArrayList<Course>(); //创建集合保存课程信息
    private TextView et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spu = SharedPreferencesUtil.getInstance(getApplicationContext());

        recyclerView = (RecyclerView)findViewById(R.id.rv_course) ;
        //给recyclerview里的item设置布局，一行一个
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        et_search = (TextView)findViewById(R.id.et_search);

        getData();
    }

    private void getData(){
        //初始化数据库
        LitePal.getDatabase();
        //判断数据库中是否已有课程信息，有则直接读取，没有则从网络请求
        List<Course> courses = DataSupport.findAll(Course.class);
        if(courses.isEmpty()){
            Log.d(TAG, "getData: ");
            String address = "http://123.207.6.140:8080/getAllCourses";
            RequestBody requestBody = new FormBody.Builder().build();
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
        }else{
            RecyclerviewAdapter reAdapter = new RecyclerviewAdapter(MainActivity.this,courses);
            recyclerView.setAdapter(reAdapter);
        }
    }

    public void onLogoutClick(View view) {
        spu.setLogin(false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    public void onSearchClick(View view) {
        String keyWord = et_search.getText().toString();
        String address = "http://123.207.6.140:8080/searchCourses";
        RequestBody requestBody = new FormBody
                .Builder()
                .add("condition",keyWord)
                .build();
        HttpUtil.sendOkHttpRequest(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println("search : "+responseData);
                showResponseSearch(responseData);
            }
        });
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                JSONArray jsonArray = JSONArray.parseArray(response);
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        JSONObject json = jsonArray.getJSONObject(i);
                        String courseID = json.getString("id");
                        String name = json.getString("name");
                        String teacher = json.getString("teacher");
                        String school = json.getString("school");
                        String imageUrl = json.getString("imageUrl");
                        String videoUrl = json.getString("videoUrl");
                        String audioUrl = json.getString("audioUrl");
                        int type;
                        if(videoUrl!=null&&audioUrl!=null){
                            type = 1;
                        }else {
                            type = 0;
                        }

                        Course course = new Course(courseID,name,teacher,school,imageUrl,type);
                        course.save();
                        courseList.add(course);
                    }
                }
                RecyclerviewAdapter reAdapter = new RecyclerviewAdapter(MainActivity.this,courseList);
                recyclerView.setAdapter(reAdapter);
            }
        });
    }

    private void showResponseSearch(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("response",response);
                startActivity(intent);
            }
        });
    }
}
