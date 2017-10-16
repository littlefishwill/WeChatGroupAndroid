package com.szw.tools.wechatgroupandroid.view.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.MainActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.QaIngManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaRadomAskManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaUserAskManager;

public class AskRodomOpenActivity extends BaseActivity {

    private ViewGroup container;
    private TextView title,des;
    private Button submit;
    private int type; // 0 = ask,1=radom

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type",0);


        setContentView(R.layout.activity_qa_askrodomopen);

        container = (ViewGroup) findViewById(R.id.container);
        title = (TextView) findViewById(R.id.tv_qa_askrodom_title);
        des = (TextView) findViewById(R.id.tv_qa_askrodom_des);
        submit = (Button) findViewById(R.id.close);

        logic();



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
    }

    private void logic() {
        if(type==0){
            title.setText("自助请求提问");
            if(QaUserAskManager.getInstance().isOpen()){
                des.setText("当前模式开启中，是否关闭该模式？");
                submit.setText("停止问答");
            }else{
                des.setText("开启该模式:当微信聊天中有人发送指令'来题'、'出题'、'给题'、'求题'，时系统会随机抽选选中题库的某一道题目进行提问，问答。" );
                submit.setText("开启");
            }
        }else if(type==1){
            title.setText("随机无限抽题提问");
            if(QaRadomAskManager.getInstance().isOpen()){
                des.setText("当前模式开启中，是否关闭该模式？");
                submit.setText("停止问答");
            }else{
                des.setText("开启该模式:当你进入微信任意聊天界面，系统会开始随机抽取选中题库中的题目进行问答。" );
                submit.setText("开启");
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(type==0){
                    if(QaUserAskManager.getInstance().isOpen()){
                        QaUserAskManager.getInstance().open(false);
                    }else{
                        QaUserAskManager.getInstance().open(true);
                        QaRadomAskManager.getInstance().open(false);
                        QaIngManager.getInstance().setQaNowQuestions(null);
                    }
                }else if(type==1){
                    if(QaRadomAskManager.getInstance().isOpen()){
                        QaRadomAskManager.getInstance().open(false);
                    }else{
                        QaRadomAskManager.getInstance().open(true);
                        QaUserAskManager.getInstance().open(false);
                        QaIngManager.getInstance().setQaNowQuestions(null);
                    }
                }

                setResult(2);
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
