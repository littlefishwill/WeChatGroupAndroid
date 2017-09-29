package com.szw.tools.wechatgroupandroid.db;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;

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
        return liteOrm.query(new QueryBuilder<ChatScoreDao>(ChatScoreDao.class).groupBy("groupName"));
    }
}
