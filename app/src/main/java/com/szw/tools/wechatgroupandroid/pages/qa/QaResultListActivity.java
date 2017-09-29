package com.szw.tools.wechatgroupandroid.pages.qa;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
            public void convert(CommonViewHolder holder, final QaResult qaResult, final int posation) {
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

                holder.getView(R.id.item_question_contain).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(QaResultListActivity.this);
                        normalDialog.setTitle("删除");
                        normalDialog.setMessage("您确定要删除'"+qaResult.getQaLibraryName()+"'在 "+timeformat.format(qaResult.getStartTime())+"的这场问答结果吗？" );
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //...To-do
                                        QaPlayResultManager.getInstance().getQaResults().remove(posation);
                                        QaPlayResultManager.getInstance().delectQaResult(qaResult);
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                    }
                                });

                        normalDialog.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //...To-do
                                    }
                                });
                        // 显示
                        normalDialog.show();

                        return false;
                    }
                });

            }
        });


    }
}
