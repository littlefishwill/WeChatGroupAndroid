package com.szw.tools.wechatgroupandroid.pages.qa;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AutoCompleteTextView;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.MainActivity;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.user.User;
import com.szw.tools.wechatgroupandroid.user.UserManager;
import com.szw.tools.wechatgroupandroid.view.dialog.MorphDialogToFab;
import com.szw.tools.wechatgroupandroid.view.dialog.MorphFabToDialog;
import com.szw.tools.wechatgroupandroid.view.dialog.MorphTransition;

public class AddQuestionLibraryActivity extends BaseActivity {

    private ViewGroup container;
    private AutoCompleteTextView title;
    private AutoCompleteTextView author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestionlibrary);

        container = (ViewGroup) findViewById(R.id.container);
        title = (AutoCompleteTextView) findViewById(R.id.atv_add_question_title);
        author = (AutoCompleteTextView) findViewById(R.id.atv_add_question_author);

        //方式一
        setupSharedEelementTransitions1(container);

        autoFillAuthor();

        View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
        container.setOnClickListener(dismissListener);
        container.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                submitLogic();
            }
        });
    }

    private void autoFillAuthor() {
        User user = UserManager.getInstance().getUser();
        if(user.getUserName()!=null && user.getUserName().length()>0){
            author.setText(user.getUserName());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void submitLogic() {
        if(title.getText().toString().trim().length()<1){
            title.setError("题库名称不可为空");
            title.requestFocus();
            return;
        }

        if(author.getText().toString().trim().length()<1){
            author.setError("作者不可为空");
            author.requestFocus();
            return;
        }

        Questions questions = new Questions();
        questions.setAuthor(author.getText().toString().trim());
        questions.setTitle(title.getText().toString().trim());
        questions.setCrreatTime(System.currentTimeMillis());

        QaManager.getInstance().saveQuestions(questions);

        // --- cache author
        User user = UserManager.getInstance().getUser();
        user.setUserName(author.getText().toString().trim());
        UserManager.getInstance().updateUser(user);

        Intent intent = new Intent();
        intent.putExtra("data",questions);
        setResult(1,intent);
        finishAfterTransition();

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
