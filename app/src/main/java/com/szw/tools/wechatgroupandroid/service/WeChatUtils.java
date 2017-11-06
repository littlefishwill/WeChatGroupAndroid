package com.szw.tools.wechatgroupandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.service.domain.GroupChat;
import com.szw.tools.wechatgroupandroid.service.domain.WeChat;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class WeChatUtils {

    private static String WECHAT_ID_CHAT_TITLE_TEXT = "com.tencent.mm:id/h2"; //聊天界面-title
    private static String WECHAT_ID_CHAT_ENTER_TEXT = "com.tencent.mm:id/a71";//聊天界面-输入框
    private static String WECHAT_ID_CHAT_SEND ="com.tencent.mm:id/a77";//聊天界面-发送按钮
    private static String WECHAT_ID_CHAT_LIST="com.tencent.mm:id/a64"; //聊天界面-对话列表
    private static String WECHAT_ID_CHAT_LIST_ITEM_ICO="com.tencent.mm:id/iw"; //聊天界面-会话条目-Image
    private static String WECHAT_ID_CHATLIST_LIST_ITEM_NAME_TEXT="com.tencent.mm:id/ak1"; //聊天列表界面-会话条目-Image
    private static String WECHAT_ID_CHATLIST_LIST_ITEM_CONTENT_TEXT="com.tencent.mm:id/ak3"; //聊天列表界面-会话条目-Image

    private static WeChatUtils weChatUtils;
    private boolean isEnterWechat = false;
    private WeChat cacheWeChatGroup;
    private AccessibilityService accessibilityService;
    private WeChatBaseListener<WeChat> weChatBaseListener;
    private Chat cacheChat;
    private List<String> filterOwerChat = new LinkedList<>();

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
                    if (cacheWeChatGroup!=null) {
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
        List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId1 = rootNode.findAccessibilityNodeInfosByViewId(WECHAT_ID_CHAT_TITLE_TEXT);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onGetMessage(AccessibilityEvent event,WeChatBaseListener<Chat> weChatBaseListener){
        if(cacheWeChatGroup!=null){
            // enter wechat tell
            switch (event.getEventType()){
                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                    Chat weChatTellRecode = getWeChatTellRecode(getRootInActiveWindow());
                    if(weChatTellRecode!=null){
                        if(!filterOwerSenndText(weChatTellRecode)) {
                            weChatBaseListener.onGet(weChatTellRecode);
                        }
                    }
                    break;
            }
        }
    }

    private boolean filterOwerSenndText(Chat weChatTellRecode) {
        boolean isSend  = false;

        for(String cacheStr:filterOwerChat){
            if(weChatTellRecode.getMessage().startsWith(cacheStr)){
                return true;
            }
        }


        return isSend;
    }

    private String cacheSendText = "";
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void sendText(String text,boolean isSend){

        if(cacheWeChatGroup!=null){
            AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
            List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId(WECHAT_ID_CHAT_ENTER_TEXT);

            if(accessibilityNodeInfosByViewId==null || accessibilityNodeInfosByViewId.size()<1){
                return;
            }

            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfosByViewId.get(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                if(text.trim().length()>0) {
                    cacheSendText = text;
                }

                ClipboardManager clipboard = (ClipboardManager) WeChatAdnroidGroup.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("des", text);
                clipboard.setPrimaryClip(clip);
                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);

            }

            if(isSend) {
                List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId1 = rootInActiveWindow.findAccessibilityNodeInfosByViewId(WECHAT_ID_CHAT_SEND);
                if(accessibilityNodeInfosByViewId1.size()>0) {
                    AccessibilityNodeInfo accessibilityNodeInfo1 = accessibilityNodeInfosByViewId1.get(0);
                    if (accessibilityNodeInfo1.isClickable()) {

                        if(filterOwerChat.size()>10){
                            filterOwerChat.remove(0);
                        }

                        String sendMessageCache = text;
                        if(sendMessageCache.length()>5){
                            sendMessageCache = sendMessageCache.substring(0,5);
                        }

                        filterOwerChat.add(sendMessageCache);
                        accessibilityNodeInfo1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void openKeyBord(boolean openKeBord){
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a6g");
        if(accessibilityNodeInfosByViewId!=null && accessibilityNodeInfosByViewId.size()>0) {
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfosByViewId.get(0);
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public AccessibilityNodeInfo getRootInActiveWindow(){
       return  accessibilityService.getRootInActiveWindow();
    }

    public interface   WeChatBaseListener<T>{
        void  onGet(T object);
        void onExit();
    }

    /**
     * 遍历所有控件，找到头像Imagview，里面有对联系人的描述
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private Chat getWeChatTellRecode(AccessibilityNodeInfo node) {
        if(cacheChat==null){
            cacheChat = new Chat();
        }

        List<AccessibilityNodeInfo> bqc = node.findAccessibilityNodeInfosByViewId(WECHAT_ID_CHAT_LIST);
        if(bqc.size()>0){
            AccessibilityNodeInfo accessibilityNodeInfo = bqc.get(0);
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(accessibilityNodeInfo.getChildCount() - 1);
            if(child==null){
                return null;
            }
            List<AccessibilityNodeInfo> imageicoS = child.findAccessibilityNodeInfosByViewId(WECHAT_ID_CHAT_LIST_ITEM_ICO);
            if(imageicoS.size()>0){
                AccessibilityNodeInfo imageico = imageicoS.get(0);
                CharSequence contentDescription = imageico.getContentDescription();
                String chatName = contentDescription.toString().replace("头像", "");
                cacheChat.setName(chatName);


                //----根据名称查询，首页文字变化
                List<AccessibilityNodeInfo> homeUserItemName = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(WECHAT_ID_CHATLIST_LIST_ITEM_NAME_TEXT);
                List<AccessibilityNodeInfo> homeUserItemText = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(WECHAT_ID_CHATLIST_LIST_ITEM_CONTENT_TEXT);
                for(int i=0;i<homeUserItemName.size();i++){
                    if(homeUserItemName.get(i).getText().toString().trim().equals(cacheWeChatGroup.getName())){

                        String text = homeUserItemText.get(i).getText().toString();

                        Log.i("WeChatUtils",text);
                        if(text.startsWith(chatName+":")){
                            text = text.split(":")[1];
                            Log.i("WeChatUtils","群聊分割名称后字段:"+text);
                        }

                        if(cacheChat!=null && cacheChat.getMessage()!=null && cacheChat.getMessage().equals(text)){
                            // --- 过滤重复信息
//                            openKeyBord(true);
                            cacheChat.setMessage(text);
                            return null;
                        }

                        // --过滤发送提示语
                        if(cacheSendText.equals(text)){
                            cacheChat.setMessage(text);
                            return null;
                        }

                        if(text.contains("回答正确") || text.contains("回答错误") || text.startsWith("随机提问:《")){
                            cacheChat.setMessage(text);
                                return  null;
                        }

                        cacheChat.setMessage(text);

                        sendText("",false);
                        Chat chat = new Chat();
                        chat.setMessage(cacheChat.getMessage());
                        chat.setName(cacheChat.getName());
                        return chat;

                    }
                }
            }
        }

        return null;
        // --- find last recode
    }


    public WeChat getCacheWeChatGroup() {
        return cacheWeChatGroup;
    }
}
