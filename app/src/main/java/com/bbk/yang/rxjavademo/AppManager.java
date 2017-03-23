package com.bbk.yang.rxjavademo;

import android.app.Application;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

/**
 * Created by yang on 2017/3/23.
 */

public class AppManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.init(LogLevel.ALL);
    }
}
