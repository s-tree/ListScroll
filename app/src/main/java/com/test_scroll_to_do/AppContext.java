package com.test_scroll_to_do;

import android.content.Context;

/**
 * Created by Administrator on 2016/8/13.
 */
public class AppContext {

    public static Context context;

    public static void init(Context context){
        AppContext.context = context;
    }

    public static Context getApplicationContext(){
        return context;
    }

}
