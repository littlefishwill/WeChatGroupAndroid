package com.szw.tools.wechatgroupandroid.user;

import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.utils.Sputils;

/**
 * Created by shenmegui on 2017/9/22.
 */
public class UserManager extends Manager {
    private static UserManager userManager;
    public static UserManager getInstance(){
        if(userManager==null){
            userManager = new UserManager();
        }
        return userManager;
    }
    private UserManager() {}

    @Override
    public String getSpNameSpec() {
        return "UserManager";
    }

    @Override
    public void init() {

    }

    // 缓存user
    private User cacheUser;

    public User getUser(){

        if(cacheUser!=null){
            return cacheUser;
        }
        User user = Sputils.getInstance(WeChatAdnroidGroup.getInstance()).getObject(this, User.class);
        if(user==null){
            user = new User();
        }
        cacheUser = user;
        return user;
    }

    public void updateUser(User user){
        cacheUser = user;
        Sputils.getInstance(WeChatAdnroidGroup.getInstance()).putObject(this,user);
    }


}
