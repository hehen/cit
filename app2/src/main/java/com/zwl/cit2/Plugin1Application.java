package com.zwl.cit2;

import android.app.Application;
import android.util.Log;

public class Plugin1Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        userName = "插件中的初始化";
        Log.e("Plugin1Application","插件中的初始化");
    }

    public static String userName;
}
