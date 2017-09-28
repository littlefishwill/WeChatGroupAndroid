package com.szw.tools.wechatgroupandroid.pages.qa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaResult;
import com.szw.tools.wechatgroupandroid.utils.TimeFormatUtils;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;
import java.text.SimpleDateFormat;

/**
 * Created by shenmegui on 2017/9/28.
 */
public class QaResultListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SimpleDateFormat timeformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_resultlist);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recyclerView  = (RecyclerView) findViewById(R.id.rv_qa_results);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommonAdapter<QaResult>(QaResultListActivity.this,R.layout.item_playresult,QaPlayResultManager.getInstance().getQaResults()) {
            @Override
            public void convert(CommonViewHolder holder, final QaResult qaResult, int posation) {
                TextView name =  holder.getView(R.id.tv_qa_item_title);
                TextView time =  holder.getView(R.id.tv_qa_item_time);
                TextView time_dur =  holder.getView(R.id.tv_qa_item_author);
                name.setText(qaResult.getWeChat().getName()+"("+qaResult.getQaLibraryName()+")");
                time.setText(timeformat.format(qaResult.getEndTime()));
                time_dur.setText("用时:"+TimeFormatUtils.formatSeconds(qaResult.getEndTime()/1000-qaResult.getStartTime()/1000));

                holder.getView(R.id.item_question_contain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QaResultListActivity.this, QuestionsResultShowAvtivity.class);
                        intent.putExtra("data",qaResult);
                        startActivity(intent);
                    }
                });

            }
        });


    }
}
