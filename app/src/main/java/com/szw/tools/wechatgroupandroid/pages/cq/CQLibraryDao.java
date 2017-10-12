package com.szw.tools.wechatgroupandroid.pages.cq;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shenmegui on 2017/10/10.
 */
public class CQLibraryDao {
    private String name;

    public CQLibraryDao(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cq> getCqLibrary() {
        if(cqLibrary==null){
            cqLibrary = new LinkedList<>();
        }
        return cqLibrary;
    }

    public void setCqLibrary(List<Cq> cqLibrary) {
        this.cqLibrary = cqLibrary;
    }

    private List<Cq>  cqLibrary = new LinkedList<>();

}
