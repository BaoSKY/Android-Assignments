package com.example.studycourse.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.studycourse.Util.Course;
import com.example.studycourse.dao.CourseDao;

@Database(entities = {Course.class}, version = 1, exportSchema = false)
public abstract class CourseRoomDatabase extends RoomDatabase {

    private static volatile CourseRoomDatabase INSTANCE;

    public static CourseRoomDatabase getDatabase(Context context){
        if(INSTANCE == null){
            synchronized (CourseRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context,
                            CourseRoomDatabase.class, "elearning").build();
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public abstract CourseDao courseDao();
}
