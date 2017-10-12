package com.szw.tools.wechatgroupandroid.pages.qa;

import android.os.AsyncTask;
import android.util.Log;

import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng_Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaIngLoadListener;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaPlayListenerListener;
import com.szw.tools.wechatgroupandroid.service.domain.WeChat;
import com.szw.tools.wechatgroupandroid.utils.ObjSearUtils;
import com.szw.tools.wechatgroupandroid.utils.Sputils;
import java.io.File;

/**
 * Created by shenmegui on 2017/9/26.
 */
public class QaIngManager  extends Manager {
    private static QaIngManager qaIngManager;
    private QaPlayer qaPlayer;
    public static QaIngManager getInstance(){
        if(qaIngManager==null){
            qaIngManager = new QaIngManager();
        }
        return qaIngManager;
    }

    private QaIngManager() {

    }

    public QaPlayer getQaPlayer(){
        if(qaPlayer==null){
            qaPlayer = new QaPlayer();
        }
        return qaPlayer;
    }

    private String cacheQuestionsId="";

    public void setQaNowQuestions(Questions qaQuestions){
        if(qaQuestions==null){
            cacheQuestionsId = "";
            Sputils.getInstance(WeChatAdnroidGroup.getInstance()).putFiled(this,"qaing","");
            return;
        }
        cacheQuestionsId = qaQuestions.getId();
        Sputils.getInstance(WeChatAdnroidGroup.getInstance()).putFiled(this,"qaing",qaQuestions.getId());

    }

    public Questions getQaNowQuestions(){

        if(cacheQuestionsId.length()<1){
            cacheQuestionsId =  Sputils.getInstance(WeChatAdnroidGroup.getInstance()).getField(this,"qaing","");
        }

        if(cacheQuestionsId.length()<1){
            return null;
        }else {
            Questions questionsWithId = QaManager.getInstance().getQuestionsWithId(cacheQuestionsId);
            return questionsWithId==null?new Questions():questionsWithId;
        }
    }

    @Override
    public String getSpNameSpec() {
        return "QaIngManager";
    }

    @Override
    public void init() {

    }

    private AsyncTask loadQuestions;
    public void loadQaIng(final QaIngLoadListener qaIngLoadListener){
        if(loadQuestions!=null){
            loadQuestions.cancel(true);
            loadQuestions = null;
        }
        loadQuestions =  new AsyncTask<Void, Void, Void>() {
            private QaIng qaIng;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    qaIng = ObjSearUtils.readObj(getCacheQaIngFile(),QaIng.class);
                    Log.e("??","--"+qaIng.getQaings().size());
                } catch (Exception e) {
                    e.printStackTrace();
                    qaIng = null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(qaIng == null){
                    qaIng = new QaIng();
                }
                if(qaIngLoadListener!=null){
                    qaIngLoadListener.onLoad(qaIng);
                }
            }
        }.execute();
    }
    public int delectQaIng(){
       return ObjSearUtils.delObj(getCacheQaIngFile());
    }
    public int saveQaing(QaIng qaIng){
        //创建文件
        return ObjSearUtils.saveObj(getCacheQaIngFile(),qaIng);
    }
    private File getCacheQaIngFile(){
        return new File(new File(QaManager.rootFile,QaManager.qaResultFiles),"qaing.cache");
    }


}
