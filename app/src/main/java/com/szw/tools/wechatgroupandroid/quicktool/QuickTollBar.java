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
import com.szw.tools.wechatgroupandroid.db.ChatScoreDao;
import com.szw.tools.wechatgroupandroid.db.DbManager;
import com.szw.tools.wechatgroupandroid.pages.cq.CqManager;
import com.szw.tools.wechatgroupandroid.pages.cq.CqPlayListener;
import com.szw.tools.wechatgroupandroid.pages.qa.QaIngManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaPlayResultManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaRadomAskManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaUserAskManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QuestionsShowAvtivity;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaPlayListenerListener;
import com.szw.tools.wechatgroupandroid.pages.score.ScoreManager;
import com.szw.tools.wechatgroupandroid.pages.score.ScorePlayListener;
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
    private View cqContain,scContain,qaContain;
    public static QuickTollBar getInstance(Context context){
        if(quickTollBar==null) {
           quickTollBar = new QuickTollBar(context);
        }
        return quickTollBar;
    }

    private QuickTollBar(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.bar_quick_tool, null);

        //---qa
        titleQa = (TextView) view.findViewById(R.id.tv_qa_bar_title);
        tips = (TextView) view.findViewById(R.id.tv_qa_bar_tips);
        times = (TextView) view.findViewById(R.id.tv_qa_bar_time);
        qades = (TextView) view.findViewById(R.id.tv_qa_bar_qa_des);
        qaanswer = (TextView) view.findViewById(R.id.tv_qa_bar_answer);
        cqContain = view.findViewById(R.id.qa_bar_contain_cq);
        scContain = view.findViewById(R.id.qa_bar_contain_sc);
        qaContain= view.findViewById(R.id.qa_bar_contain);
        // -- qa over

        setGravity(Gravity.TOP|Gravity.RIGHT,10, DpOrPx.dip2px(WeChatAdnroidGroup.getInstance(),50));
        setDuration(LENGTH_LONG);
        // question logic
