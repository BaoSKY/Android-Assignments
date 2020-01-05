package com.example.studycourse.Util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequest(String address, RequestBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        System.out.println(request);
        System.out.println(requestBody);
        client.newCall(request).enqueue(callback);
    }
}
