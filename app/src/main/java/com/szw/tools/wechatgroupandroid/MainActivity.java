package com.szw.tools.wechatgroupandroid;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.szw.tools.wechatgroupandroid.pages.actionlist.ActionListFragment;
import com.szw.tools.wechatgroupandroid.pages.qa.QaManager;
import com.szw.tools.wechatgroupandroid.pages.qa.QaPlayResultManager;
import com.szw.tools.wechatgroupandroid.service.PhoneActivityService;
import com.szw.tools.wechatgroupandroid.service.StartService;
import com.szw.tools.wechatgroupandroid.user.User;
import com.szw.tools.wechatgroupandroid.user.UserManager;
import com.szw.tools.wechatgroupandroid.utils.DpOrPx;
import com.szw.tools.wechatgroupandroid.view.RippleView;
import com.szw.tools.wechatgroupandroid.view.dialog.DialogActivity;
import com.szw.tools.wechatgroupandroid.view.htext.ScaleTextView;

public class MainActivity extends BaseActivity {

    public static int Request_Dialog_Open= 1;
    public static int Request_Dialog_Res_Cancle =3;
    public static int Request_Dialog_Res_Success =4;
    private RippleView helpSwitch;
    private ScaleTextView switchDes,helloText;
    private BaseFragment actionListFragment;
    private FloatingActionButton serviceSwitch;
    private static BaseActivity cacheActivity;

    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheActivity = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helpSwitch = (RippleView) findViewById(R.id.ripp_view_main_switchopen);
        switchDes = (ScaleTextView) findViewById(R.id.sctv_main_switch_des);
        helloText = (ScaleTextView) findViewById(R.id.sctv_main_hello);
        serviceSwitch = (FloatingActionButton) findViewById(R.id.fab);
        appBarLayout = (AppBarLayout) findViewById(R.id.abl_switch_contain);
        switLayoutLogic();
//


    }
    private int fullHeightCardBar;
    private void switLayoutLogic() {
        appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                fullHeightCardBar = appBarLayout.getHeight();
                checkServiceStatue();
                actionSwitch();
                actionListLogic();
                loadAllData();
                appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 获取所有数据
     */
    private void loadAllData() {
        QaManager.getInstance().loadQuesetions(null);
        QaPlayResultManager.getInstance().loadQaResult(null);
    }

    /**
     * 功能开关
     */
    private void actionSwitch() {
        serviceSwitch.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                User user = UserManager.getInstance().getUser();
                user.setOpen(!user.isOpen());
                UserManager.getInstance().updateUser(user);
                checkServiceStatue();
                if (PhoneActivityService.isAccessibilitySettingsOn(MainActivity.this)) {

                } else {
                    Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, serviceSwitch, getString(R.string.transition_dialog));
                    startActivityForResult(intent, Request_Dialog_Open, options.toBundle());
//                    switchDes.animateText("开启服务后，重新点击这里哦~");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Request_Dialog_Open && resultCode == Request_Dialog_Res_Success){
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, StartService.class));
        if(appBarLayout.getHeight()>0){
            checkServiceStatue();
        }
    }
    private void checkServiceStatue() {
        switchDes.animateText("");
        helloText.animateText("");
        if(PhoneActivityService.isAccessibilitySettingsOn(MainActivity.this)){
            User user = UserManager.getInstance().getUser();
            if(user.isOpen()){
//                helloText.setVisibility(View.INVISIBLE);
                helloText.animateText("");
                changeLayout(true);
            }else{
                helpSwitch.setVisibility(View.INVISIBLE);
                helpSwitch.stopRippleAnimation();
                switchDes.animateText("已关闭,点击小红钮可开启~");
                helloText.animateText(getWelcomString());
                changeLayout(false);
            }
        }else{
            helpSwitch.setVisibility(View.INVISIBLE);
            helpSwitch.stopRippleAnimation();
            User user = UserManager.getInstance().getUser();
            user.setOpen(false);
            UserManager.getInstance().updateUser(user);
            helloText.animateText(getWelcomString());
            switchDes.animateText("请点击小红钮，手动开启服务.");
            changeLayout(false);
        }
    }

    private String getWelcomString(){
        User user = UserManager.getInstance().getUser();
        if(user.getUserName()==null || user.getUserName().length()<1){
            return "欢迎您使用.";
        }else{
            return "欢迎您,"+user.getUserName()+".";
        }
    }


    /**
     * 根据 开关变换布局
     * @param b
     */
    private void changeLayout(final boolean b) {
        ValueAnimator va;
        if(b){
            int i = DpOrPx.dip2px(MainActivity.this, 180f);
            if(appBarLayout.getHeight()==i){
                switchDes.animateText("已开启，正常运行中~");
                return;
            }
           va = ValueAnimator.ofInt(appBarLayout.getHeight(), i);
        }else{
            if(appBarLayout.getHeight()==fullHeightCardBar){
                return;
            }
            va = ValueAnimator.ofInt(appBarLayout.getHeight(), fullHeightCardBar);
        }

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取当前的height值
                int h =(Integer)valueAnimator.getAnimatedValue();
                //动态更新view的高度
                appBarLayout.getLayoutParams().height = h;
                appBarLayout.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (b) {
                    helpSwitch.setVisibility(View.VISIBLE);
                    helpSwitch.startRippleAnimation();
                    switchDes.animateText("已开启，正常运行中~");
                } else {
//                    helloText.setVisibility(View.VISIBLE);
                    helloText.animateText(getWelcomString());
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.setInterpolator(new DecelerateInterpolator());
        va.setDuration(400);
        //开始动画
        va.start();
    }

    private void actionListLogic() {
        if(actionListFragment==null){
            actionListFragment = new ActionListFragment();
        }
        relaceFragment(R.id.fl_main_actionlist,actionListFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            Intent service = new Intent(this, StartService.class);
            startService(service);
            return true;
        }
        return false;
    }

    public static void finishS(){
        if(cacheActivity!=null){
            cacheActivity.finish();
        }
    }

}
