package com.example.studycourse.vm;

import android.content.Context;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.studycourse.Util.Course;
import com.example.studycourse.Util.HttpUtil;
import com.example.studycourse.dao.CourseDao;
import com.example.studycourse.room.CourseRoomDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseViewHolder {
    private static final String TAG = "CourseViewHolder";
    private String courseId;

    private CourseRoomDatabase database;

    public final ObservableField<String> id = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> teacher = new ObservableField<>();
    public final ObservableField<String> school = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();
    public final ObservableField<String> info = new ObservableField<>();

    public CourseViewHolder(Context context, String courseId){
        database = CourseRoomDatabase.getDatabase(context);
        this.courseId = courseId;
        queryCourse();
    }

    public void queryCourse(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CourseDao courseDao = database.courseDao();
                Course course = courseDao.findCourseById(courseId);

                id.set(courseId);
                name.set(course.getcourseName());
                teacher.set(course.getTeacher());
                school.set(course.getSchool());
                time.set(course.getCourseDate());
                info.set(course.getCourseDescription());
            }
        }).start();
    }
}
