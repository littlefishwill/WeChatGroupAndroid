package com.szw.tools.wechatgroupandroid.service;

import android.view.accessibility.AccessibilityEvent;
import com.szw.tools.wechatgroupandroid.service.domain.GroupChat;
import com.szw.tools.wechatgroupandroid.service.domain.WeChatGroup;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class WeChatUtils {
    private static WeChatUtils weChatUtils;
    public static WeChatUtils getInstance(){
        if(weChatUtils==null){
            weChatUtils = new WeChatUtils();
        }
        return weChatUtils;
    }
    private WeChatUtils() {
    }

    /**
     * 监听，当进入微信去聊界面
     * @param event
     * @param weChatBaseListener
     */
    public void onEnterGroup(AccessibilityEvent event,WeChatBaseListener<WeChatGroup> weChatBaseListener){

    }

    public void onGetMessage(AccessibilityEvent event,WeChatBaseListener<GroupChat> weChatBaseListener){

    }



    public interface WeChatBaseListener<T>{
        void onGet(T object);
    }
}
