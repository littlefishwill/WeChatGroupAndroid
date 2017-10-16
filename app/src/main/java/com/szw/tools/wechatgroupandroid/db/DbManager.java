package com.szw.tools.wechatgroupandroid.db;

import android.database.Cursor;
import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenmegui on 2017/9/29.
 */
public class DbManager extends Manager {
    private static DbManager dbManager;
    public  LiteOrm liteOrm;

    public static DbManager getInstance(){
        if(dbManager==null){
            dbManager = new DbManager();
        }
        return  dbManager;
    }

    private DbManager() {
        init();
    }

    @Override
    public String getSpNameSpec() {
        return null;
    }

    @Override
    public void init() {
        if (liteOrm == null) {
//            DataBaseConfig dataBaseConfig = new DataBaseConfig(WeChatAdnroidGroup.getInstance());
//            dataBaseConfig.dbName = "liteorm.db";
//            dataBaseConfig.dbVersion = 101;
//            dataBaseConfig.debugged = true;
//            this.liteOrm  = LiteOrm.newCascadeInstance(dataBaseConfig);
            this.liteOrm = LiteOrm.newSingleInstance(WeChatAdnroidGroup.getInstance(), "liteorm.db");
        }
        liteOrm.setDebugged(true); // open the log
    }

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    public List<ChatScoreDao> getGroupSocre(){
        List<ChatScoreDao> chatScoreDaos = new ArrayList<>();
        try {
            Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select groupName,sum(score) as  score from chatScoreDao  group by groupName order by socreTime desc", null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("groupName"));
                int socre = cursor.getInt(cursor.getColumnIndex("score"));
                ChatScoreDao chatScoreDao = new ChatScoreDao();
                chatScoreDao.setGroupName(name);
                chatScoreDao.setScore(socre);
                chatScoreDaos.add(chatScoreDao);
            }
        }catch (Exception ex){
            return new ArrayList<ChatScoreDao>();
        }

        return chatScoreDaos;
//        return liteOrm.query(new QueryBuilder<ChatScoreDao>(ChatScoreDao.class).groupBy("groupName"));
    }

    public List<ChatScoreDao> getGroupInSCore(String groupName){
        List<ChatScoreDao> chatScoreDaos = new ArrayList<>();
        try {
            Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select chatName,sum(score) as  score from chatScoreDao  where groupName = '"+groupName+"'  group by chatName order by socreTime desc", null);
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("chatName"));
                int socre = cursor.getInt(cursor.getColumnIndex("score"));
                ChatScoreDao chatScoreDao = new ChatScoreDao();
                chatScoreDao.setChatName(name);
                chatScoreDao.setScore(socre);
                chatScoreDaos.add(chatScoreDao);
            }
        }catch (Exception e){

        }

        return  chatScoreDaos;
    }

    /**
     * 查询组，具体某场考试的分数。
     * @param groupName
     * @param qaId
     * @return
     */
    public List<ChatScoreDao> getGroupInSCore(String groupName,String qaId){
        List<ChatScoreDao> chatScoreDaos = new ArrayList<>();
        try {
            Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select chatName,sum(score) as  score from chatScoreDao  where groupName = '"+groupName+"' and qaResultId = '"+qaId+"' group by chatName order by socreTime desc", null);
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("chatName"));
                int socre = cursor.getInt(cursor.getColumnIndex("score"));
                ChatScoreDao chatScoreDao = new ChatScoreDao();
                chatScoreDao.setChatName(name);
                chatScoreDao.setScore(socre);
                chatScoreDaos.add(chatScoreDao);
            }
        }catch (Exception e){

        }

        return  chatScoreDaos;
    }

    /**
     * 查询组，某个成员的总分
     * @param groupName
     * @param chatname
     * @return
     */
    public List<ChatScoreDao> getChatScoreInGroup(String groupName,String chatname){
        List<ChatScoreDao> chatScoreDaos = new ArrayList<>();
        try {
            Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select chatName,sum(score) as  score from chatScoreDao  where groupName = '" + groupName + "' and chatName = '" + chatname + "' group by chatName order by socreTime desc", null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("chatName"));
                int socre = cursor.getInt(cursor.getColumnIndex("score"));
                ChatScoreDao chatScoreDao = new ChatScoreDao();
                chatScoreDao.setChatName(name);
                chatScoreDao.setScore(socre);
                chatScoreDaos.add(chatScoreDao);
            }
        }catch (Exception e){

        }
        return  chatScoreDaos;
    }

    /**
     * 查询组，用户提问选择的库
     * @return
     */
    public Map<String,QaChoose> getUserAskChooseLib(){

        Map<String,QaChoose> chatScoreDaos = new HashMap<>();
        try{
            Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select id,radomAsk,userAsk  from  qaChoose  where userAsk = 1", null);
            while (cursor.moveToNext()){
                QaChoose qaChoose = new QaChoose();
                qaChoose.setId(cursor.getString(cursor.getColumnIndex("id")));
                qaChoose.setRadomAsk(cursor.getInt(cursor.getColumnIndex("radomAsk")));
                qaChoose.setUserAsk(cursor.getInt(cursor.getColumnIndex("userAsk")));
                chatScoreDaos.put(qaChoose.getId(),qaChoose);
            }
        }catch (Exception e){

        }

        return  chatScoreDaos;
    }

    /**
     * 查询组 随机提问题库
     * @return
     */
    public Map<String,QaChoose> getRadomAskChooseLib(){
        Map<String,QaChoose> chatScoreDaos = new HashMap<>();
        try{
            Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select id,radomAsk,userAsk from qaChoose  where radomAsk = 1", null);
            while (cursor.moveToNext()){
                QaChoose qaChoose = new QaChoose();
                qaChoose.setId(cursor.getString(cursor.getColumnIndex("id")));
                qaChoose.setRadomAsk(cursor.getInt(cursor.getColumnIndex("radomAsk")));
                qaChoose.setUserAsk(cursor.getInt(cursor.getColumnIndex("userAsk")));
                chatScoreDaos.put(qaChoose.getId(),qaChoose);
            }
        }catch (Exception e){

        }
        return  chatScoreDaos;
    }

}
