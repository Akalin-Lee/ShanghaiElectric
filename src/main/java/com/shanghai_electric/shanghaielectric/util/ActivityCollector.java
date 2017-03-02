package com.shanghai_electric.shanghaielectric.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akalin on 2017/2/28.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finshAll(){
        for (Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
