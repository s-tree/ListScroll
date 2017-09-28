package com.test_scroll_to_do;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/5.
 */
public class MyAdapter extends MyBaseAdapter {
    public MyAdapterOnClickListener mOnclickListener;
    public int width;

    public MyAdapter(int width){
        this.width = width;
    }

    @Override
    int getMenuWidth() {
        return width;
    }

    @Override
    MyBaseHolder createHolder(MyScrollView myScrollView) {
        return new MyHolder(myScrollView);
    }

    @Override
    View getContentView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,null);
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class MyHolder extends MyBaseHolder{
        private TextView t;

        public MyHolder(MyScrollView myScrollView) {
            super(myScrollView);
            t = (TextView) myScrollView.findViewById(R.id.text);
            if(mOnclickListener == null){
                mOnclickListener = new MyAdapterOnClickListener();
            }
            addView("新增","add", Color.WHITE,t.getResources().getColor(R.color.gary),mOnclickListener);
            addView("删除", "del", Color.WHITE, t.getResources().getColor(R.color.green), mOnclickListener);
            addView("取消", "cancle", Color.WHITE, t.getResources().getColor(R.color.blue), mOnclickListener);
        }

        @Override
        public void bindView(int position) {
            super.bindView(position);
            t.setText("" + position);
            if(position % 3 == 0){
                setMenuVisibble("cancle", View.GONE);
            }
            else{
                setMenuVisibble("cancle", View.VISIBLE);
            }
        }

    }

    private static class MyAdapterOnClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            View parent = (View) v.getParent();
            int position = (int) parent.getTag();
            MyToast.show("position = "+ position + "tag = " + v.getTag());
        }
    }
}
