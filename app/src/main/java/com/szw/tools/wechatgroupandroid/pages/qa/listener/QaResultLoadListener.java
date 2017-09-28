package com.szw.tools.wechatgroupandroid.pages.qa.listener;

import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaResult;

import java.util.List;

/**
 * Created by shenmegui on 2017/9/28.
 */
public interface QaResultLoadListener {
    void onLoad(List<QaResult> qaResults);
    void onFialed(int code);
}
