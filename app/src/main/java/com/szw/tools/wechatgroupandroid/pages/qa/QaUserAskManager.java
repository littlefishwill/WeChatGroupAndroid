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
import com.szw.tools.wechatgroupandroid.service.domain.WeChat;
import com.szw.tools.wechatgroupandroid.utils.Sputils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shenmegui on 2017/10/13.
 */
public class QaUserAskManager extends Manager {
    private static  final String openSwitch = "qauserask";
    private static  boolean cacheOpen = false;
    private static  QaUserAskManager qaUserAskManager;
    private QaUserAskManagerListener qaUserAskManagerListener;
    private Question playingQuesions;
    private String[] comond =new String[]{"抽题","来题","来题目","我要答题"};
    private CountDownTimer countDownTimer;
    public static QaUserAskManager getInstance(){
        if(qaUserAskManager==null){
            qaUserAskManager = new QaUserAskManager();
        }
        return qaUserAskManager;
    }

    @Override
    public String getSpNameSpec() {
        return "QaUserAskManager";
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

    public void play(QaUserAskManagerListener qaUserAskManagerListener){
        this.qaUserAskManagerListener = qaUserAskManagerListener;
    }

    public interface QaUserAskManagerListener{
        void onReadyQusetions(Questions questions,Question question,Chat chat,int choosePos);
        void onAnswerRight(Chat weChat,Question question);
        void onAnswerFaild(Chat weChat,Question question);
        void onError(Chat weChat,String msg,int errorCode);
        void onFinish();
        void onTick(long time);
    }

    public void onRevceive(final Chat chat){
        Log.e("Enter","Enter1");
        if(!isOpen() || !QaManager.getInstance().isOpen()){
            return;
        }

        Log.e("Enter","Enter2");
        synchronized (QaUserAskManager.this) {

            boolean isComond = false;
            for(String s:comond){
                if(s.equals(chat.getMessage().trim())){
                    isComond = true;
                    break;
                }
            }


            if(isComond){
                Log.e("Enter","Enter3");
            }else{
                if(playingQuesions!=null){
                    String[] type2Answer = playingQuesions.getType2Answer();
                    for(int i = 0;i<type2Answer.length;i++){
                        if(chat.getMessage().equals(type2Answer[i])){
                            if (qaUserAskManagerListener != null) {
                                qaUserAskManagerListener.onAnswerRight(chat,playingQuesions);
                                if(countDownTimer!=null){
                                    countDownTimer.cancel();
                                }
                                playingQuesions = null;
                            }
                            return;
                        }
                    }

                    if (qaUserAskManagerListener != null) {
                        qaUserAskManagerListener.onAnswerFaild(chat,playingQuesions);
                    }
                    return;

                }else{
                    return;
                }
            }

            if(playingQuesions!=null){
                if (qaUserAskManagerListener != null) {
                    qaUserAskManagerListener.onError(chat, "@"+chat.getName()+"请等待当前题目结束，正在提问的题目为:\r\n"+playingQuesions.getDes(), 3);
                    return;
                }
            }

            Map<String, QaChoose> userAskChooseLib = DbManager.getInstance().getUserAskChooseLib();

            if (userAskChooseLib.size() < 1) {
                if (qaUserAskManagerListener != null) {
                    qaUserAskManagerListener.onError(chat, "没有选择任何题库，请前往选择题库", 0);
                }
                return;
            }

            List<Questions> readyChooseLib = new ArrayList<>();
            Iterator<Map.Entry<String, QaChoose>> iterator = userAskChooseLib.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, QaChoose> next = iterator.next();
                Questions questions = QaManager.getInstance().getQuestionsWithId(next.getKey());
                if(questions!=null && questions.getQuestions()!=null) {
                    if (questions.getQuestions().size() > 0) {
                        readyChooseLib.add(questions);
                    }
                }
            }

            if (readyChooseLib.size() < 1) {
                if (qaUserAskManagerListener != null) {
                    qaUserAskManagerListener.onError(chat, "选中题库为空题库，请添加题目", 1);
                }
                return;
            }

            Random randomCq = new Random();
            int radomLibPos = randomCq.nextInt(readyChooseLib.size()) % (readyChooseLib.size() - 0 + 1) + 0;

            Questions questions = readyChooseLib.get(radomLibPos);
            int radomQuesionsPos = randomCq.nextInt(questions.getQuestions().size()) % (questions.getQuestions().size() - 0 + 1) + 0;
            Question question = questions.getQuestions().get(radomQuesionsPos);

            playingQuesions = question;

            if (qaUserAskManagerListener != null) {
                qaUserAskManagerListener.onReadyQusetions(questions, question,chat,radomQuesionsPos);
            }

            countDownTimer = new CountDownTimer(question.getTime(),1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (qaUserAskManagerListener != null) {
                        qaUserAskManagerListener.onTick(millisUntilFinished);
                    }
                }

                @Override
                public void onFinish() {
                    playingQuesions=null;
                    if (qaUserAskManagerListener != null) {
                        qaUserAskManagerListener.onFinish();
                    }
                }
            };
            countDownTimer.start();


        }

    }


}
