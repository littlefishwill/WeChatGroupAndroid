package com.szw.tools.wechatgroupandroid.db;

import android.database.Cursor;
import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;

import java.util.ArrayList;
import java.util.List;

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
            liteOrm = LiteOrm.newSingleInstance(WeChatAdnroidGroup.getInstance(), "liteorm.db");
        }
        liteOrm.setDebugged(true); // open the log
    }

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    public List<ChatScoreDao> getGroupSocre(){
        List<ChatScoreDao> chatScoreDaos = new ArrayList<>();
        Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select groupName,sum(score) as  score from chatdao  group by groupName order by socreTime desc", null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("groupName"));
            int socre = cursor.getInt(cursor.getColumnIndex("score"));
            ChatScoreDao chatScoreDao = new ChatScoreDao();
            chatScoreDao.setGroupName(name);
            chatScoreDao.setScore(socre);
            chatScoreDaos.add(chatScoreDao);
        }

        return chatScoreDaos;
//        return liteOrm.query(new QueryBuilder<ChatScoreDao>(ChatScoreDao.class).groupBy("groupName"));
    }

    public List<ChatScoreDao> getGroupInSCore(String groupName){
        List<ChatScoreDao> chatScoreDaos = new ArrayList<>();
        Cursor cursor = liteOrm.getReadableDatabase().rawQuery("select chatName,sum(score) as  score from chatdao  where groupName = '"+groupName+"'  group by chatName order by socreTime desc", null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("chatName"));
            int socre = cursor.getInt(cursor.getColumnIndex("score"));
            ChatScoreDao chatScoreDao = new ChatScoreDao();
            chatScoreDao.setChatName(name);
            chatScoreDao.setScore(socre);
            chatScoreDaos.add(chatScoreDao);
        }
        return  chatScoreDaos;
    }

}
