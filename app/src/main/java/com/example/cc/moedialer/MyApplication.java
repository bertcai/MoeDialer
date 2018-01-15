package com.example.cc.moedialer;

import android.app.Application;
import android.content.Context;

/**
 * Created by cc on 18-1-10.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
