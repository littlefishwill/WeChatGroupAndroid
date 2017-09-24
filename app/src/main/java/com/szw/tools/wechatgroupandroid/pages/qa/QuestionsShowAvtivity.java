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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;

/**
 * Created by SuZhiwei on 2017/9/24.
 */
public class QuestionsShowAvtivity extends BaseActivity {
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton qaQ,chQ;
    private Questions questions;
    private RecyclerView questionsRv;
    private CommonAdapter questionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionsshow);
        questionsRv = (RecyclerView) findViewById(R.id.rv_qa_questions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//设置返回箭头显示
        questions = (Questions) getIntent().getSerializableExtra("data");
        actionBar.setTitle(questions.getTitle());//父标题

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.menu_fab);
        qaQ = (FloatingActionButton) findViewById(R.id.menu_qa);
        chQ = (FloatingActionButton) findViewById(R.id.menu_choose);
        addQustionLogic();
        questionList();

    }

    private void questionList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        questionsRv.setLayoutManager(layoutManager);
        questionAdapter = new CommonAdapter<Question>(this,R.layout.item_question,questions.getQuestions()) {
            @Override
            public void convert(CommonViewHolder holder, Question question, final int posation) {
                TextView title = holder.getView(R.id.tv_question_title);
                final TextView pos = holder.getView(R.id.tv_question_number);
                TextView time = holder.getView(R.id.question_time);
                TextView source = holder.getView(R.id.question_source);
                TextView answer = holder.getView(R.id.tv_question_answer);
                ImageView del= holder.getView(R.id.iv_question_del);
                ImageView edit= holder.getView(R.id.iv_question_edit);

                time.setText(question.getTime()/1000+"秒");
                pos.setText(posation+1+"");
                title.setText(question.getDes());
                source.setText(question.getSource()+"积分");
                answer.setText("答:"+getString(question.getType2Answer()));

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(QuestionsShowAvtivity.this);
                        normalDialog.setTitle("删除文件");
                        Question queston = questions.getQuestions().get(posation);
                        normalDialog.setMessage("您确定要'"+queston.getDes()+"'这道题吗?" );
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        questions.getQuestions().remove(posation);
                                        QaManager.getInstance().saveQuestions(questions);
                                        questionAdapter.notifyDataSetChanged();
                                        //...To-do
//                                        QaManager.getInstance().delectQuestions((Questions) questionsAdapter.getmDatas().get(postation));
//                                        questionsAdapter.getmDatas().remove(postation);
//                                        questionsAdapter.notifyDataSetChanged();
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
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        floatingActionMenu.close(true);
                        Intent intent = new Intent(QuestionsShowAvtivity.this, AddQuestionQaActivity.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(QuestionsShowAvtivity.this, v, getString(R.string.transition_dialog));
                        intent.putExtra("data",questions);
                        intent.putExtra("edit",posation);
                        startActivityForResult(intent, 1, options.toBundle());
                    }
                });
            }
        };
        questionsRv.setAdapter(questionAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==2){
            Questions questions = (Questions) data.getSerializableExtra("data");
            QuestionsShowAvtivity.this.questions = questions;
            questionAdapter.changeData(questions.getQuestions());

        }
    }

    public static String getString(String[] arrs){
        if(arrs==null){
            return "";
        }
        String str = "";
        for(String s:arrs){
            str = str+ s +",";
        }
//
//        for(String s:arrs){
//            str = str+"(" +s+")";
//        }

        if(str.endsWith(",") || str.endsWith("，")){
            str = str.substring(0,str.length()-1);
        }
        return str;
    }
    private void addQustionLogic() {
        View.OnClickListener listener = new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.menu_qa:
                        Intent intent = new Intent(QuestionsShowAvtivity.this, AddQuestionQaActivity.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(QuestionsShowAvtivity.this, qaQ, getString(R.string.transition_dialog));
                        intent.putExtra("data",questions);
                        startActivityForResult(intent, 1, options.toBundle());
                        break;
                    case R.id.menu_choose:
                        break;
                }
            }
        };
        qaQ.setOnClickListener(listener);
        chQ.setOnClickListener(listener);
    }
}
