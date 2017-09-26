package com.szw.tools.wechatgroupandroid.quicktool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.pages.qa.QaIngManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaManager;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaPlayListenerListener;
import com.szw.tools.wechatgroupandroid.service.WeChatUtils;
import com.szw.tools.wechatgroupandroid.utils.DpOrPx;
import com.szw.tools.wechatgroupandroid.utils.TimeFormatUtils;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class QuickTollBar  extends Toast{
    private static QuickTollBar quickTollBar;
    private View view;
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
        view = LayoutInflater.from(context).inflate(R.layout.bar_quick_tool, null);
        setGravity(Gravity.TOP|Gravity.RIGHT,10, DpOrPx.dip2px(WeChatAdnroidGroup.getInstance(),50));
        setDuration(LENGTH_LONG);
        // question logic
        QaLogic(view);

        setView(view);
    }

    private TextView titleQa,tips,times,qades;

    private void QaLogic(View view) {
        titleQa = (TextView) view.findViewById(R.id.tv_qa_bar_title);
        tips = (TextView) view.findViewById(R.id.tv_qa_bar_tips);
        times = (TextView) view.findViewById(R.id.tv_qa_bar_time);
        qades = (TextView) view.findViewById(R.id.tv_qa_bar_qa_des);

        View qaContain= view.findViewById(R.id.qa_bar_contain);
        Questions openQusetions = QaIngManager.getInstance().getQaNowQuestions();
        if(openQusetions.getTitle()==null){
            qaContain.setVisibility(View.GONE);
            return;
        }
        qaContain.setVisibility(View.VISIBLE);
        titleQa.setText(openQusetions.getTitle()+"("+openQusetions.getQuestions().size()+")");
        tips.setText("问答倒计时:");
        times.setText(10+"");

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = WeChatAdnroidGroup.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = WeChatAdnroidGroup.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private boolean isCancle;
    @Override
    public void cancel() {
        super.cancel();
        isCancle = true;
        QaIngManager.getInstance().getQaPlayer().quitePlay();
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
        QaLogic(view);
        show();
        QaIngManager.getInstance().getQaPlayer().play(new QaPlayListenerListener() {
            @Override
            public void onReady(Questions questions, int current, long currentTime, String tip) {
                qades.setText("等待开始");
                times.setText(TimeFormatUtils.formatSeconds(currentTime/1000));
                tips.setText(tip);
//                if(times.getText().equals("9秒")) {
//                    String des = "10秒后将开始答题，试卷为（"+questions.getTitle()+"）";
//                    WeChatUtils.getInstance().sendText(des,true);
//                }
            }

            @Override
            public void onTickChange(long tick, String tip) {
                times.setText(TimeFormatUtils.formatSeconds(tick/1000));
            }

            @Override
            public void onNext(Question question,int pos) {
                qades.setText(pos+1+"."+question.getDes());
                tips.setText("剩余");
                WeChatUtils.getInstance().sendText(question.getDes(),true);
            }

            @Override
            public void onFinshPlay() {
                times.setText("已经完成");
                tips.setText("");
            }
        }, WeChatUtils.getInstance().getCacheWeChatGroup());
    }
}
