package com.test_scroll_to_do;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/8/6.
 */
public abstract class MyBaseRecyclerAdapter extends RecyclerView.Adapter<MyBaseRecyclerAdapter.MyBaseRecyclerHolder> {

    private static class MyBaseRecyclerHandler extends Handler{
        private MyBaseRecyclerAdapter adapter;
        public MyBaseRecyclerHandler(MyBaseRecyclerAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public void handleMessage(Message msg) {
            if(adapter.onItemClickListener != null){
                adapter.onItemClickListener.onItemClick(null,(View)msg.obj,msg.what,0);
            }
        }
    }

    public MyBaseRecyclerHandler myBaseRecyclerHandler;
    public AdapterView.OnItemClickListener onItemClickListener;
    public MyOnContentViewClick onContentViewClick;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public MyBaseRecyclerAdapter(){
        myBaseRecyclerHandler = new MyBaseRecyclerHandler(this);
        onContentViewClick = new MyOnContentViewClick(myBaseRecyclerHandler);
    }

    @Override
    public MyBaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyScrollView scrollView = new MyScrollView(parent.getContext());
        MyBaseRecyclerHolder baseRecyclerHolder = getContentView(parent.getContext(),scrollView);
        baseRecyclerHolder.setHandler(onContentViewClick);
        return baseRecyclerHolder;
    }

    public abstract MyBaseRecyclerHolder getContentView(Context context,View scrollView);

    public static class MyBaseRecyclerHolder extends RecyclerView.ViewHolder{

        public MyBaseRecyclerHolder(View itemView) {
            super(itemView);
        }

        public void setHandler(View.OnClickListener click){
            MyScrollView myScrollView = (MyScrollView) itemView;
            myScrollView.getMyContentView().setOnClickListener(click);
        }

        public void bindMyView(int position){
            if(MyScrollView.selectPosition == position){
                showMenu();
            }
            else{
                dissMenu();
            }
        }

        public void showMenu(){
            MyScrollView myScrollView = (MyScrollView) itemView;
            myScrollView.showMenu();
        }

        public void dissMenu(){
            MyScrollView myScrollView = (MyScrollView) itemView;
            myScrollView.dissMenu();
        }

        public void addView(String text,Object tag,View.OnClickListener onClickListener){
            MyScrollView myScrollView = (MyScrollView) itemView;
            myScrollView.addMenu(text,tag,onClickListener);
        }

        public void addView(String text,Object tag,int tvColor,int bgColor,View.OnClickListener onClickListener){
            MyScrollView myScrollView = (MyScrollView) itemView;
            myScrollView.addMenu(text,tag,tvColor,bgColor,onClickListener);
        }

        public void setMenuVisibble(String tag , int visible){
            MyScrollView myScrollView = (MyScrollView) itemView;
            myScrollView.setMenuVisible(tag,visible);
        }

        public void setPosition(int position){
            MyScrollView myScrollView = (MyScrollView) itemView;
            myScrollView.setItemPosition(position);
        }
    }

    static class MyOnContentViewClick implements View.OnClickListener{
        public Handler handler;
        public MyOnContentViewClick(Handler handler){
            this.handler = handler;
        }

        @Override
        public void onClick(View v) {
            if(handler == null){
                return;
            }
            Message message = Message.obtain();
            message.obj = v;
            message.what = (int) v.getTag();
            handler.sendMessage(message);
        }
    }

}
