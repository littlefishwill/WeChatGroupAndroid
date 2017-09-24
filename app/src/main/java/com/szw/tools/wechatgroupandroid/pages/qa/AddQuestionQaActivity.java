package com.szw.tools.wechatgroupandroid.pages.qa;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.MainActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.utils.Sputils;

public class AddQuestionQaActivity extends BaseActivity {

    private ViewGroup container;
    private AutoCompleteTextView title,answer,source,time;
    private  Questions questions;
    private int edit;
    private TextView titleShow;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion_qa);
        titleShow = (TextView) findViewById(R.id.tv_qa_question_title);
        questions = (Questions) getIntent().getSerializableExtra("data");
        edit = getIntent().getIntExtra("edit", -1);
        container = (ViewGroup) findViewById(R.id.container);
        submit = (Button) findViewById(R.id.close);

        //方式一
        setupSharedEelementTransitions1(container);
        //方式二
//        setupSharedEelementTransitions2();

        View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };

        AddQuestionLogic();

    }

    private void AddQuestionLogic() {
        title = (AutoCompleteTextView) findViewById(R.id.atv_add_question_title);
        answer = (AutoCompleteTextView) findViewById(R.id.atv_add_question_answer);
        source = (AutoCompleteTextView) findViewById(R.id.atv_add_question_source);
        time = (AutoCompleteTextView) findViewById(R.id.atv_add_question_time);
        if(edit!=-1){
            titleShow.setText("编辑题目");
            submit.setText("确认修改");
            Question question = questions.getQuestions().get(edit);
            title.setText(question.getDes());
            if(question.getType()==Question.TYPE_QA) {
                answer.setText(QuestionsShowAvtivity.getString(question.getType2Answer()));
            }else if(question.getType()==Question.TYPE_CHOOSE){
                answer.setText(QuestionsShowAvtivity.getString(question.getType1Answer()));
            }
            source.setText(question.getSource()+"");
            time.setText(question.getTime()/1000+"");
        }else{
            Question question = Sputils.getInstance(AddQuestionQaActivity.this).getObject(QaManager.getInstance(), Question.class);
            if(question!=null){
                source.setText(question.getSource()+"");
                time.setText(question.getTime()/1000+"");
            }
        }



        submit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString().trim();
                String answerStr = answer.getText().toString().trim();
                String sourceStr = source.getText().toString().trim();
                String timeStr = time.getText().toString().trim();
                if (titleStr.length() < 1) {
                    title.setError("题目不可为空");
                    title.requestFocus();
                    return;
                } else if (answerStr.length() < 1) {
                    answer.setError("答案不可为空");
                    answer.requestFocus();
                    return;
                } else if (sourceStr.length() < 1) {
                    source.setError("分数不可为空");
                    source.requestFocus();
                    return;
                } else if (timeStr.length() < 1) {
                    time.setError("时间不可为空");
                    time.requestFocus();
                    return;
                }
                Question question;
                if(edit==-1){
                    question = new Question();
                }else{
                    question = questions.getQuestions().get(edit);
                }
                question.setDes(titleStr);
                question.setType(Question.TYPE_QA);
                String[] split = answerStr.split("[,，]");
                question.setType2Answer(split);
                question.setSource(Integer.parseInt(sourceStr));
                question.setTime(Integer.parseInt(timeStr) * 1000);
                if(edit==-1) {
                    questions.getQuestions().add(question);
                }
                QaManager.getInstance().saveQuestions(questions);
                Sputils.getInstance(AddQuestionQaActivity.this).putObject(QaManager.getInstance(), question);
                Intent data = new Intent();
                data.putExtra("data",questions);
                setResult(2, data);
                finishAfterTransition();
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void dismiss() {
        setResult(Activity.RESULT_CANCELED);
        finishAfterTransition();
    }

}
