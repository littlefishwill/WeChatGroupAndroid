package com.szw.tools.wechatgroupandroid.pages.actionlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.szw.tools.wechatgroupandroid.BaseFragment;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.actionlist.domain.Action;
import com.szw.tools.wechatgroupandroid.pages.cq.CqManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaActivity;
import com.szw.tools.wechatgroupandroid.pages.score.GroupScoreListActivity;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;

/**
 * Created by SuZhiwei on 2017/9/21.
 */
public class ActionListFragment  extends BaseFragment{
    private RecyclerView actionList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_actionlist;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionList = (RecyclerView) findViewById(R.id.rv_actionlist);
        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 3);
        actionList.setLayoutManager(mgr);
        actionList.setAdapter(new CommonAdapter<Action>(getActivity(),R.layout.item_actionlist,ActionManager.getInstance().getActions()) {
            @Override
            public void convert(CommonViewHolder holder, Action action,int pos) {
                ImageView ico = holder.getView(R.id.iv_actionitem_ico);
                TextView name = holder.getView(R.id.tv_actionitem_name);
                final SwitchButton switchButton = holder.getView(R.id.switch_button);
                ico.setImageResource(action.getIcoResourceId());
                name.setText(action.getName());
                holder.getRootView().setTag(action);
                switchButton.setTag(action);

                switch (action.getActionId()){
                    case ActionManager.Action_Id_QA:
                        switchButton.setVisibility(View.GONE);
                        break;
                    case ActionManager.Action_Id_JF:
//                        startActivity(new Intent(getActivity(), GroupScoreListActivity.class));
                        break;
                    case ActionManager.Action_Id_CQ:
                          switchButton.setChecked(CqManager.getInstance().isOpen());
                        break;
                }

                switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        Action action = (Action) view.getTag();
                        switch (action.getActionId()){
                            case ActionManager.Action_Id_CQ:
//                                startActivity(new Intent(getActivity(), GroupScoreListActivity.class));
                                CqManager.getInstance().open(isChecked);
                                break;
                        }
                    }
                });


                holder.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Action action = (Action) v.getTag();
                        switch (action.getActionId()){
                            case ActionManager.Action_Id_QA:
                                startActivity(new Intent(getActivity(), QaActivity.class));
                                break;
                            case ActionManager.Action_Id_JF:
                                startActivity(new Intent(getActivity(), GroupScoreListActivity.class));
                                break;
                            case ActionManager.Action_Id_CQ:
//                                startActivity(new Intent(getActivity(), GroupScoreListActivity.class));
                                switchButton.setChecked(!CqManager.getInstance().isOpen());
                                CqManager.getInstance().open(!CqManager.getInstance().isOpen());
                                break;
                        }
                    }
                });
            }
        });
    }

}
