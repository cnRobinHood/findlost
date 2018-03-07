package com.weareone.findlost.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weareone.findlost.talk.TalkActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;


public class NotificationClickEventReceiver {
    private static final String TAG = "NotificationClickEventR";
    private Context mContext;

    public NotificationClickEventReceiver(Context context) {

        mContext = context;
        //注册接收消息事件
        JMessageClient.registerEventReceiver(this);
    }


    public void onEvent(NotificationClickEvent notificationClickEvent) {
        Log.d(TAG, "onEvent: " + "onclick");
        if (null == notificationClickEvent) {
            return;
        }
        Message msg = notificationClickEvent.getMessage();
        if (msg != null) {
            String targetId = msg.getTargetID();
            String appKey = msg.getFromAppKey();
            ConversationType type = msg.getTargetType();
            Conversation conv = null;
            Intent notificationIntent = new Intent(mContext, TalkActivity.class);
            conv = JMessageClient.getSingleConversation(targetId, appKey);
            notificationIntent.putExtra("username", conv.getTitle());
            mContext.startActivity(notificationIntent);
        }
    }

}
