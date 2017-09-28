package com.test_scroll_to_do;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by Administrator on 2016/8/5.
 */
public class MyScrollView extends HorizontalScrollView {
    private int nowLeft = 0;
    private int menuWidth = 0;
    private int height;
    private LinearLayout linearLayout;
    private int width;
    private int marginTop,marginButtom;
    private static MyScrollHandler mHandler;
    private static ExecutorService service;
    public static MyScrollView selectMyScrollView ;
    public static int selectPosition = -1;

    //处理滑动动画
    private static class MyScrollHandler extends Handler{
        public void handleMessage(Message msg) {
            if(msg.obj == null || msg.what < 0 ){
                return;
            }
            int nowS = msg.what;
            MyScrollView myScrollView = (MyScrollView) msg.obj;
            myScrollView.scrollTo(nowS,0);
        }
    }

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化
    private void init(){
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        addView(linearLayout, 0, params);
        if(mHandler == null){
            mHandler = new MyScrollHandler();
        }
        if(service == null){
            service = Executors.newFixedThreadPool(5);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        layoutChild();
    }

    private void layoutChild(){
        if(linearLayout.getChildCount() > 0){
            View content = linearLayout.getChildAt(0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (width,height);
            content.setLayoutParams(params);
        }
    }

    //设置position
    public void setItemPosition(int position){
        linearLayout.setTag(position);
        if(linearLayout.getChildCount() > 1){
            linearLayout.getChildAt(0).setTag(position);
        }
    }

    //获取position
    public int getItemPosition(){
        Object o = linearLayout.getTag();
        if(o != null && o instanceof Integer){
            return (int) linearLayout.getTag();
        }
        return -1;
    }

    //添加Content布局
    public void addContent(View v){
        addContent(v, width,0,0,0,0);
    }

    //添加Content布局
    public void addContent(View v,int contentWidth,int leftMargin,int topMargin,int rightMargin,int bottomMargin){
        if(linearLayout.getChildCount() != 0)
            throw  new RuntimeException("viewgroup had a contentView now");
        v.measure(0, 0);
        height = v.getMeasuredHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (contentWidth,height);
        params.topMargin = topMargin;
        params.bottomMargin = bottomMargin;
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        marginTop = topMargin;
        marginButtom = bottomMargin;
        linearLayout.addView(v, 0, params);
    }

    //获取ContentView
    public View getMyContentView(){
        if(linearLayout.getChildCount() == 0){
            return null;
        }
        return linearLayout.getChildAt(0);
    }

    //选择性显示隐藏menu
    public void setMenuVisible(String menuTag,int isVisible){
        View v = linearLayout.findViewWithTag(menuTag);
        if(v != null){
            v.setVisibility(isVisible);
        }
    }

    //添加menu
    public void addMenu(String text, Object tag,OnClickListener onClickListener){
        addMenu(text, tag, Color.BLACK,Color.WHITE,onClickListener);
    }

    //添加menu
    public void addMenu(String text, Object tag,int tvColor,int bgColor,OnClickListener onClickListener){
        TextView t = new TextView(getContext());
        t.setGravity(Gravity.CENTER);
        t.setText(text);
        t.setTextColor(tvColor);
        t.setBackgroundColor(bgColor);
        t.setTag(tag);
        t.setOnClickListener(onClickListener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                ((int) (height * 1.5),height);
        params.topMargin = marginTop;
        params.bottomMargin = marginButtom;
        linearLayout.addView(t, linearLayout.getChildCount(), params);
        menuWidth = (int) ((height * 1.5)) * (linearLayout.getChildCount() - 1);
    }

    //监听滑动事件
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        nowLeft = l;
    }

    //显示菜单
    public void showMenu(){
        scrollTo(menuWidth,0);
        selectMyScrollView = this;
    }

    //隐藏菜单
    public void dissMenu(){
        scrollTo(0,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
            if(nowLeft >= menuWidth/3){
                movetoMySelf(menuWidth);
                selectPosition = getItemPosition();
            }
            else{
                movetoMySelf(0);
                selectPosition = -1;
            }
            return true;
        }
        else {
            if(selectMyScrollView != null && selectMyScrollView != this){
                if(selectMyScrollView != null && selectMyScrollView.getScaleX() != 0){
                    selectMyScrollView.movetoMySelf(0);
                    selectPosition = -1;
                }
            }
            selectMyScrollView = this;
        }
        return super.onTouchEvent(ev);
    }

    //带动画的滑动
    public void movetoMySelf(int end){
        int mLeft = getScrollX();
        if(service == null || service.isShutdown()){
            service = Executors.newFixedThreadPool(5);
        }
        try{
            service.execute(new MyEndScrollRunnable(this,mLeft,end));
        }catch (RejectedExecutionException e){
            e.printStackTrace();
        }
    }

    static class MyEndScrollRunnable implements Runnable{
        public int nowLeft,end ;
        public MyScrollView mScrollView;

        public MyEndScrollRunnable(MyScrollView mScrollView,int left,int end){
            this.nowLeft = left;
            this.end = end;
            this.mScrollView = mScrollView;
        }

        @Override
        public void run() {
            int chaLeft = end - nowLeft;
            int count = 50;
            int stepLeft = chaLeft / count;
            for(int i = 0 ; i < count ; i++){
                int now = nowLeft + stepLeft * (i+1);
                Message message = Message.obtain();
                message.what = now;
                message.obj = mScrollView;
                mHandler.sendMessage(message);
                SystemClock.sleep(1);
                if(now == 0 || now == end){
                    break;
                }
            }
            Message message = Message.obtain();
            message.what = end;
            message.obj = mScrollView;
            mHandler.sendMessage(message);
            mScrollView = null;
        }
    }

    public static void release(){
        selectMyScrollView = null;
        mHandler = null;
    }
}
