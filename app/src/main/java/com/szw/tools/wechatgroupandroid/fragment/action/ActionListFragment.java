package com.szw.tools.wechatgroupandroid.fragment.action;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.szw.tools.wechatgroupandroid.BaseFragment;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.fragment.domain.Action;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;

import org.w3c.dom.Text;

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
        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 4);
        actionList.setLayoutManager(mgr);
        actionList.setAdapter(new CommonAdapter<Action>(getActivity(),R.layout.item_actionlist,ActionManager.getInstance().getActions()) {
            @Override
            public void convert(CommonViewHolder holder, Action action) {
                ImageView ico = holder.getView(R.id.iv_actionitem_ico);
                TextView name = holder.getView(R.id.tv_actionitem_name);
                ico.setImageResource(action.getIcoResourceId());
                name.setText(action.getName());
            }
        });

    }


}
