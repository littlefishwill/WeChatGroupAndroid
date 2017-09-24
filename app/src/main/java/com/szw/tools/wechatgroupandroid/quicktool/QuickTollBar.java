package com.szw.tools.wechatgroupandroid.quicktool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.szw.tools.wechatgroupandroid.R;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class QuickTollBar  extends Toast{
    private static QuickTollBar quickTollBar;
    public static QuickTollBar getInstance(Context context){
        if(quickTollBar==null) {
           quickTollBar = new QuickTollBar(context);
        }
        return quickTollBar;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            show();
        }
    };
    private QuickTollBar(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bar_quick_tool, null);
        setGravity(Gravity.BOTTOM|Gravity.RIGHT,0,150);
        setDuration(LENGTH_LONG);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cancel();
                return false;
            }
        });
        setView(view);
    }

    private boolean isCancle;
    @Override
    public void cancel() {
        super.cancel();
        isCancle = true;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void show() {
        super.show();
       if(!isCancle){
           handler.sendEmptyMessageDelayed(0,3000);
       }
    }

    public void showAlways(){
        isCancle = false;
        show();
    }
}
