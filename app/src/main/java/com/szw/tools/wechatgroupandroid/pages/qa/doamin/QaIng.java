package com.szw.tools.wechatgroupandroid.pages.qa.doamin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenmegui on 2017/9/26.
 */
public class QaIng implements Serializable{
    private Map<String,QaIng_Question> qaings;

    public Map<String, QaIng_Question> getQaings() {
        if(qaings==null){
            qaings = new HashMap<>();
        }
        return qaings;
    }

    public void setQaings(Map<String, QaIng_Question> qaings) {
        this.qaings = qaings;
    }
}