//        QaLogic(view);

        setView(view);
    }

    private TextView titleQa,tips,times,qades,qaanswer;

    private void QaLogic(View view) {
        if(!QaManager.getInstance().isOpen()){
            qaContain.setVisibility(View.GONE);
            return;
        }

        qaContain.setVisibility(View.VISIBLE);

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

        if(QaUserAskManager.getInstance().isOpen()){
            QaUserAskManager.getInstance().cancle();
        }

        if(QaRadomAskManager.getInstance().isOpen()){
            QaRadomAskManager.getInstance().cancle();
        }


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
        //--score
        ScoreLogic();

        show();
    }

    private void ScoreLogic() {
        if(ScoreManager.getInstance().isOpen()){
            scContain.setVisibility(View.VISIBLE);
        }else{
            scContain.setVisibility(View.GONE);
        }
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
        // --- qa player custom

        if(!QaManager.getInstance().isOpen()){
            return;
        }else{
            titleQa.setText("您没有开启任何答题题库");
            qades.setText("请前往题库界面，点击任意红色开始按钮。");
            tips.setText("请选择题库");
            times.setText("--");
        }

        // custom qa
        if(QaIngManager.getInstance().getQaNowQuestions()!=null) {
            QaIngManager.getInstance().getQaPlayer().play(new QaPlayListenerListener() {
                @Override
                public void onReady(Questions questions, int current, long currentTime, String tip) {
                    Questions qaNowQuestions = QaIngManager.getInstance().getQaNowQuestions();
                    titleQa.setText(qaNowQuestions.getTitle()+"("+qaNowQuestions.getQuestions().size()+")");
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

        // user ask
        if(QaUserAskManager.getInstance().isOpen()){
            titleQa.setText("自助请求提问");
            tips.setText("等待抽题");
            times.setText("");
            qades.setText("等待发送“抽题”命令");
            qaanswer.setText("");
            QaUserAskManager.getInstance().play(new QaUserAskManager.QaUserAskManagerListener() {
                private long time;
                @Override
                public void onReadyQusetions(Questions questions, Question question,Chat chat,int choosePos) {
                    WeChatUtils.getInstance().sendText("@"+chat.getName()+"抽到题（"+questions.getTitle()+"中的第"+(choosePos+1)+"题.\r\n题目:"+question.getDes()+"("+question.getSource()+"分) 限时"+TimeFormatUtils.formatSeconds(question.getTime()/1000),true);
                    titleQa.setText(questions.getTitle()+"中的第"+(choosePos+1)+"题）");
                    qades.setText(question.getDes());
                    tips.setText("剩余");
                    qaanswer.setText("答案：" + QuestionsShowAvtivity.getString(question.getType2Answer()));
                }

                @Override
                public void onAnswerRight(Chat chat,Question question) {
                    ChatScoreDao chatScoreDao = new ChatScoreDao();
                    chatScoreDao.setChatName(chat.getName());
                    chatScoreDao.setGroupName(WeChatUtils.getInstance().getCacheWeChatGroup().getName());
                    chatScoreDao.setScore(question.getSource());
                    chatScoreDao.setQaResultId("ask");
                    chatScoreDao.setSocreTime(System.currentTimeMillis());
                    DbManager.getInstance().getLiteOrm().save(chatScoreDao);

                    titleQa.setText("自助请求提问");
                    tips.setText("等待抽题");
                    times.setText("");
                    qades.setText("等待发送“抽题”命令");
                    qaanswer.setText("");

                    WeChatUtils.getInstance().sendText(chat.getName()+"回答正确！得"+question.getSource()+"分。\r\n 进行下一题问答。~",true);
                }

                @Override
                public void onAnswerFaild(Chat weChat,Question question) {

                    WeChatUtils.getInstance().sendText(weChat.getName()+"回答错误!\r\n剩余时间:"+TimeFormatUtils.formatSeconds(time/1000),true);
                }

                @Override
                public void onError(Chat weChat, String msg, int errorCode) {
                    if(errorCode==3){
                        WeChatUtils.getInstance().sendText(msg,true);
                        return;
                    }
                    tips.setText("错误："+msg);
                }

                @Override
                public void onFinish() {
                    titleQa.setText("随机抽题提问");
                    tips.setText("等待抽题");
                    times.setText("");
                    qades.setText("等待发送“抽题”命令");
                    qaanswer.setText("");
                    WeChatUtils.getInstance().sendText("时间到此题无人回答正确,\r\n可以发送“抽题”继续请求下一道题目。",true);
                }

                @Override
                public void onTick(long time) {
                    this.time  = time;
                    times.setText(TimeFormatUtils.formatSecondsUseCode(time / 1000));
                }
            });
        }

        // radom ask
        if(QaRadomAskManager.getInstance().isOpen()){
            titleQa.setText("随机提问开启");
            tips.setText("开始倒计时");
            times.setText("");
            qades.setText("即将开始自动随机抽题问答");
            qaanswer.setText("");
            QaRadomAskManager.getInstance().play(new QaRadomAskManager.QaRadomAskManagerListener() {
                private long time;
                @Override
                public void onReadyQusetions(Questions questions, Question question, int choosePos) {
                    WeChatUtils.getInstance().sendText("随机提问:《"+questions.getTitle()+"中的第"+(choosePos+1)+"题》\r\n题目:"+question.getDes()+"("+question.getSource()+"分) 限时"+TimeFormatUtils.formatSeconds(question.getTime()/1000),true);
                    titleQa.setText(questions.getTitle()+"中的第"+(choosePos+1)+"题");
                    qades.setText(question.getDes());
                    tips.setText("剩余");
                    qaanswer.setText("答案：" + QuestionsShowAvtivity.getString(question.getType2Answer()));
                }

                @Override
                public void onAnswerRight(Chat chat, Question question) {
                    ChatScoreDao chatScoreDao = new ChatScoreDao();
                    chatScoreDao.setChatName(chat.getName());
                    chatScoreDao.setGroupName(WeChatUtils.getInstance().getCacheWeChatGroup().getName());
                    chatScoreDao.setScore(question.getSource());
                    chatScoreDao.setQaResultId("ask");
                    chatScoreDao.setSocreTime(System.currentTimeMillis());
                    DbManager.getInstance().getLiteOrm().save(chatScoreDao);

                    titleQa.setText("自助请求提问");
                    tips.setText("等待抽题");
                    times.setText("");
                    qades.setText("等待发送“抽题”命令");
                    qaanswer.setText("");

                    WeChatUtils.getInstance().sendText(chat.getName()+"回答正确！得"+question.getSource()+"分。\r\n 即将发送下一道题目~",true);
                }

                @Override
                public void onAnswerFaild(Chat weChat, Question question) {
                    WeChatUtils.getInstance().sendText(weChat.getName()+"回答错误!\r\n剩余时间:"+TimeFormatUtils.formatSeconds(time/1000)+"?"+weChat.getMessage(),true);
                }

                @Override
                public void onError(String msg, int errorCode) {
                    if(errorCode==3){
                        WeChatUtils.getInstance().sendText(msg,true);
                        return;
                    }
                    tips.setText("错误："+msg);
                }

                @Override
                public void onFinish() {
                    WeChatUtils.getInstance().sendText("时间到此题无人回答正确,\r\n继续提问下一道题目。",true);
                }

                @Override
                public void onTick(long time) {
                    this.time = time;
                    times.setText(TimeFormatUtils.formatSecondsUseCode(time / 1000));
                }
            });
        }
    }


}
