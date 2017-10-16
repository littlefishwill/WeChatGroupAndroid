package com.szw.tools.wechatgroupandroid.pages.qa;

import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.utils.Sputils;

/**
 * Created by shenmegui on 2017/10/13.
 */
public class QaRadomAskManager extends Manager {
    private static  final String openSwitch = "qaradomask";
    private static  boolean cacheOpen = false;
    private static QaRadomAskManager qaUserAskManager;
    public static QaRadomAskManager getInstance(){
        if(qaUserAskManager==null){
            qaUserAskManager = new QaRadomAskManager();
        }
        return qaUserAskManager;
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



}
