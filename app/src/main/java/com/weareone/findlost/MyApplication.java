package com.weareone.findlost;

import android.app.Application;

import com.weareone.findlost.utils.NotificationClickEventReceiver;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by asus on 2018/2/28.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND | JMessageClient.FLAG_NOTIFY_WITH_LED | JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
        //注册Notification点击的接收器
        new NotificationClickEventReceiver(getApplicationContext());
    }
}
