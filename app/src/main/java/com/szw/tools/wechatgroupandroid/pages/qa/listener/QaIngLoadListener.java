package com.szw.tools.wechatgroupandroid.pages.qa.listener;

import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;

import java.util.List;

/**
 * Created by SuZhiwei on 2017/9/23.
 */
public interface QaIngLoadListener {
    void onLoad(QaIng qaIng);
    void onError(int code);
}
