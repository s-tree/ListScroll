package com.test_scroll_to_do;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2016/8/15.
 */
public class MyFristActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_first);
    }

    public void go(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
