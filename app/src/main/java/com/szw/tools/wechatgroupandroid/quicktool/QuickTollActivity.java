package com.szw.tools.wechatgroupandroid.quicktool;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.szw.tools.wechatgroupandroid.BaseActivity;
import com.szw.tools.wechatgroupandroid.R;

/**
 * Created by SuZhiwei on 2017/9/22.
 */
public class QuickTollActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quicktool);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }
}
