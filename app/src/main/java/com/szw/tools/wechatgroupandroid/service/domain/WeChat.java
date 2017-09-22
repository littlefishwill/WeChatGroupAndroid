package com.szw.tools.wechatgroupandroid.service.domain;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class WeChat {
    private String name;
    private boolean isGroup;
    private int number;

    public String getName() {
        if (name.endsWith(")") && name.contains("(")){
            String[] split = name.split("\\(");
            if (split.length == 2) {
                return split[0];
            } else {
//                name.split("")
            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
