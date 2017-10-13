package com.szw.tools.wechatgroupandroid.pages.score;

import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.db.ChatScoreDao;
import com.szw.tools.wechatgroupandroid.db.DbManager;
import com.szw.tools.wechatgroupandroid.pages.cq.CqPlayListener;
import com.szw.tools.wechatgroupandroid.pages.qa.QaPlayResultManager;
import com.szw.tools.wechatgroupandroid.service.WeChatUtils;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.utils.Sputils;

import java.util.List;

/**
 * Created by shenmegui on 2017/9/29.
 */
public class ScoreManager  extends Manager{
    private static  ScoreManager scoreManager;
    private static  final String openSwitch = "scswitch";
    private static  boolean cacheOpen = false;
    private ScorePlayListener scorePlayListener;
    public static ScoreManager getInstance (){
        if(scoreManager==null){
            scoreManager = new ScoreManager();
        }
        return scoreManager;
    }

    @Override
    public String getSpNameSpec() {
        return "ScoreManager";
    }

    @Override
    public void init() {
        cacheOpen = Sputils.getInstance(WeChatAdnroidGroup.getInstance()).getField(this, openSwitch, true);
    }

    public boolean isOpen(){
        return  cacheOpen;
    }

    public void open(boolean neddOpen){
        cacheOpen = neddOpen;
        Sputils.getInstance(WeChatAdnroidGroup.getInstance()).putFiled(this,openSwitch,neddOpen);
    }

    public void onReceive(Chat chat){
        if(cacheOpen){
            if(chat.getMessage().equals("查分") || chat.getMessage().equals("查分数")){
                List<ChatScoreDao> chatScoreInGroup = DbManager.getInstance().getChatScoreInGroup(WeChatUtils.getInstance().getCacheWeChatGroup().getName(), chat.getName());
                if(chatScoreInGroup.size()>0){
                    WeChatUtils.getInstance().sendText("@"+chat.getName()+":\r\n"+"你的历史总分为:"+chatScoreInGroup.get(0).getScore()+"分.",true);
                }else{
                    WeChatUtils.getInstance().sendText("@"+chat.getName()+":\r\n"+"你的历史总分为:0分.",true);
                }
            }

            if(chat.getMessage().equals("查排名")){
                sendQAResult(chat);
            }
        }
    }

    /**
     * 发送公布，考试结果
     */
    private void sendQAResult(Chat chat) {
        //---
        List<ChatScoreDao> groupInSCore = DbManager.getInstance().getGroupInSCore(WeChatUtils.getInstance().getCacheWeChatGroup().getName());
        StringBuilder socreBuilder = new StringBuilder();
        socreBuilder.append("@"+chat.getName()+"本群分数排名如下:\r\n");
        int i = 1;
        for(ChatScoreDao chatScoreDao:groupInSCore){
            socreBuilder.append("第"+i+"名:"+chatScoreDao.getChatName()+" 总分:"+chatScoreDao.getScore()+"\r\n");
        }
        String trim = socreBuilder.toString().trim();
        WeChatUtils.getInstance().sendText(trim,true);

    }

    public void play(ScorePlayListener scorePlayListener){
        this.scorePlayListener = scorePlayListener;
    }
}
