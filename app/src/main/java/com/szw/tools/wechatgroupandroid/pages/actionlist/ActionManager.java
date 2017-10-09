package com.szw.tools.wechatgroupandroid.pages.actionlist;

import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.actionlist.domain.Action;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuZhiwei on 2017/9/21.
 */
public class ActionManager {

    private  static ActionManager actionManager;

    public  static  ActionManager getInstance(){
        if(actionManager==null){
            actionManager = new ActionManager();
        }
        return actionManager;
    }

    private ActionManager() {
    }

    /**
     * 问答 id
     */
    public static final int Action_Id_QA =0x01;
    /**
     * 积分 id
     */
    public static final int Action_Id_JF =0x02;

    /**
     * 抽签 id
     */
    public static final int Action_Id_CQ =0x03;

    public List<Action> getActions(){
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new Action("问答", Action_Id_QA,R.mipmap.action_qa,true));
        actions.add(new Action("抽签", Action_Id_CQ,R.mipmap.chouqian,true));
        actions.add(new Action("积分", Action_Id_JF,R.mipmap.socre_ico,true));
//        actions.add(new Action("娱乐", Action_Id_HP,R.mipmap.action_happy,true));
//        actions.add(new Action("天气", Action_Id_QA,R.mipmap.action_wheather,true));
//        actions.add(new Action("答卷", Action_Id_QA,R.mipmap.action_qa,true));
        return actions;
    }

}
