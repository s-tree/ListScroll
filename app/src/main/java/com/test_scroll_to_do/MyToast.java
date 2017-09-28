package com.test_scroll_to_do;

import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/13.
 */
public class MyToast {

    public static Toast toast;

    public static Toast getInstance(){
        if(toast == null){
            toast = Toast.makeText(AppContext.getApplicationContext(),"",Toast.LENGTH_SHORT);
        }
        return toast;
    }

    public static void show(String text){
        Toast t = getInstance();
        t.setText(text);
        t.show();
    }
}
