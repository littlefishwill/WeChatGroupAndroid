package com.szw.tools.wechatgroupandroid.pages.qa;

import android.os.CountDownTimer;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng_Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaIngLoadListener;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaPlayListenerListener;
import com.szw.tools.wechatgroupandroid.service.domain.WeChat;

/**
 * Created by shenmegui on 2017/9/26.
 */
public class QaPlayer {
    private int currentPlayPos;
    private Questions qaNowQuestions;
    private QaPlayListenerListener qaPlayListenerListener;
    private WeChat weChat;
    private QaIng qaIng;
    public void play(QaPlayListenerListener qapl, WeChat wct){
        this.qaPlayListenerListener = qapl;
        this.weChat = wct;
        QaIngManager.getInstance().loadQaIng(new QaIngLoadListener() {
            @Override
            public void onLoad(QaIng qaIng) {
                QaPlayer.this.qaIng = qaIng;
                qaNowQuestions = QaIngManager.getInstance().getQaNowQuestions();
                QaIng_Question qaIng_question = qaIng.getQaings().get(weChat.getName());
                currentPlayPos = -1;
                long progressTime = 0;
                String tips = "问答倒计时:";
                if(qaIng_question!=null){
                    currentPlayPos = qaIng_question.getCurrent();
                    progressTime = qaIng_question.getUseTime();
//                    Question question = qaNowQuestions.getQuestions().get(currentPlayPos);
                    tips = "下一题倒计时";
                }else{
                    progressTime =10;
                }
                qaPlayListenerListener.onReady(qaNowQuestions,currentPlayPos,progressTime,tips);

                playGoOn(qaNowQuestions.getQuestions().get(currentPlayPos==-1?0:currentPlayPos),0);

            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public void quitePlay(){
        countDownTimer.cancel();
    }

    private CountDownTimer countDownTimer;
    private QaIng_Question qaIng_question;
    private Question question;
    private void playGoOn(Question question, long aleryPlay){
        this.question = question;
        qaPlayListenerListener.onNext(question);
         qaIng_question = new QaIng_Question();
         qaIng_question.setCurrent(currentPlayPos);

         countDownTimer = new CountDownTimer(question.getTime()-aleryPlay, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                qaIng_question.setUseTime(millisUntilFinished);
                qaPlayListenerListener.onTickChange(millisUntilFinished,"timeChange");
            }

            @Override
            public void onFinish() {
                currentPlayPos = currentPlayPos+1;
                if(qaNowQuestions.getQuestions().size()>currentPlayPos){
                    Question questionNext = qaNowQuestions.getQuestions().get(currentPlayPos);
                    playGoOn(questionNext,0);
                }
            }
        };
        countDownTimer.start();
    }
}
