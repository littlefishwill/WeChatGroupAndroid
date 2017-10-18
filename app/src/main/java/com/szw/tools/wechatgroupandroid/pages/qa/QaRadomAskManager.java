package com.szw.tools.wechatgroupandroid.pages.qa;

import android.os.CountDownTimer;
import android.util.Log;

import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.db.DbManager;
import com.szw.tools.wechatgroupandroid.db.QaChoose;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.utils.Sputils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by shenmegui on 2017/10/13.
 */
public class QaRadomAskManager extends Manager {
    private static  final String openSwitch = "qaradomask";
    private static  boolean cacheOpen = false;
    private static  QaRadomAskManager qaRadomAskManager;
    private QaRadomAskManagerListener qaRadomAskManagerListener;
    private Question playingQuesions;
    private String[] comond =new String[]{"抽题","来题","来题目","我要答题"};
    private CountDownTimer countDownTimer;
    public static QaRadomAskManager getInstance(){
        if(qaRadomAskManager==null){
            qaRadomAskManager = new QaRadomAskManager();
        }
        return qaRadomAskManager;
    }

    @Override
    public String getSpNameSpec() {
        return "QaRadomAskManager";
    }

    @Override
    public void init() {
        cacheOpen = Sputils.getInstance(WeChatAdnroidGroup.getInstance()).getField(this, openSwitch, false);
    }

    public boolean isOpen(){
        return  cacheOpen;
    }

    public void open(boolean neddOpen){
        cacheOpen = neddOpen;
        Sputils.getInstance(WeChatAdnroidGroup.getInstance()).putFiled(this,openSwitch,neddOpen);
    }

    public void save(String qaLibraryId){

    }

    public void delect(String qaLibraryId){

    }

    public void play(final QaRadomAskManagerListener qaRadomAskManagerListener){
        this.qaRadomAskManagerListener = qaRadomAskManagerListener;
        countDownTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(qaRadomAskManagerListener!=null){
                    qaRadomAskManagerListener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                raDomQuestions();
            }
        };
        countDownTimer.start();
    }

    public interface QaRadomAskManagerListener{
        void onReadyQusetions(Questions questions, Question question, int choosePos);
        void onAnswerRight(Chat weChat,Question question);
        void onAnswerFaild(Chat weChat,Question question);
        void onError(String msg,int errorCode);
        void onFinish();
        void onTick(long time);
    }

    public void onRevceive(final Chat chat){
        Log.e("Enter","Enter1");
        if(!isOpen() || !QaManager.getInstance().isOpen()){
            return;
        }

        if(playingQuesions==null){
            return;
        }

        Log.e("Enter","Enter2");
        synchronized (QaRadomAskManager.this) {

                if(playingQuesions!=null){
                    String[] type2Answer = playingQuesions.getType2Answer();
                    for(int i = 0;i<type2Answer.length;i++){
                        if(chat.getMessage().equals(type2Answer[i])){
                            if (qaRadomAskManagerListener != null) {
                                qaRadomAskManagerListener.onAnswerRight(chat,playingQuesions);
                                if(countDownTimer!=null){
                                    countDownTimer.cancel();
                                }
                                playingQuesions = null;
                                raDomQuestions();
                            }
                            return;
                        }
                    }

                    if (qaRadomAskManagerListener != null) {
                        qaRadomAskManagerListener.onAnswerFaild(chat,playingQuesions);
                    }
                    return;

                }else{
                    return;
                }

        }

    }

    public void raDomQuestions(){
        synchronized (this) {
            Map<String, QaChoose> userAskChooseLib = DbManager.getInstance().getRadomAskChooseLib();

            if (userAskChooseLib.size() < 1) {
                if (qaRadomAskManagerListener != null) {
                    qaRadomAskManagerListener.onError("没有选择任何题库，请前往选择题库", 0);
                }
                return;
            }

            List<Questions> readyChooseLib = new ArrayList<>();
            Iterator<Map.Entry<String, QaChoose>> iterator = userAskChooseLib.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, QaChoose> next = iterator.next();
                Questions questions = QaManager.getInstance().getQuestionsWithId(next.getKey());
                if (questions != null && questions.getQuestions() != null) {
                    if (questions.getQuestions().size() > 0) {
                        readyChooseLib.add(questions);
                    }
                }
            }

            if (readyChooseLib.size() < 1) {
                if (qaRadomAskManagerListener != null) {
                    qaRadomAskManagerListener.onError("选中题库为空题库，请添加题目", 1);
                }
                return;
            }

            Random randomCq = new Random();
            int radomLibPos = randomCq.nextInt(readyChooseLib.size()) % (readyChooseLib.size() - 0 + 1) + 0;

            Questions questions = readyChooseLib.get(radomLibPos);
            int radomQuesionsPos = randomCq.nextInt(questions.getQuestions().size()) % (questions.getQuestions().size() - 0 + 1) + 0;
            Question question = questions.getQuestions().get(radomQuesionsPos);

            playingQuesions = question;

            if (qaRadomAskManagerListener != null) {
                qaRadomAskManagerListener.onReadyQusetions(questions, question, radomQuesionsPos);
            }

            countDownTimer = new CountDownTimer(question.getTime(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (qaRadomAskManagerListener != null) {
                        qaRadomAskManagerListener.onTick(millisUntilFinished);
                    }
                }

                @Override
                public void onFinish() {
                    playingQuesions = null;
                    if (qaRadomAskManagerListener != null) {
                        qaRadomAskManagerListener.onFinish();
                    }

                    raDomQuestions();
                }
            };
            countDownTimer.start();
        }
    }

    public void cancle(){
        if(countDownTimer!=null) {
            countDownTimer.cancel();
        }
        playingQuesions = null;
    }


}
