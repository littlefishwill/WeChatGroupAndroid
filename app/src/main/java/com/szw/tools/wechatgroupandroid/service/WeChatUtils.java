package com.szw.tools.wechatgroupandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
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
    private Chat cacheChat;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onGetMessage(AccessibilityEvent event,WeChatBaseListener<Chat> weChatBaseListener){
        if(cacheWeChatGroup!=null){
            // enter wechat tell
            switch (event.getEventType()){
                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                    Chat weChatTellRecode = getWeChatTellRecode(getRootInActiveWindow());
                    if(weChatTellRecode!=null){
                        weChatBaseListener.onGet(weChatTellRecode);
                    }
                    break;
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void sendText(String text,boolean isSend){
        if(cacheWeChatGroup!=null){
            AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
            List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a6g");

            if(accessibilityNodeInfosByViewId==null || accessibilityNodeInfosByViewId.size()<1){
                return;
            }

            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfosByViewId.get(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ClipboardManager clipboard = (ClipboardManager) WeChatAdnroidGroup.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("des", text);
                clipboard.setPrimaryClip(clip);
                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }

            if(isSend) {
                List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId1 = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a6m");
                if(accessibilityNodeInfosByViewId1.size()>0) {
                    AccessibilityNodeInfo accessibilityNodeInfo1 = accessibilityNodeInfosByViewId1.get(0);
                    if (accessibilityNodeInfo1.isClickable()) {
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
        List<AccessibilityNodeInfo> bqc = node.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a5j");
        if(bqc.size()>0){
            AccessibilityNodeInfo accessibilityNodeInfo = bqc.get(0);
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(accessibilityNodeInfo.getChildCount() - 1);
            List<AccessibilityNodeInfo> imageicoS = child.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/io");
            if(imageicoS.size()>0){
                AccessibilityNodeInfo imageico = imageicoS.get(0);
                CharSequence contentDescription = imageico.getContentDescription();
                cacheChat.setName(contentDescription.toString().replace("头像", ""));

                //----根据名称查询，首页文字变化
                List<AccessibilityNodeInfo> homeUserItemName = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ajc");
                List<AccessibilityNodeInfo> homeUserItemText = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aje");
                for(int i=0;i<homeUserItemName.size();i++){
                    if(homeUserItemName.get(i).getText().toString().trim().equals(cacheWeChatGroup.getName())){
                        String text = homeUserItemText.get(i).getText().toString();
                        if(cacheChat!=null && cacheChat.getMessage()!=null && cacheChat.getMessage().equals(text)){
                            // --- 过滤重复信息
//                            openKeyBord(true);
                            return null;
                        }
//                        Toast.makeText(WeChatAdnroidGroup.getInstance(),homeUserItemName.get(i).getText()+"="+i+ text,Toast.LENGTH_LONG).show();

                        cacheChat.setMessage(text);
//                        getRootInActiveWindow().
//                        getRootInActiveWindow().refresh();

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
