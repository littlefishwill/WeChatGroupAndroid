package com.szw.tools.wechatgroupandroid.pages.qa.listener;

import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;

/**
 * Created by SuZhiwei on 2017/9/23.
 */
public interface QaPlayListenerListener {
    void onReady(Questions questions,int current,long currentTime,String tips);
    void onTickChange(long tick,String tip);
    void onNext(Question question,int postaion);
    void onFinshPlay();
}
