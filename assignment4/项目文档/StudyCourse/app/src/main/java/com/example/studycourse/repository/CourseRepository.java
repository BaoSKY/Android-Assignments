package com.example.studycourse.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.studycourse.Util.Course;
import com.example.studycourse.dao.CourseDao;
import com.example.studycourse.room.CourseRoomDatabase;

import java.util.List;

public class CourseRepository {

    private CourseDao courseDao;
    private LiveData<List<Course>> courses;

    public CourseRepository(Application application){
        CourseRoomDatabase database = CourseRoomDatabase.getDatabase(application);
        courseDao = database.courseDao();
        courses = courseDao.getCourseAll();
    }

    public LiveData<List<Course>> getAllCourses(){
        return courses;
    }

    public void insert(Course course){
        new InsertAsyncTask(courseDao).execute(course);
    }

    private static class InsertAsyncTask extends AsyncTask<Course, Void, Void>{
        private CourseDao courseDao;

        InsertAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses){
            courseDao.insert(courses[0]);
            return null;
        }
    }
}
