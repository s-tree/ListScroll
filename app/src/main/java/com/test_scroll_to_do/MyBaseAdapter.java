package com.test_scroll_to_do;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/5.
 */
public abstract class MyBaseAdapter extends BaseAdapter{

    public MyBaseAdapterHandler myBaseRecyclerHandler;
    public AdapterView.OnItemClickListener onItemClickListener;

    private static class MyBaseAdapterHandler extends Handler {
        private MyBaseAdapter adapter;
        public MyBaseAdapterHandler(MyBaseAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public void handleMessage(Message msg) {
            if(adapter.onItemClickListener != null){
                adapter.onItemClickListener.onItemClick(null,(View)msg.obj,msg.what,0);
            }
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public MyBaseAdapter(){
        myBaseRecyclerHandler = new MyBaseAdapterHandler(this);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyBaseHolder myBaseHolder = null;
        if(view == null){
            MyScrollView scrollView = new MyScrollView(viewGroup.getContext());
            View contentView = getContentView(viewGroup.getContext());
            if(getMenuWidth() == 0){
                scrollView.addContent(contentView);
            }
            else{
                scrollView.addContent(contentView,getMenuWidth(),0,0,0,0);
            }
            myBaseHolder = createHolder(scrollView);
            myBaseHolder.setHandler(myBaseRecyclerHandler);
            scrollView.setTag(myBaseHolder);
            view = scrollView;
        }
        else{
            myBaseHolder = (MyBaseHolder) view.getTag();
        }
        myBaseHolder.bindView(i);
        return view;
    }

    abstract int getMenuWidth();
    abstract MyBaseHolder createHolder(MyScrollView myScrollView);

    abstract View getContentView(Context context);

    static class MyBaseHolder {
        MyScrollView myScrollView;
        MyBaseAdapterHandler myBaseRecyclerHandler;

        public MyBaseHolder(MyScrollView myScrollView){
            this.myScrollView = myScrollView;
        }

        public void setHandler(MyBaseAdapterHandler baseRecyclerHandler){
            this.myBaseRecyclerHandler = baseRecyclerHandler;
            myScrollView.getMyContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myBaseRecyclerHandler == null) {
                        return;
                    }
                    Message message = Message.obtain();
                    message.obj = v;
                    message.what = myScrollView.getItemPosition();
                    myBaseRecyclerHandler.sendMessage(message);
                }
            });
        }

        public void showMenu(){
            myScrollView.showMenu();
        }

        public void dissMenu(){
            myScrollView.dissMenu();
        }

        public void addView(String text,Object tag,View.OnClickListener onClickListener){
            myScrollView.addMenu(text,tag,onClickListener);
        }

        public void addView(String text,Object tag,int tvColor,int bgColor,View.OnClickListener onClickListener){
            myScrollView.addMenu(text,tag,tvColor,bgColor,onClickListener);
        }


        public void setMenuVisibble(String tag , int visible){
            myScrollView.setMenuVisible(tag,visible);
        }

        public void bindView(int position){
            if(MyScrollView.selectPosition != position){
                dissMenu();
            }
            else{
                showMenu();
            }
            setPosition(position);
        }

        public void setPosition(int position){
            myScrollView.setItemPosition(position);
        }

    }

}
