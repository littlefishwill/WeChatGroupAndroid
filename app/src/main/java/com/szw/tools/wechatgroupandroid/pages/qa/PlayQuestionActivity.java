package com.szw.tools.wechatgroupandroid.pages.qa;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.MainActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;

public class PlayQuestionActivity extends BaseActivity {

    private ViewGroup container;
    private Questions questions;
    private Button submit;
    private TextView playQuestionsDes;
    private boolean isNeddAddQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_question);
        submit = (Button)findViewById(R.id.close);
        questions = (Questions) getIntent().getSerializableExtra("data");
        container = (ViewGroup) findViewById(R.id.container);
        playQuestionsDes = (TextView) findViewById(R.id.tv_play_custom_qa_des);

        switchLogic();

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

        container.setOnClickListener(dismissListener);
        submit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!isNeddAddQuestion){
                    if(QaIngManager.getInstance().getQaNowQuestions()!=null && QaIngManager.getInstance().getQaNowQuestions().getId().equals(questions.getId())){
                        QaIngManager.getInstance().setQaNowQuestions(null);
                    }else{
                        QaIngManager.getInstance().setQaNowQuestions(questions);
                    }
                }

                QaUserAskManager.getInstance().open(false);
                QaRadomAskManager.getInstance().open(false);

                if(isNeddAddQuestion){
                    Intent intent = new Intent(PlayQuestionActivity.this, QuestionsShowAvtivity.class);
                    intent.putExtra("data",questions);
                    startActivity(intent);
                }

                setResult(2);
                finishAfterTransition();
            }
        });
    }

    private void switchLogic() {

        if(questions.getQuestions()!=null && questions.getQuestions().size()>0){
            isNeddAddQuestion = false;
        }else{
            isNeddAddQuestion = true;
        }

        if(QaIngManager.getInstance().getQaNowQuestions()!=null && QaIngManager.getInstance().getQaNowQuestions().getId().equals(questions.getId())){
            submit.setText("停止问答");
        }else{
            if(isNeddAddQuestion){
                playQuestionsDes.setText("该题库中没有任何题目，不可开启答题！是否前往添加题目？");
                submit.setText("添加题目");
            }else {
                playQuestionsDes.setText("开启后，请手动进入任意微信聊天界面,系统会默认10秒自动内发送题目，开始问答。");
                submit.setText("开始问答");
            }
        }
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
