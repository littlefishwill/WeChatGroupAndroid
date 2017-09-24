package com.szw.tools.wechatgroupandroid.pages.qa.doamin;

import java.io.Serializable;

/**
 * 题目
 */
public class Question implements Serializable {
    public static final int TYPE_CHOOSE=1;
    public static final int TYPE_QA=2;
    /**
     * 题库标题
     */
    private String des;

    /**
     * 问题类型，1=选择题，2=问答题。
     */
    private int type;

    /**
     * 选择题答案数组
     */
    private String[] type1Answer;

    /**
     * 选择题正确答案角标
     */
    private int type1RightAnswer;

    /**
     * 问答题 结果
     */
    private String[] type2Answer;

    /**
     * 分值
     */
    private int source;
    /**
     * 用时
     */
    private long time;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String[] getType2Answer() {
        return type2Answer;
    }

    public void setType2Answer(String[] type2Answer) {
        this.type2Answer = type2Answer;
    }

    public int getType1RightAnswer() {
        return type1RightAnswer;
    }

    public void setType1RightAnswer(int type1RightAnswer) {
        this.type1RightAnswer = type1RightAnswer;
    }

    public String[] getType1Answer() {
        return type1Answer;
    }

    public void setType1Answer(String[] type1Answer) {
        this.type1Answer = type1Answer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
