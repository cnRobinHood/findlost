package com.weareone.findlost.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Rott on 2018/2/26.
 */

public class HttpUtil {
    public static final String BASEUEL = "http://123.207.8.147:8080/findlost";
//    public static final String BASEUEL = "http://172.18.104.184:8080";

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback, String data[]) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
