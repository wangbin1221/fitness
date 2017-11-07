package com.example.f.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Administrator on 2017/11/7.
 */

public class SharedPreferencesUtill {
    private Context mContext;

    private String FILE_NAME="share_date";

    public SharedPreferencesUtill(String file_name){
        FILE_NAME = file_name;
    }

    public SharedPreferencesUtill(Context context){
        this.mContext = context;
    }
    /**
     *
     * 保存数据的方法
     */
    public void setParam(String key,Object object){
        String type = object.getClass().getName();
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)){
            editor.putString(key,object.toString());
        }else if ("Integer".equals(type)){
            editor.putInt(key,(Integer)object);
        }else if ("Boolean".equals(type)){
            editor.putBoolean(key,(Boolean)object);
        }else if ("Float".equals(type)){
            editor.putFloat(key,(Float)object);
        }else if ("Long".equals(type)){
            editor.putLong(key,(Long)object);
        }
        editor.commit();
    }
    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     */
    public Object getParam(String key,Object defaultObject){

        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        if ("String".equals(type)){
            return sp.getString(key,(String) defaultObject);
        }else if ("Integer".equals(type)){
            return sp.getInt(key,(Integer) defaultObject);
        }else if ("Boolean".equals(type)){
            return sp.getBoolean(key,(Boolean)defaultObject);
        }else if ("Float".equals(type)){
            return sp.getFloat(key,(Float)defaultObject);
        }else if ("Long".equals(type)){
            return sp.getLong(key,(Long)defaultObject);
        }
        return null;
    }
    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @return
     */
    // Delete
    public void remove( String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
