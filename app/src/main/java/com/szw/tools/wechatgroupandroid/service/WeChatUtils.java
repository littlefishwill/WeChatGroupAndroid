package com.szw.tools.wechatgroupandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.szw.tools.wechatgroupandroid.service.domain.GroupChat;
import com.szw.tools.wechatgroupandroid.service.domain.WeChat;

import java.util.List;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class WeChatUtils {
    private static WeChatUtils weChatUtils;
    private boolean isEnterWechat = false;
    private WeChat cacheWeChatGroup;
    private AccessibilityService accessibilityService;
    private WeChatBaseListener<WeChat> weChatBaseListener;
    public static WeChatUtils getInstance(){
        if(weChatUtils==null){
            weChatUtils = new WeChatUtils();
        }
        return weChatUtils;
    }

    private WeChatUtils() {

    }

    public void initWeChat(AccessibilityService accessibilityService,AccessibilityEvent event,WeChatBaseListener weChatBaseListener){
        this.accessibilityService = accessibilityService;
        if(event.getEventType()==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            if(event.getClassName().toString().equals("com.tencent.mm.ui.LauncherUI")) {
                isEnterWechat = true;
//                if(weChatBaseListener!=null){
//                    weChatBaseListener.onGet(new Object());
//                }
            }else{
                if(weChatBaseListener!=null){
                    weChatBaseListener.onExit();
                }
                // 防止直接点home按键触发的返回
                if(weChatBaseListener!=null && cacheWeChatGroup!=null){
                    cacheWeChatGroup = null;
                    weChatBaseListener.onExit();
                }
                isEnterWechat = false;
            }
        }
    }

    /**
     * 监听，当进入微信去聊界面
     * @param event
     * @param weChatBaseListener
     */
    private String cacheGroupName;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onEnterTell(AccessibilityEvent event, WeChatBaseListener<WeChat> weChatBaseListener){
        this.weChatBaseListener = weChatBaseListener;
        if(isEnterWechat) {
            switch (event.getEventType()) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    checkIsInChatPage(weChatBaseListener);
                    break;
                case AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE:
                    if (event.getClassName().equals("android.widget.LinearLayout")) {
                        checkIsInChatPage(weChatBaseListener);
                    }
                    break;
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    if (cacheWeChatGroup!=null && event.getClassName().equals("android.widget.TextView")) {
                        checkIsInChatPage(weChatBaseListener);
                    }
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void checkIsInChatPage(WeChatBaseListener<WeChat> weChatBaseListener) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        // 微信首页 第一个元素为资第一 view.，聊天界面第一个节点为linnerlayout
        List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId1 = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gz");
        if (accessibilityNodeInfosByViewId1.size() > 0) {
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfosByViewId1.get(0);
            if(cacheWeChatGroup==null) {
                cacheWeChatGroup = new WeChat();
                cacheWeChatGroup.setName(accessibilityNodeInfo.getText().toString().trim());
                weChatBaseListener.onGet(cacheWeChatGroup);
            }
         }else{
            if(cacheWeChatGroup!=null) {
                weChatBaseListener.onExit();
            }
            cacheWeChatGroup = null;
         }
    }

    public void onGetMessage(AccessibilityEvent event,WeChatBaseListener<GroupChat> weChatBaseListener){

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public AccessibilityNodeInfo getRootInActiveWindow(){
       return  accessibilityService.getRootInActiveWindow();
    }

    public interface   WeChatBaseListener<T>{
        void  onGet(T object);
        void onExit();
    }
}
