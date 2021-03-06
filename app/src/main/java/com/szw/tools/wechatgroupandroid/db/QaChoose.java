package com.szw.tools.wechatgroupandroid.db;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 题库选择db
 */
@Table("qaChoose")
public class QaChoose {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.BY_MYSELF)
    private String id;
    // 非空字段
    @NotNull
    private int userAsk;

    // 非空字段
    @NotNull
    private int radomAsk;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserAsk() {
        return userAsk;
    }

    public void setUserAsk(int userAsk) {
        this.userAsk = userAsk;
    }

    public int getRadomAsk() {
        return radomAsk;
    }

    public void setRadomAsk(int radomAsk) {
        this.radomAsk = radomAsk;
    }
}
