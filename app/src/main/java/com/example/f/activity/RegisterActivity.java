package com.example.f.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.f.R;
import com.example.f.ui.PickerView;
import com.example.f.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private PickerView pickerView;
    private List<String> mDatas;
    private Toolbar toolbar;
    private String num;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showMessage(num);
            }
        });
        pickerView = (PickerView) findViewById(R.id.pickerview);
        mDatas=new ArrayList<>();
        mDatas.add("男");
        mDatas.add("女");
        mDatas.add("保密");
        pickerView.setData(mDatas);
        pickerView.setSelected(1);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                num = text;
            }
        });
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
