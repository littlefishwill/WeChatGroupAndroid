package com.szw.tools.wechatgroupandroid.user;

/**
 * Created by shenmegui on 2017/9/22.
 */
public class User {
    private boolean isOpen;
    private String userName;
    private String email;
    private String phone;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
