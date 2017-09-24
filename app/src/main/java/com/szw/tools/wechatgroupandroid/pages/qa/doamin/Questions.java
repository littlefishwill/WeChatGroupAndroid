package com.szw.tools.wechatgroupandroid.pages.qa.doamin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 题库
 */
public class Questions implements Serializable {
    private String id;

    public Questions() {
        id = UUID.randomUUID().toString();
    }

    /**
     * 题库标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 创建时间
     */
    private Long crreatTime;

    /**
     * 所有题目
     */
    private List<Question> questions;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getCrreatTime() {
        return crreatTime;
    }

    public void setCrreatTime(Long crreatTime) {
        this.crreatTime = crreatTime;
    }

    public List<Question> getQuestions() {
        if(questions==null){
            questions = new ArrayList<>();
        }
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getId() {
        return id;
    }
}
