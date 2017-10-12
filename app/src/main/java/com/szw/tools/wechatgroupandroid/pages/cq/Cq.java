package com.szw.tools.wechatgroupandroid.pages.cq;

/**
 * Created by shenmegui on 2017/10/10.
 */
public class Cq {
    private String cq;
    private String as;
    private int cqitemPos;
    private int cqLibraryPos;
    private String cqLibName;

    public Cq(String cq, String as) {
        this.cq = cq;
        this.as = as;
    }

    public String getCq() {
        return cq;
    }

    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }


    public int getCqitemPos() {
        return cqitemPos;
    }

    public void setCqitemPos(int cqitemPos) {
        this.cqitemPos = cqitemPos;
    }

    public int getCqLibraryPos() {
        return cqLibraryPos;
    }

    public void setCqLibraryPos(int cqLibraryPos) {
        this.cqLibraryPos = cqLibraryPos;
    }

    public String getCqLibName() {
        return cqLibName;
    }

    public void setCqLibName(String cqLibName) {
        this.cqLibName = cqLibName;
    }
}
