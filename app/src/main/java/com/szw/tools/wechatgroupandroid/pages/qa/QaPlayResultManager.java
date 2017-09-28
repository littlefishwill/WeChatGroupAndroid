package com.szw.tools.wechatgroupandroid.pages.qa;

import android.os.AsyncTask;
import android.util.Log;

import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaResult;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaResultItem;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QaResultLoadListener;
import com.szw.tools.wechatgroupandroid.service.WeChatUtils;
import com.szw.tools.wechatgroupandroid.service.domain.Chat;
import com.szw.tools.wechatgroupandroid.utils.ObjSearUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by shenmegui on 2017/9/28.
 */
public class QaPlayResultManager {
    public static File QaResultDir = new File(QaManager.rootFile,QaManager.qaResultFiles);
    private static  QaPlayResultManager qaPlayResultManager;
    public  static QaPlayResultManager getInstance(){
        if(qaPlayResultManager==null){
            qaPlayResultManager = new QaPlayResultManager();
        }
        return qaPlayResultManager;
    }

    private QaPlayResultManager() {

    }

    //------ question recodes;
    private QaResult qaResult;

    public void onStar(Questions questions){
        qaResult = new QaResult();
        qaResult.setId(UUID.randomUUID().toString());
        qaResult.setStartTime(System.currentTimeMillis());
        qaResult.setQaLibraryName(questions.getTitle());
        qaResult.setWeChat(WeChatUtils.getInstance().getCacheWeChatGroup());
        qaResult.setQaLibraryId(questions.getId());
        qaResult.setQaLibraryAuthorName(questions.getAuthor());
        qaResult.setQaResultItems(new ArrayList<QaResultItem>());
        Log.e("QaPlayResultManager", "onStart");


}

    public void onPlayIng(Question question){
        Log.e("QaPlayResultManager","onPlayIng");
        QaResultItem qaResultItem; qaResultItem = new QaResultItem();
        qaResultItem.setQuestion(question);
        qaResultItem.setErrorWechats(new ArrayList<Chat>());
        qaResultItem.setRightWechats(new ArrayList<Chat>());
        qaResult.getQaResultItems().add(qaResultItem);

    }

    public void onPlayEnd(){

    }

    public void onAnswer(Chat chat, boolean isRight){
        if(qaResult == null || qaResult.getQaResultItems()==null){
            return;
        }



        QaResultItem qaResultItem = qaResult.getQaResultItems().get(qaResult.getQaResultItems().size() - 1);

        if(chat.getMessage().contains(qaResultItem.getQuestion().getDes()) &&chat.getMessage().endsWith(")")){
            return;
        }

        Log.e("QaPlayResultManager","onAnswer:"+chat.getName()+":"+chat.getMessage()+"?"+isRight+":"+qaResultItem.getErrorWechats().size());
        if(isRight){
            qaResult.getQaResultItems().get(qaResult.getQaResultItems().size()-1).getRightWechats().add(chat);
        }else{
            qaResult.getQaResultItems().get(qaResult.getQaResultItems().size()-1).getErrorWechats().add(chat);
        }
    }

    public void onStop(){
        Log.e("QaPlayResultManager","onStop");
        if(qaResult==null){
            return;
        }
        saveQuestions(qaResult);
        qaResult = null;

    }

    private File getCacheQaIngFile(QaResult qaResult){
        return new File(QaResultDir,qaResult.getId()+QaManager.endDesResult);
    }


    //--------------- for load
    private AsyncTask loadQaResultTask;
    private List<QaResult> qaResults;
    private Map<String,QaResult> qaResultMap;
    public void loadQaResult(final QaResultLoadListener qaResultLoadListener){
        if(loadQaResultTask !=null){
            loadQaResultTask.cancel(true);
            loadQaResultTask = null;
        }
        loadQaResultTask =  new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                File[] files = QaResultDir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.isFile() && pathname.getName().endsWith(QaManager.endDesResult)) {
                            return true;
                        }
                        return false;
                    }
                });
                qaResults = new ArrayList<QaResult>();
                qaResultMap = new HashMap<>();

                if(files==null){
                    return null;
                }

                for(File file:files){
                    ObjectInputStream in;
                    try {
                        in = new ObjectInputStream(new FileInputStream(file));
                        QaResult qaResult = (QaResult)in.readObject();
                        qaResults.add(qaResult);
                        qaResultMap.put(qaResult.getId(),qaResult);
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(qaResultLoadListener!=null){
                    qaResultLoadListener.onLoad(qaResults);
                }
            }
        }.execute();
    }

    public int saveQuestions(QaResult qaResult){
        qaResult.setEndTime(System.currentTimeMillis());
        if(qaResultMap.get(qaResult.getId())==null){
            qaResults.add(qaResult);
        }
        qaResultMap.put(qaResult.getId(),qaResult);

        //写入文件
        ObjSearUtils.saveObj(getCacheQaIngFile(qaResult),qaResult);

        return 0;
    }

    public List<QaResult> getQaResults() {
        if(qaResults==null){
            qaResults = new ArrayList<>();
        }
        return qaResults;
    }
}
