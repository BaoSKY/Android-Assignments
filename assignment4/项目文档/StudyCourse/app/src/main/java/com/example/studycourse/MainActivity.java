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
import com.example.studycourse.dao.CourseDao;
import com.example.studycourse.room.CourseRoomDatabase;

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

    private CourseRoomDatabase database;
    private CourseDao courseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spu = SharedPreferencesUtil.getInstance(getApplicationContext());

        recyclerView = (RecyclerView)findViewById(R.id.rv_course) ;
        //给recyclerview里的item设置布局，一行一个
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        et_search = (TextView)findViewById(R.id.et_search);

        database = CourseRoomDatabase.getDatabase(this);
        courseDao = database.courseDao();
        getData();
    }

    private void getData(){
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
                        String imageURL = json.getString("imageUrl");
                        Course course = new Course(courseID,name,teacher,school,imageURL);
                        courseList.add(course);
                    }
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Course> courses = courseDao.findAllCourses();
                        if(courses.isEmpty()){
                            for(Course course:courseList){
                                courseDao.insert(course);
                            }
                        }
                    }
                }).start();
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
