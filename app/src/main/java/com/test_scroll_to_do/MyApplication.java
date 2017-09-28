package com.test_scroll_to_do;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Administrator on 2016/8/13.
 */
public class MyApplication extends Application {
    public static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        AppContext.init(this);
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }
}
