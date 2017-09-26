package com.szw.tools.wechatgroupandroid.pages.qa;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.szw.tools.wechatgroupandroid.Manager;
import com.szw.tools.wechatgroupandroid.R;
import com.szw.tools.wechatgroupandroid.WeChatAdnroidGroup;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.QaIng;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Question;
import com.szw.tools.wechatgroupandroid.pages.qa.doamin.Questions;
import com.szw.tools.wechatgroupandroid.pages.qa.listener.QuestiionLoadListener;

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

/**
 * Created by SuZhiwei on 2017/9/23.
 */
public class QaManager extends Manager {
    private static QaManager qaManager;
    public static File rootFile = new File(Environment.getExternalStorageDirectory(), WeChatAdnroidGroup.getInstance().getString(R.string.qa_questions_root_folder_name));
    public static String qaingFile =WeChatAdnroidGroup.getInstance().getString(R.string.qa_questions_qaing_folder_name);
    private static String endDes = WeChatAdnroidGroup.getInstance().getString(R.string.qa_questions_file_enddes);

    public static QaManager  getInstance(){
        if(qaManager==null){
            qaManager = new QaManager();
        }
        return qaManager;
    }
    private QaManager() {

    }

    private AsyncTask loadQuestions;
    private List<Questions> cacheQuestiones;
    private Map<String,Questions> cacheCursorMap = new HashMap<>();
    private QaIng cacheQaing;

    //----question locl
    public void loadQuesetions(final QuestiionLoadListener questiionLoadListener){
        if(loadQuestions!=null){
            loadQuestions.cancel(true);
            loadQuestions = null;
        }
        loadQuestions =  new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                File[] files = rootFile.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.isFile() && pathname.getName().endsWith(endDes)) {
                            return true;
                        }
                        return false;
                    }
                });
                cacheQuestiones = new ArrayList<Questions>();
                cacheCursorMap = new HashMap<>();
                for(File file:files){
                    ObjectInputStream in;
                    try {
                        in = new ObjectInputStream(new FileInputStream(file));
                       Questions questions = (Questions)in.readObject();
                       cacheQuestiones.add(questions);
                        cacheCursorMap.put(questions.getId(),questions);
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
                if(questiionLoadListener!=null){
                    questiionLoadListener.onLoad(cacheQuestiones.size(),cacheQuestiones);
                }
            }
        }.execute();
    }
    public int delectQuestions(Questions questions){
        File questionFile = getQuestionFile(questions);
        if(questionFile.exists()){
            questionFile.delete();
            return 0;
        }
        return 1;
    }
    public int saveQuestions(Questions questions){
        if(cacheCursorMap.get(questions.getId())==null){
            cacheQuestiones.add(questions);
        }
        cacheCursorMap.put(questions.getId(),questions);

        //创建文件
        File questionFile = getQuestionFile(questions);
        if(!questionFile.getParentFile().exists()){
            questionFile.getParentFile().mkdirs();
        }
        try {
            if(!questionFile.exists()) {
                questionFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        // 写入题库
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(questionFile.getAbsolutePath()));
            out.writeObject(questions);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
    //----question locl - end
    public File getQuestionFile(Questions questions){
        return new File(rootFile,questions.getId()+endDes);
    }
    public Questions getQuestionsWithId(String id){
        return cacheCursorMap.get(id);
    }


    @Override
    public String getSpNameSpec() {
        return "qamanager";
    }

    @Override
    public void init() {

    }

    public List<Questions> getCacheQuestiones() {
        return cacheQuestiones;
    }
}
