package com.fengwei23.rxjavalearning;

import android.app.Application;
import android.content.Context;

/**
 * author: w.feng
 * time  : 2018/07/24
 * desc  :
 */
public class App extends Application {

    public static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
