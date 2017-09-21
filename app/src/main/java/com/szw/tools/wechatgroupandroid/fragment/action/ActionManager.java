package com.szw.tools.wechatgroupandroid.fragment.action;

import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.fragment.domain.Action;
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
    public static int Action_Id_QA =0x01;
    /**
     * 签到 id
     */
    public static int Action_Id_QD =0x02;

    public List<Action> getActions(){
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new Action("考试", Action_Id_QA,R.mipmap.action_qa,true));
        actions.add(new Action("签到", Action_Id_QD,R.mipmap.action_qd,true));
        actions.add(new Action("抽签", Action_Id_QA,R.mipmap.action_qa,true));
        actions.add(new Action("答卷", Action_Id_QA,R.mipmap.action_qa,true));
        actions.add(new Action("答卷", Action_Id_QA,R.mipmap.action_qa,true));
        actions.add(new Action("答卷", Action_Id_QA,R.mipmap.action_qa,true));
        return actions;
    }

}
