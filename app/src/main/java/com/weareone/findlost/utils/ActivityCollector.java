package com.weareone.findlost.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rott on 2018/2/24.
 */
//Activity工具类
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void finishAllActivity() {
        for (Activity activity : activities
                ) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
