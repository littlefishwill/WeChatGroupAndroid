package com.szw.tools.wechatgroupandroid.pages.qa;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaResult;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaResultItem;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.utils.TimeFormatUtils;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by SuZhiwei on 2017/9/24.
 */
public class QuestionsResultShowAvtivity extends BaseActivity {
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton qaQ,chQ;
    private QaResult qaResult;
    private RecyclerView questionsRv;
    private CommonAdapter questionAdapter;
    private  ActionBar actionBar;
    private SimpleDateFormat timeformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_requestshow);
        qaResult = (QaResult) getIntent().getSerializableExtra("data");
        questionsRv = (RecyclerView) findViewById(R.id.rv_qa_questions);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//设置返回箭头显示
        actionBar.setTitle(qaResult.getQaLibraryName());//父标题
        actionBar.setSubtitle(timeformat.format(qaResult.getStartTime()));

        questionList();


    }



    private void questionList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        questionsRv.setLayoutManager(layoutManager);
        questionAdapter = new CommonAdapter<QaResultItem>(this,R.layout.item_qa_result_show,qaResult.getQaResultItems()) {
            @Override
            public void convert(CommonViewHolder holder, QaResultItem qaResultItem, final int posation) {
                TextView title = holder.getView(R.id.tv_question_title);
                final TextView pos = holder.getView(R.id.tv_question_number);
                TextView time = holder.getView(R.id.question_time);
                TextView source = holder.getView(R.id.question_source);
                TextView answer = holder.getView(R.id.tv_question_answer);
                TextView rightAnswer = holder.getView(R.id.tv_question_right);
                TextView errorAnswer = holder.getView(R.id.tv_question_error);
                Question question = qaResultItem.getQuestion();

                time.setText(question.getTime()/1000+"秒");
                pos.setText(posation+1+"");
                title.setText(question.getDes());
                source.setText(question.getSource()+"积分");
                answer.setText("正确答案:"+getString(question.getType2Answer()));

                rightAnswer.setText(getAnswer(qaResultItem.getRightWechats()));
                errorAnswer.setText(getAnswer(qaResultItem.getErrorWechats()));


            }
        };
        questionsRv.setAdapter(questionAdapter);
    }

    public String getAnswer(List<Chat> list){
        StringBuilder stringBuilder = new StringBuilder();
        for(Chat chat:list){
            stringBuilder.append(chat.getName()+":"+chat.getMessage());
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString().trim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getString(String[] arrs){
        if(arrs==null){
            return "";
        }
        String str = "";
        for(String s:arrs){
            str = str+ s +",";
        }

        if(str.endsWith(",") || str.endsWith("，")){
            str = str.substring(0,str.length()-1);
        }
        return str;
    }
}
