package com.test_scroll_to_do;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.eclipse.mat.hprof.IHprofParserHandler;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private RecyclerView recycler;
    private MyAdapter adapter;
    MyRecyclerAdapter myRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        adapter = new MyAdapter(screenWidth);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyToast.show("listView click position = " + position);
            }
        });
        list.setAdapter(adapter);
        recycler = (RecyclerView) findViewById(R.id.recycler);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recycler.setLayoutManager(manager);
        myRecyclerAdapter = new MyRecyclerAdapter(screenWidth);
        myRecyclerAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyToast.show("recycler click position = " + position);
            }
        });
        recycler.setAdapter(myRecyclerAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("ListView");
        menu.add("recyclerView");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(TextUtils.equals(item.getTitle(),"ListView")){
            list.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }else{
            recycler.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyScrollView.release();
        MyApplication.refWatcher.watch(myRecyclerAdapter.myBaseRecyclerHandler);
        myRecyclerAdapter.myBaseRecyclerHandler = null;
        myRecyclerAdapter.onItemClickListener = null;
        myRecyclerAdapter.mOnclickListener = null;
        myRecyclerAdapter.myBaseRecyclerHandler = null;
        myRecyclerAdapter.onContentViewClick.handler = null;
        myRecyclerAdapter.onContentViewClick = null;
        myRecyclerAdapter = null;
        MyApplication.refWatcher.watch(adapter.myBaseRecyclerHandler);
        adapter.myBaseRecyclerHandler = null;
        adapter.onItemClickListener = null;
        adapter.mOnclickListener = null;
        adapter.myBaseRecyclerHandler = null;
        adapter = null;
        list.setAdapter(null);
        list = null;
        recycler.setAdapter(null);
        recycler = null;
        System.gc();
        System.runFinalization();
        MyApplication.refWatcher.watch(this);
    }
}
