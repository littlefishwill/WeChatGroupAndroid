package com.szw.tools.wechatgroupandroid.pages.score;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socregroup);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("群组积分,或个人积分(总分排名)");
        }

        groupScore = (RecyclerView) findViewById(R.id.rv_socre_groups);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        groupScore.setLayoutManager(layoutManager);

        groupScore.setAdapter(new CommonAdapter<ChatScoreDao>(GroupScoreListActivity.this, R.layout.item_scoregroup, DbManager.getInstance().getGroupSocre()) {

            @Override
            public void convert(CommonViewHolder holder, final ChatScoreDao chatScoreDao, int posation) {
                TextView title = holder.getView(R.id.tv_qa_item_title);
                title.setText(chatScoreDao.getGroupName());

                TextView score = holder.getView(R.id.tv_qa_item_time);
                score.setText("历史总分:"+chatScoreDao.getScore()+"分");

                holder.getView(R.id.cv_questions).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupScoreListActivity.this, ChatScoreListActivity.class);
                        intent.putExtra("data",chatScoreDao.getGroupName());
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
