package com.example.f.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.f.R;

public class InformationAcitivity extends AppCompatActivity {
        private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("个人信息");
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {//mActionBarToolbar就是android.support.v7.widget.Toolbar
           toolbar.setTitle("");//设置为空，可以自己定义一个居中的控件，当做标题控件使用
        }
    }
}
