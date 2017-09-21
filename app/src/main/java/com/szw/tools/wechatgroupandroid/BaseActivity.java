package com.szw.tools.wechatgroupandroid;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shenmegui on 2017/9/21.
 */
public class BaseActivity extends AppCompatActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;

    public void relaceFragment(int layoutId,BaseFragment baseFragment){
        if(manager==null){
            manager = getSupportFragmentManager();
        }
        transaction = manager.beginTransaction();
        transaction.replace(layoutId, baseFragment);
        transaction.commit();
    }
}
