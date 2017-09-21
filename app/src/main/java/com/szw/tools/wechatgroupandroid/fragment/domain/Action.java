package com.szw.tools.wechatgroupandroid.fragment.domain;

/**
 * Created by SuZhiwei on 2017/9/21.
 */
public class Action {
    private String name;
    private int actionId;
    private int icoResourceId;
    private boolean isOpen;

    public Action(String name, int actionId, int icoResourceId, boolean isOpen) {
        this.name = name;
        this.actionId = actionId;
        this.icoResourceId = icoResourceId;
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getIcoResourceId() {
        return icoResourceId;
    }

    public void setIcoResourceId(int icoResourceId) {
        this.icoResourceId = icoResourceId;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
