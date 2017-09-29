package com.szw.tools.wechatgroupandroid.db;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by shenmegui on 2017/9/29.
 */
@Table("chatdao")
public class ChatScoreDao {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.BY_MYSELF)
    private String chatName;

    // 非空字段
    @NotNull
    private String groupName;

    // 非空字段
    @NotNull
    private int score;

    // 非空字段 问答结果库id
    @NotNull
    private String qaResultId;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getQaResultId() {
        return qaResultId;
    }

    public void setQaResultId(String qaResultId) {
        this.qaResultId = qaResultId;
    }
}
