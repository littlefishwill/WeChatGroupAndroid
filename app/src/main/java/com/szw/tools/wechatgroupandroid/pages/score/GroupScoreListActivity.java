package com.szw.tools.wechatgroupandroid.pages.score;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.db.ChatScoreDao;
import com.szw.tools.wechatgroupandroid.db.DbManager;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;

/**
 * Created by shenmegui on 2017/9/29.
 */
public class GroupScoreListActivity extends BaseActivity {
    private RecyclerView groupScore;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_socregroup);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        groupScore = (RecyclerView) findViewById(R.id.rv_socre_groups);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        groupScore.setLayoutManager(layoutManager);

        groupScore.setAdapter(new CommonAdapter<ChatScoreDao>(GroupScoreListActivity.this,R.layout.item_question, DbManager.getInstance().getGroupSocre()) {

            @Override
            public void convert(CommonViewHolder holder, ChatScoreDao chatScoreDao, int posation) {

            }
        });

//        groupScore.setAdapter();

    }
}
