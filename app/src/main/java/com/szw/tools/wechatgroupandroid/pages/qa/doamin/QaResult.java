package com.szw.tools.wechatgroupandroid.pages.qa.doamin;

import com.szw.tools.wechatgroupandroid.service.domain.WeChat;
import java.io.Serializable;
import java.util.List;

/**
 * Created by shenmegui on 2017/9/28.
 */
public class QaResult implements Serializable {

    /**
     * 题库id
     */
    private String id;
    private String qaLibraryId;
    private String qaLibraryName;
    private long StartTime;
    private long endTime;
    private WeChat weChat;
    private List<QaResultItem> qaResultItems;
    private String qaLibraryAuthorName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQaLibraryId() {
        return qaLibraryId;
    }

    public void setQaLibraryId(String qaLibraryId) {
        this.qaLibraryId = qaLibraryId;
    }

    public String getQaLibraryName() {
        return qaLibraryName;
    }

    public void setQaLibraryName(String qaLibraryName) {
        this.qaLibraryName = qaLibraryName;
    }

    public long getStartTime() {
        return StartTime;
    }

    public void setStartTime(long startTime) {
        StartTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public WeChat getWeChat() {
        return weChat;
    }

    public void setWeChat(WeChat weChat) {
        this.weChat = weChat;
    }

    public List<QaResultItem> getQaResultItems() {
        return qaResultItems;
    }

    public void setQaResultItems(List<QaResultItem> qaResultItems) {
        this.qaResultItems = qaResultItems;
    }

    public String getQaLibraryAuthorName() {
        return qaLibraryAuthorName;
    }

    public void setQaLibraryAuthorName(String qaLibraryAuthorName) {
        this.qaLibraryAuthorName = qaLibraryAuthorName;
    }
}
