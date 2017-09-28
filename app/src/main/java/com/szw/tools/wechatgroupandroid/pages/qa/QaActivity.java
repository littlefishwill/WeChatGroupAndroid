package com.szw.tools.wechatgroupandroid.pages.qa;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QuestiionLoadListener;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonAdapter;
import com.szw.tools.wechatgroupandroid.view.adapter.CommonViewHolder;
import com.szw.tools.wechatgroupandroid.view.htext.ScaleTextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by SuZhiwei on 2017/9/23.
 */
public class QaActivity extends BaseActivity {
    private ScaleTextView emptyQA;
    private FloatingActionButton addQuestion;
    private RecyclerView questionsList;
    private CommonAdapter questionsAdapter;
    private SimpleDateFormat timeformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private List<Questions>  questionses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        emptyQA = (ScaleTextView) findViewById(R.id.stv_qa_empty_des);
        addQuestion = (FloatingActionButton) findViewById(R.id.fab);
        questionsList = (RecyclerView) findViewById(R.id.rv_qa_questions);
        addQuestionLogic();
        showQuestionLibrary();
        qaResultLogic();

//        emptyQA.animateText("题库为空！点击右下角小红点可以添加题库.");

    }

    private void qaResultLogic() {
        findViewById(R.id.rv_qa_result_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QaActivity.this,QaResultListActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(questionsAdapter!=null){
            questionsAdapter.notifyDataSetChanged();
        }
    }

    private void showQuestionLibrary() {
    questionses =  QaManager.getInstance().getCacheQuestiones();
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    questionsList.setLayoutManager(layoutManager);
        questionsAdapter = new CommonAdapter<Questions>(QaActivity.this, R.layout.item_questions, questionses) {
            @Override
            public void convert(CommonViewHolder holder, Questions questions, final int posation) {
                TextView title = holder.getView(R.id.tv_qa_item_title);
                TextView author = holder.getView(R.id.tv_qa_item_author);
                TextView time = holder.getView(R.id.tv_qa_item_time);
                title.setText(questions.getTitle());
                author.setText(questions.getAuthor());
                time.setText(timeformat.format(questions.getCrreatTime()));

                // long clcikListener
                holder.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemLongClickListener(posation);
                        return false;
                    }
                });

                holder.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QaActivity.this, QuestionsShowAvtivity.class);
                        intent.putExtra("data",(Questions)questionsAdapter.getmDatas().get(posation));
                        startActivity(intent);
                    }
                });

                // for layout
                CardView cardView = holder.getView(R.id.cv_questions);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                if (posation == questionses.size() - 1) {
                    layoutParams.setMargins(0, 1, 0, 3);
                } else {
                    layoutParams.setMargins(0, 1, 0, 0);
                }

                ImageView play = holder.getView(R.id.iv_qa_play);

                if(QaIngManager.getInstance().getQaNowQuestions()!=null && questions.getId().equals(QaIngManager.getInstance().getQaNowQuestions().getId())){
                    play.setImageResource(R.drawable.qa_stop);
                }else{
                    play.setImageResource(R.mipmap.qa_play);
                }

                play.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QaActivity.this, PlayQuestionActivity.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(QaActivity.this, v, getString(R.string.transition_dialog));
                        intent.putExtra("data",questionses.get(posation));
                        startActivityForResult(intent, 1, options.toBundle());
                    }
                });
            }
        };
        questionsList.setAdapter(questionsAdapter);
    }

    public void onItemLongClickListener(final int postation){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(QaActivity.this);
        normalDialog.setTitle("删除文件");
        Questions questions = (Questions) questionsAdapter.getmDatas().get(postation);
        normalDialog.setMessage("您确定要删除'"+questions.getTitle()+"'题库吗?" );
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        QaManager.getInstance().delectQuestions((Questions) questionsAdapter.getmDatas().get(postation));
                        questionsAdapter.getmDatas().remove(postation);
                        questionsAdapter.notifyDataSetChanged();
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

    private void addQuestionLogic() {
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QaActivity.this, AddQuestionLibraryActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(QaActivity.this, addQuestion, getString(R.string.transition_dialog));
                startActivityForResult(intent, 1, options.toBundle());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){
           questionsAdapter.notifyDataSetChanged();
        }else if(requestCode==1 && resultCode==2){
            questionsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
