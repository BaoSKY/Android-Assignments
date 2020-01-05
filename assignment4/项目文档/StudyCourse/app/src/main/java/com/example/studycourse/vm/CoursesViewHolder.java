package com.example.studycourse.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studycourse.Util.Course;
import com.example.studycourse.repository.CourseRepository;

import java.util.List;

public class CoursesViewHolder extends AndroidViewModel {
    private LiveData<List<Course>>  courses;
    private CourseRepository repository;

    public CoursesViewHolder(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        courses = repository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses(){
        return courses;
    }

    public CourseRepository getRepository(){
        return repository;
    }

    public void insert(Course course){
        repository.insert(course);
    }

}
