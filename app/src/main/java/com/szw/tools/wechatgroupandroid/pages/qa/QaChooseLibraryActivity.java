package com.szw.tools.wechatgroupandroid.pages.qa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by SuZhiwei on 2017/9/23.
 */
public class QaChooseLibraryActivity extends BaseActivity {

    private RecyclerView questionsList;
    private CommonAdapter questionsAdapter;
    private SimpleDateFormat timeformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_lib);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        questionsList = (RecyclerView) findViewById(R.id.rv_qa_libs_choose);

        showQuestionLibrary();

//        emptyQA.animateText("题库为空！点击右下角小红点可以添加题库.");

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(questionsAdapter!=null){
            questionsAdapter.notifyDataSetChanged();
        }
    }

    private void showQuestionLibrary() {
        List<Questions> cacheQuestiones = QaManager.getInstance().getCacheQuestiones();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        questionsList.setLayoutManager(layoutManager);
        questionsAdapter = new CommonAdapter<Questions>(QaChooseLibraryActivity.this, R.layout.item_questions_check, cacheQuestiones) {
            @Override
            public void convert(CommonViewHolder holder, Questions questions, final int posation) {
                TextView title = holder.getView(R.id.tv_qa_item_title);
                TextView author = holder.getView(R.id.tv_qa_item_author);
                TextView time = holder.getView(R.id.tv_qa_item_time);
                title.setText(questions.getTitle());
                author.setText(questions.getAuthor());
                time.setText(timeformat.format(questions.getCrreatTime()));
            }
        };
        questionsList.setAdapter(questionsAdapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
