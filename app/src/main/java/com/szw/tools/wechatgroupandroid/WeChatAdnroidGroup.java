package com.szw.tools.wechatgroupandroid;

import android.app.Application;

import com.szw.tools.wechatgroupandroid.db.DbManager;
import com.szw.tools.wechatgroupandroid.pages.cq.CqManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaRadomAskManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaUserAskManager;
import com.szw.tools.wechatgroupandroid.pages.score.ScoreManager;

/**
 * Created by shenmegui on 2017/9/21.
 */
public class WeChatAdnroidGroup extends Application {
    private static WeChatAdnroidGroup weChatAdnroidGroup;
    public static WeChatAdnroidGroup getInstance(){
        return weChatAdnroidGroup;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        weChatAdnroidGroup = this;
        QaManager.getInstance().init();
        QaUserAskManager.getInstance().init();
        QaRadomAskManager.getInstance().init();

        CqManager.getInstance().init();
        ScoreManager.getInstance().init();
    }

}
