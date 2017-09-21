package com.szw.tools.wechatgroupandroid;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.szw.tools.wechatgroupandroid.fragment.action.ActionListFragment;
import com.szw.tools.wechatgroupandroid.service.PhoneActivityService;
import com.szw.tools.wechatgroupandroid.view.RippleView;
import com.szw.tools.wechatgroupandroid.view.dialog.DialogActivity;
import com.szw.tools.wechatgroupandroid.view.htext.ScaleTextView;

public class MainActivity extends BaseActivity {
    private RippleView helpSwitch;
    private ScaleTextView switchDes;
    private BaseFragment actionListFragment;
    private FloatingActionButton serviceSwitch;
    private boolean isOpenService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helpSwitch = (RippleView) findViewById(R.id.ripp_view_main_switchopen);
        switchDes = (ScaleTextView) findViewById(R.id.sctv_main_switch_des);
        serviceSwitch = (FloatingActionButton) findViewById(R.id.fab);

//        switchDes.setText("开启");

        actionSwitch();
        actionListLogic();
//



    }

    /**
     * 功能开关
     */
    private void actionSwitch() {
        if(isOpenService){
            helpSwitch.setVisibility(View.VISIBLE);
        }else{
            helpSwitch.setVisibility(View.INVISIBLE);
            helpSwitch.stopRippleAnimation();
        }


        serviceSwitch.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(isOpenService){
                    helpSwitch.setVisibility(View.VISIBLE);
                    helpSwitch.startRippleAnimation();
                }else{
                    Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, serviceSwitch, getString(R.string.transition_dialog));
                    startActivityForResult(intent, 1, options.toBundle());
                    switchDes.animateText("开启服务后，重新点击这里哦~");
                }

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchDes.animateText("开~");
        isOpenService = PhoneActivityService.isAccessibilitySettingsOn(MainActivity.this);
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
}
