package com.example.studycourse.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *用于保存登录状态
 **/
public class SharedPreferencesUtil {
    private static final String TAG="TAG";
    private static final String KEY_LOGIN="KEY_LOGIN";

    private static SharedPreferences mPreference;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedPreferenceUtil;
    private final Context context;

    public SharedPreferencesUtil(Context context){
        this.context=context.getApplicationContext();
        mPreference=this.context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        mEditor=mPreference.edit();
    }

    //简单的单例实现
    public static SharedPreferencesUtil getInstance(Context context){
        if(mSharedPreferenceUtil==null){
            mSharedPreferenceUtil = new SharedPreferencesUtil(context);
        }
        return mSharedPreferenceUtil;
    }

    public boolean isLogin(){
        return getBoolean(KEY_LOGIN,false);
    }

    public void setLogin(boolean value){
        putBoolean(KEY_LOGIN,value);
    }

    private void put(String key,String value){
        mEditor.putString(key,value);
        mEditor.commit();
    }

    private  void putBoolean(String key, boolean value){
        mEditor.putBoolean(key,value);
        mEditor.commit();
    }

    private String get(String key){
        return mPreference.getString(key,"");
    }

    private Boolean getBoolean(String key,boolean defaultValue){
        return  mPreference.getBoolean(key,defaultValue);
    }

    private void putInt(String key,int value){
        mEditor.putInt(key,value);
        mEditor.apply();
    }

    private int getInt(String key,int defaultVlue){
        return mPreference.getInt(key,defaultVlue);
    }

}
