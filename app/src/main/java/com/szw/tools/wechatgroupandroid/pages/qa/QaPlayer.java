package com.szw.tools.wechatgroupandroid.pages.qa;

import android.os.CountDownTimer;

import com.szw.tools.wechatgroupandroid.db.ChatScoreDao;
import com.szw.tools.wechatgroupandroid.db.DbManager;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng_Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaIngLoadListener;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaPlayListenerListener;
import com.szw.tools.wechatgroupandroid.service.WeChatUtils;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.service.domain.WeChat;
import com.szw.tools.wechatgroupandroid.utils.TimeFormatUtils;

import java.util.List;

/**
 * Created by shenmegui on 2017/9/26.
 */
public class QaPlayer {
    private int currentPlayPos;
    private Questions qaNowQuestions;
    private QaPlayListenerListener qaPlayListenerListener;
    private WeChat weChat;
    private QaIng qaIng;
    long progressTime = 0;
    public void play(QaPlayListenerListener qapl, WeChat wct){
        quitePlay();
        this.qaPlayListenerListener = qapl;
        this.weChat = wct;
        QaIngManager.getInstance().loadQaIng(new QaIngLoadListener() {
            @Override
            public void onLoad(QaIng qaIng) {
                QaPlayer.this.qaIng = qaIng;
                qaNowQuestions = QaIngManager.getInstance().getQaNowQuestions();
                final QaIng_Question qaIng_question = qaIng.getQaings().get(weChat.getName());
                currentPlayPos = 0;
                progressTime = 0;
                String tips = "开始倒计时:";
                if(qaIng_question!=null){
                    currentPlayPos = qaIng_question.getCurrent();
                    progressTime = qaIng_question.getUseTime();
//                    Question question = qaNowQuestions.getQuestions().get(currentPlayPos);
                    tips = "发送卷首:";
                }else{
                    progressTime =-1;
                }

                qaPlayListenerListener.onReady(qaNowQuestions, currentPlayPos, progressTime, tips);

                if(qaNowQuestions.getQuestions().size()==0){
                    qaPlayListenerListener.onFinshPlay();
                    return;
                }
                if(currentPlayPos>= qaNowQuestions.getQuestions().size()){
                    // --- 重新开始
                    currentPlayPos = 0;
                    progressTime = 0;
                }

                countDownTimer = new CountDownTimer(10000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(qaIng_question!=null&&(currentPlayPos>0|| progressTime>0)){
                            qaPlayListenerListener.onReady(qaNowQuestions, currentPlayPos, millisUntilFinished, "即将继续答题:"+(currentPlayPos+1)+".");
                        }else {
                            qaPlayListenerListener.onReady(qaNowQuestions, currentPlayPos, millisUntilFinished, "即将开始");
                        }
                    }

                    @Override
                    public void onFinish() {
                        //--- 记录 存储开始节点
                        QaPlayResultManager.getInstance().onStar(qaNowQuestions);
                        //--- 开始
                        playGoOn(qaNowQuestions.getQuestions().get(currentPlayPos==-1?0:currentPlayPos),progressTime);


                    }
                };
                countDownTimer.start();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public void quitePlay(){
        if(countDownTimer!=null) {
            countDownTimer.cancel();
            countDownTimer = null;
            savePlayProgress(keepTime);
        }
        QaPlayResultManager.getInstance().onStop();
        question = null;

    }

    private CountDownTimer countDownTimer;
    private QaIng_Question qaIng_question;
    private Question question;
    private long keepTime;
    private void playGoOn(Question question, long aleryPlay){
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }

        this.question = question;
        keepTime = question.getTime();
        savePlayProgress(question.getTime());

         qaPlayListenerListener.onNext(question,currentPlayPos);
        // 开始记录该题目的答题情况
         QaPlayResultManager.getInstance().onPlayIng(question);
         qaIng_question = new QaIng_Question();
         qaIng_question.setCurrent(currentPlayPos);

         countDownTimer = new CountDownTimer(aleryPlay<1?question.getTime():aleryPlay, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                keepTime = millisUntilFinished;
                qaIng_question.setUseTime(millisUntilFinished);
                qaPlayListenerListener.onTickChange(millisUntilFinished,"timeChange");
            }

            @Override
            public void onFinish() {
                playNext();
            }
        };
        countDownTimer.start();
    }

    private void playNext() {
        // -------current question play end
        QaPlayResultManager.getInstance().onPlayEnd();
        currentPlayPos = currentPlayPos+1;
        if(qaNowQuestions.getQuestions().size()>currentPlayPos){
            Question questionNext = qaNowQuestions.getQuestions().get(currentPlayPos);
            qaPlayListenerListener.onTickChange(0,"");
            playGoOn(questionNext,0);
        }else{
            sendQAResult();
            QaPlayResultManager.getInstance().onStop();
            countDownTimer.cancel();
            qaPlayListenerListener.onFinshPlay();
        }
    }

    /**
     * 发送公布，考试结果
     */
    private void sendQAResult() {
        //---
        List<ChatScoreDao> groupInSCore = DbManager.getInstance().getGroupInSCore(WeChatUtils.getInstance().getCacheWeChatGroup().getName(), QaPlayResultManager.getInstance().getQaResult().getId());
        StringBuilder socreBuilder = new StringBuilder();
        socreBuilder.append("本次问答结束，得分统计如下:\r\n");
        int i = 1;
        for(ChatScoreDao chatScoreDao:groupInSCore){
            socreBuilder.append("第"+i+"名:"+chatScoreDao.getChatName()+" 总分:"+chatScoreDao.getScore()+"\r\n");
        }
        String trim = socreBuilder.toString().trim();
        WeChatUtils.getInstance().sendText(trim,true);

    }

    private void savePlayProgress(long playtime) {
        qaIng_question = new QaIng_Question();
        qaIng_question.setCurrent(currentPlayPos);
        qaIng_question.setUseTime(playtime);
        qaIng.getQaings().put(weChat.getName(),qaIng_question);
        QaIngManager.getInstance().saveQaing(qaIng);
    }

    public void onReceive(Chat chat){
        if(question!=null){
            for(String answer:question.getType2Answer()){
                if(chat.getMessage().trim().toLowerCase().equals(answer.toLowerCase())){
                    onAnswerRight(chat,question);
                    return;
                }
            }
            onAnswerFail(chat,question);
        }
    }

    private void onAnswerRight(Chat chat,Question question){
        // 存储正确答案集合
        QaPlayResultManager.getInstance().onAnswer(chat, true);
        String tips;
        if(currentPlayPos>=qaNowQuestions.getQuestions().size()-1){
            tips = "当前为最后一道题目";
        }else{
            tips = "进入下一题";
        }


        ChatScoreDao  chatScoreDao = new ChatScoreDao();
        chatScoreDao.setChatName(chat.getName());
        chatScoreDao.setGroupName(WeChatUtils.getInstance().getCacheWeChatGroup().getName());
        chatScoreDao.setScore(question.getSource());
        chatScoreDao.setQaResultId(QaPlayResultManager.getInstance().getQaResult().getId());
        chatScoreDao.setSocreTime(System.currentTimeMillis());
        DbManager.getInstance().getLiteOrm().save(chatScoreDao);

        WeChatUtils.getInstance().sendText(chat.getName()+"回答正确！\r\n得"+question.getSource()+"分。\r\n"+tips,true);

        playNext();

    }

    private void onAnswerFail(Chat chat,Question question){
        QaPlayResultManager.getInstance().onAnswer(chat,false);
        WeChatUtils.getInstance().sendText("@"+chat.getName() + "回答错误.\r\n该问题剩余回答时间为:"+ TimeFormatUtils.formatSecondsUseCode(keepTime/1000), true);
    }



}
