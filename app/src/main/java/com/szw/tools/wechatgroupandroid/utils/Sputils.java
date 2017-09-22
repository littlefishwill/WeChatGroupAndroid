package com.szw.tools.wechatgroupandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.szw.tools.wechatgroupandroid.Manager;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class Sputils {
    private Context context;
    private static Sputils sputils;
    public static Sputils getInstance(Context context){
        if(sputils==null){
            sputils =new Sputils(context);
        }

        return sputils;
    }

    private Sputils(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences(String file){
        SharedPreferences preferences = context.getSharedPreferences(file,
                Context.MODE_PRIVATE);
        return preferences;
    }

    public  void putFiled(Manager baseManager, String key, Object objec){
        SharedPreferences sharedPreferences = getSharedPreferences(baseManager.getSpNameSpec());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Class<?> aClass = objec.getClass();
        if(aClass.equals(String.class)){
            edit.putString(key,(String)objec);
        }else if(aClass.equals(Integer.class)){
            edit.putInt(key, (int) objec);
        }else if(aClass.equals(Long.class)){
            edit.putLong(key, (Long) objec);
        }else if(aClass.equals(Boolean.class)){
            edit.putBoolean(key, (Boolean) objec);
        }else if(aClass.equals(Float.class)){
            edit.putFloat(key, (float) objec);
        }

        edit.commit();
    }

    public void putObject(Manager manager,Object object){
        putFiled(manager,object.getClass().getName(),new Gson().toJson(object));
    }

    public <T extends Object> T getObject(Manager manager,Class<T> tClass){
        return new Gson().fromJson(getField(manager,tClass.getName(),""),tClass);
    }

    public <T extends Object> T getField(Manager baseManager,String key,T  objec){

        SharedPreferences sharedPreferences = getSharedPreferences(baseManager.getSpNameSpec());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Class<?> aClass = objec.getClass();
        if(aClass.equals(String.class)){
           return (T)sharedPreferences.getString(key, (String) objec);
        }else if(aClass.equals(Integer.class)){
            return (T)(Integer)sharedPreferences.getInt(key, (Integer) objec);
        }else if(aClass.equals(Long.class)){
            return (T)(Long)sharedPreferences.getLong(key, (Long) objec);
        }else if(aClass.equals(Boolean.class)){
            return (T)(Boolean)sharedPreferences.getBoolean(key, (Boolean) objec);
        }else if(aClass.equals(Float.class)){
            return (T)(Float)sharedPreferences.getFloat(key, (Float) objec);
        }

        return objec;
    }



}
