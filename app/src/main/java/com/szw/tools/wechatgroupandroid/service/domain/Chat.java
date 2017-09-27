package com.szw.tools.wechatgroupandroid.service.domain;

import java.io.Serializable;

/**
 * Created by shenmegui on 2017/9/27.
 */
public class Chat implements Serializable {
    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
