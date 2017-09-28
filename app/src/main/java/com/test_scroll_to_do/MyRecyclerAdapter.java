package com.test_scroll_to_do;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/6.
 */
public class MyRecyclerAdapter extends MyBaseRecyclerAdapter {
    public int recyclerViewWidth = 0;
    public MyRecyclerAdapterOnClickListener mOnclickListener;
    public MyRecyclerAdapter(int recyclerViewWidth){
        super();
        this.recyclerViewWidth = recyclerViewWidth;
    }


    @Override
    public MyBaseRecyclerHolder getContentView(Context context,ViewGroup scrollView) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item,null,false);
        MyScrollView _scrollView = (MyScrollView) scrollView;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 0;
        params.bottomMargin = 1;
        params.width = recyclerViewWidth;
        _scrollView.addContent(v,recyclerViewWidth,0,0,0,0);
        return new MyRecyclerHolder(scrollView);
    }

    @Override
    public void onBindViewHolder(MyBaseRecyclerHolder holder, int position) {
        holder.bindMyView(position);
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class MyRecyclerHolder extends MyBaseRecyclerHolder{
        private TextView t;

        public MyRecyclerHolder(View itemView) {
            super(itemView);
            t = (TextView) itemView.findViewById(R.id.text);
            if(mOnclickListener == null){
                mOnclickListener = new MyRecyclerAdapterOnClickListener();
            }
            addView("新增","add", Color.WHITE,t.getResources().getColor(R.color.gary),mOnclickListener);
            addView("删除", "del", Color.WHITE, t.getResources().getColor(R.color.green), mOnclickListener);
            addView("取消", "cancle", Color.WHITE, t.getResources().getColor(R.color.blue), mOnclickListener);
        }

        public void bindMyView(int position){
            super.bindMyView(position);
            t.setText("recycler " + position);
            setPosition(position);
            if(position % 3 == 0){
                setMenuVisibble("cancle",View.GONE);
            }
            else{
                setMenuVisibble("cancle",View.VISIBLE);
            }
        }

    }

    private static class MyRecyclerAdapterOnClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            View parent = (View) v.getParent();
            int position = (int) parent.getTag();
            MyToast.show("position = " + position + "tag = " + v.getTag());
        }
    }
}
