package com.example.studycourse.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "eLearning.db";
    private static final String TAG = "MyDatabaseHelper";

    private static final String CREATE_TABLE_user = "CREATE TABLE user(\n" +
                                                    "    mail text PRIMARY KEY,\n" +
                                                    "    password text\n" +
                                                    ")";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_user);
        Log.d(TAG, "onCreate: table user");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        onCreate(sqLiteDatabase);
        Log.d(TAG, "onUpgrade: ");
    }
}
