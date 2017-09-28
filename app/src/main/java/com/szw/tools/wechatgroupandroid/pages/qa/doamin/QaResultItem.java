package com.szw.tools.wechatgroupandroid.pages.qa.doamin;

import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.service.domain.WeChat;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shenmegui on 2017/9/28.
 */
public class QaResultItem implements Serializable {
    private Question question;
    private List<Chat> rightWechats;
    private List<Chat> errorWechats;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Chat> getRightWechats() {
        return rightWechats;
    }

    public void setRightWechats(List<Chat> rightWechats) {
        this.rightWechats = rightWechats;
    }

    public List<Chat> getErrorWechats() {
        return errorWechats;
    }

    public void setErrorWechats(List<Chat> errorWechats) {
        this.errorWechats = errorWechats;
    }
}
