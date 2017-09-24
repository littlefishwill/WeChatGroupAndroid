package com.szw.tools.wechatgroupandroid.view.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.MainActivity;
import com.szw.tools.wechatgroupandroid.R;

public class DialogActivity extends BaseActivity {

    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        container = (ViewGroup) findViewById(R.id.container);

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
        container.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setResult(MainActivity.Request_Dialog_Res_Success);
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
