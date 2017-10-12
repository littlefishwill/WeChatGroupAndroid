package com.szw.tools.wechatgroupandroid.db;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by shenmegui on 2017/9/29.
 */
@Table("chatdao")
public class CQDao {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.BY_MYSELF)
    private String id;
    // 非空字段
    @NotNull
    private String chatName;

    // 非空字段
    @NotNull
    private String groupName;

    // 非空字段 cqlib 库角标
    @NotNull
    private int librarypos;

    // 非空字段 cqitemspos 库角标
    @NotNull
    private int cqitemspos;

    @NotNull
    private long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getLibrarypos() {
        return librarypos;
    }

    public void setLibrarypos(int librarypos) {
        this.librarypos = librarypos;
    }

    public int getCqitemspos() {
        return cqitemspos;
    }

    public void setCqitemspos(int cqitemspos) {
        this.cqitemspos = cqitemspos;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
