package com.szw.tools.wechatgroupandroid.quicktool;

import android.content.Context;
import android.os.AsyncTask;
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
import com.szw.tools.wechatgroupandroid.pages.cq.CqManager;
import com.szw.tools.wechatgroupandroid.pages.cq.CqPlayListener;
import com.szw.tools.wechatgroupandroid.pages.qa.QaIngManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QuestionsShowAvtivity;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaPlayListenerListener;
import com.szw.tools.wechatgroupandroid.service.WeChatUtils;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.utils.DpOrPx;
import com.szw.tools.wechatgroupandroid.utils.TimeFormatUtils;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class QuickTollBar  extends Toast{
    private static QuickTollBar quickTollBar;
    private View view;
    private View cqContain;
    public static QuickTollBar getInstance(Context context){
        if(quickTollBar==null) {
           quickTollBar = new QuickTollBar(context);
        }
        return quickTollBar;
    }

    private QuickTollBar(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.bar_quick_tool, null);
        setGravity(Gravity.TOP|Gravity.RIGHT,10, DpOrPx.dip2px(WeChatAdnroidGroup.getInstance(),50));
        setDuration(LENGTH_LONG);
        // question logic
//        QaLogic(view);

        setView(view);
    }

    private TextView titleQa,tips,times,qades,qaanswer;

    private void QaLogic(View view) {
        titleQa = (TextView) view.findViewById(R.id.tv_qa_bar_title);
        tips = (TextView) view.findViewById(R.id.tv_qa_bar_tips);
        times = (TextView) view.findViewById(R.id.tv_qa_bar_time);
        qades = (TextView) view.findViewById(R.id.tv_qa_bar_qa_des);
        qaanswer = (TextView) view.findViewById(R.id.tv_qa_bar_answer);
        cqContain = view.findViewById(R.id.qa_bar_contain_cq);

        View qaContain= view.findViewById(R.id.qa_bar_contain);
        Questions openQusetions = QaIngManager.getInstance().getQaNowQuestions();
        if(openQusetions==null){
            qaContain.setVisibility(View.GONE);
            return;
        }
        qaContain.setVisibility(View.VISIBLE);
        titleQa.setText(openQusetions.getTitle()+"("+openQusetions.getQuestions().size()+")");
        tips.setText("问答倒计时:");
        times.setText(10+"");

        QaplayLogic();

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
        if(showAsyncTask!=null){
            showAsyncTask.cancel(true);
        }
        QaIngManager.getInstance().getQaPlayer().quitePlay();
        quickTollBar = null;

    }

    private AsyncTask showAsyncTask = null;
    @Override
    public void show() {
        super.show();
       if(!isCancle){
           if(showAsyncTask!=null) {
               showAsyncTask.cancel(true);
           }
           showAsyncTask = new AsyncTask() {
               @Override
               protected Object doInBackground(Object[] params) {
                   try {
                       Thread.sleep(2500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   return null;
               }

               @Override
               protected void onPostExecute(Object o) {
                   super.onPostExecute(o);
                   if(!isCancle) {
                       show();
                   }
               }
           };
           showAsyncTask.execute();
       }
    }

    public void showAlways(){
        isCancle = false;
        //--qa
        QaLogic(view);
        //--cq
        CqLogic();


        show();
    }

    private void CqLogic() {
        if(CqManager.getInstance().isOpen()) {
            cqContain.setVisibility(View.VISIBLE);
            CqManager.getInstance().play(new CqPlayListener() {
                @Override
                public void onNeddSend(String text) {
                    WeChatUtils.getInstance().sendText(text, true);
                }
            });
        }else{
            cqContain.setVisibility(View.GONE);
        }
    }

    private void QaplayLogic() {
        if(QaIngManager.getInstance().getQaNowQuestions()!=null) {
            QaIngManager.getInstance().getQaPlayer().play(new QaPlayListenerListener() {
                @Override
                public void onReady(Questions questions, int current, long currentTime, String tip) {
                    qades.setText(tip);
                    times.setText(TimeFormatUtils.formatSecondsUseCode(currentTime / 1000));
                    tips.setText(tip);
                }

                @Override
                public void onTickChange(long tick, String tip) {
                    times.setText(TimeFormatUtils.formatSecondsUseCode(tick / 1000));
                }

                @Override
                public void onNext(Question question, int pos) {
                    qades.setText(pos + 1 + "." + question.getDes());
                    tips.setText("剩余");
                    qaanswer.setText("答案：" + QuestionsShowAvtivity.getString(question.getType2Answer()));
                    WeChatUtils.getInstance().sendText(pos + 1 + "." + question.getDes() + "(" + question.getSource() + "分)", true);
                }

                @Override
                public void onFinshPlay() {
                    times.setText("已经完成");
                    tips.setText("");
                }
            }, WeChatUtils.getInstance().getCacheWeChatGroup());
        }
    }


}
