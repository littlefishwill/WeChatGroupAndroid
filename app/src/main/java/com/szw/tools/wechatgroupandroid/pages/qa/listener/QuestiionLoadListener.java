package com.szw.tools.wechatgroupandroid.pages.qa.listener;

import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;

import java.util.List;

/**
 * Created by SuZhiwei on 2017/9/23.
 */
public interface QuestiionLoadListener {
    void onLoad(int count,List<Questions> questionses);
    void onError(int code);
}
