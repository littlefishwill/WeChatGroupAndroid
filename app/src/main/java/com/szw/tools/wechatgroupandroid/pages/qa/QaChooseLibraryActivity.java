package com.szw.tools.wechatgroupandroid.pages.qa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.db.DbManager;
import com.szw.tools.wechatgroupandroid.db.QaChoose;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by SuZhiwei on 2017/9/23.
 */
public class QaChooseLibraryActivity extends BaseActivity {

    private RecyclerView questionsList;
    private CommonAdapter questionsAdapter;
    private SimpleDateFormat timeformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private int type;//0= ask,1=rodom
    private Map<String, QaChoose> cacheChosseQustion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_lib);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        type =  getIntent().getIntExtra("type",0);
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
        if(type==0){
            cacheChosseQustion = DbManager.getInstance().getUserAskChooseLib();
        }else if(type==1){
            cacheChosseQustion = DbManager.getInstance().getRadomAskChooseLib();
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        questionsList.setLayoutManager(layoutManager);
        questionsAdapter = new CommonAdapter<Questions>(QaChooseLibraryActivity.this, R.layout.item_questions_check, cacheQuestiones) {
            @Override
            public void convert(CommonViewHolder holder, final Questions questions, final int posation) {
                TextView title = holder.getView(R.id.tv_qa_item_title);
                TextView author = holder.getView(R.id.tv_qa_item_author);
                TextView time = holder.getView(R.id.tv_qa_item_time);
                CheckBox choose = holder.getView(R.id.iv_qa_play);
                title.setText(questions.getTitle());
                author.setText(questions.getAuthor());
                time.setText(timeformat.format(questions.getCrreatTime()));
                choose.setChecked(cacheChosseQustion.get(questions.getId())==null?false:true);
                choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        QaChoose qaChoose = cacheChosseQustion.get(questions.getId());
                        if(qaChoose==null){
                            qaChoose = new QaChoose();
                            qaChoose.setId(questions.getId());
                        }
                        if(type==0){
                            qaChoose.setUserAsk(isChecked?1:0);
                        }else if(type==1){
                            qaChoose.setRadomAsk(isChecked?1:0);
                        }

                        DbManager.getInstance().getLiteOrm().save(qaChoose);
                    }
                });
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
