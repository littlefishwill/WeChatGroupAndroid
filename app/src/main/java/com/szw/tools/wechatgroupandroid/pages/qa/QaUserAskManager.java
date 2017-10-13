package com.szw.tools.wechatgroupandroid.pages.qa;

import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.utils.Sputils;

/**
 * Created by shenmegui on 2017/10/13.
 */
public class QaUserAskManager extends Manager {
    private static  final String openSwitch = "qauserask";
    private static  boolean cacheOpen = false;
    private static  QaUserAskManager qaUserAskManager;
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
        cacheOpen = Sputils.getInstance(WeChatAdnroidGroup.getInstance()).getField(this, openSwitch, true);
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



}
