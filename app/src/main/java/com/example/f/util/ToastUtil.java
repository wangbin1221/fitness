package com.example.f.util;

import android.content.Context;
import android.widget.Toast;

import com.example.f.Application.MyApplication;

/**
 * Created by Administrator on 2017/11/6.
 */

public class ToastUtil {
    private Context mcontext;

    public static  void showMessage(String msg){
        Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_LONG).show();
    }
}
