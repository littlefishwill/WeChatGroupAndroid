package com.szw.tools.wechatgroupandroid.pages.qa.doamin;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by shenmegui on 2017/9/26.
 */
public class QaIng_Question implements Serializable{
    private int current;
    private long useTime;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }
}
