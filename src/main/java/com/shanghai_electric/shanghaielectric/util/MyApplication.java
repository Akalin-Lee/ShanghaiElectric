package com.shanghai_electric.shanghaielectric.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Akalin on 2017/2/28.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
