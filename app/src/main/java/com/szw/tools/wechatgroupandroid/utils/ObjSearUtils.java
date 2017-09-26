package com.szw.tools.wechatgroupandroid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by shenmegui on 2017/9/26.
 */
public class ObjSearUtils {
    public static int saveObj(File file){
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        // 写入题库
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath()));
            out.writeObject(file);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public static  <T extends Object> T readObj(File file,Class<T> classOfT) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        T t1 = (T) in.readObject();
        in.close();
        return t1;
    }

    public static int delObj(File file){
        if(file.exists()){
            file.delete();
            return 0;
        }
        return 1;
    }
}
