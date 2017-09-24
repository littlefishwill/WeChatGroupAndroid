package com.szw.tools.wechatgroupandroid;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.szw.tools.wechatgroupandroid.view.dialog.MorphDialogToFab;
import com.szw.tools.wechatgroupandroid.view.dialog.MorphFabToDialog;
import com.szw.tools.wechatgroupandroid.view.dialog.MorphTransition;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 使用方式一：调用setupSharedEelementTransitions1方法
     * 使用这种方式的话需要的类是 MorphDrawable, MorphFabToDialog, MorphDialogToFab
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupSharedEelementTransitions1(View container) {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

        MorphFabToDialog sharedEnter = new MorphFabToDialog();
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphDialogToFab sharedReturn = new MorphDialogToFab();
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null) {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    /**
     * 使用方式二：调用setupSharedEelementTransitions2方法
     * 使用这种方式的话需要的类是 MorphDrawable, MorphTransition
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupSharedEelementTransitions2(View container) {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

        //hujiawei 100是随意给的一个数字，可以修改，需要注意的是这里调用container.getHeight()结果为0
        MorphTransition sharedEnter = new MorphTransition(ContextCompat.getColor(this, R.color.fab_background_color),
                ContextCompat.getColor(this, R.color.dialog_background_color), 100, getResources().getDimensionPixelSize(R.dimen.dialog_corners), true);
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphTransition sharedReturn = new MorphTransition(ContextCompat.getColor(this, R.color.dialog_background_color),
                ContextCompat.getColor(this, R.color.fab_background_color), getResources().getDimensionPixelSize(R.dimen.dialog_corners), 100,  false);
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null) {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

}
