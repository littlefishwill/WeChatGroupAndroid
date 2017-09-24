package com.szw.tools.wechatgroupandroid.service.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class WeChat {
    private String name;
    private boolean isGroup;
    private int number;

    public String getName() {
        if (name.endsWith(")") && name.contains("(")){
            Pattern p1 = Pattern.compile("\\S+\\((\\d+)\\)$");
            Matcher m = p1.matcher(name);
            if(m.find())
            {
                isGroup = true;
                number = Integer.parseInt(m.group(1));
                System.out.println(m.group(1));
                int start = m.start(1);
                String groupName = name.substring(0,start-1);
                return groupName;
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
