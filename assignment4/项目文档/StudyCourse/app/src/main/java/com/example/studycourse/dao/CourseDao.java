package com.example.studycourse.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studycourse.Util.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insert(Course course);

    @Query("SELECT * FROM course")
    LiveData<List<Course>> getCourseAll();

    @Query("SELECT * FROM course")
    List<Course> findAllCourses();

    @Query("SELECT * FROM course WHERE courseID = :courseId")
    Course findCourseById(String courseId);
}
