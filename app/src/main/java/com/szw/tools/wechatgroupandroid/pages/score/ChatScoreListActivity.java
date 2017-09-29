package com.szw.tools.wechatgroupandroid.pages.score;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
public class ChatScoreListActivity extends BaseActivity {
    private RecyclerView groupScore;
    private String groupNme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupNme =  getIntent().getStringExtra("data");
        setContentView(R.layout.activity_socregroup);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(groupNme);
            actionBar.setSubtitle("排名积分详细");
        }

        groupScore = (RecyclerView) findViewById(R.id.rv_socre_groups);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        groupScore.setLayoutManager(layoutManager);

        groupScore.setAdapter(new CommonAdapter<ChatScoreDao>(ChatScoreListActivity.this, R.layout.item_scorechat, DbManager.getInstance().getGroupInSCore(groupNme)) {

            @Override
            public void convert(CommonViewHolder holder, ChatScoreDao chatScoreDao, int posation) {
                TextView title = holder.getView(R.id.tv_qa_item_title);
                TextView score = holder.getView(R.id.tv_qa_item_time);
                TextView scoreNo = holder.getView(R.id.tv_score_no);
                ImageView scoreIco = holder.getView(R.id.tv_score_noico);
                title.setText(chatScoreDao.getChatName());
                score.setText(chatScoreDao.getScore() + "分");

                if(posation==0){
                    scoreIco.setImageResource(R.mipmap.score_1);
                }else if(posation==1){
                    scoreIco.setImageResource(R.mipmap.score_2);
                }else if(posation==2){
                    scoreIco.setImageResource(R.mipmap.score_3);
                }else{
                    scoreIco.setImageBitmap(null);
                }

                posation = posation+1;
                scoreNo.setText("NO."+posation+"");

            }
        });
    }
}
