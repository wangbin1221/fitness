package com.example.f;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.f.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private CircleImageView micon_image;//裁剪的图片

    public static final int TAKE_PHOTO = 0;

    public static final int CHOOSE_PHOTO = 1;

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try{
                        //拍摄的图片显示出来
                        Bitmap photo_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        micon_image.setImageBitmap(photo_bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
                default:break;
        }
    }

    /**
     * 界面初始化
     */
    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**
         * 在直接获取控件的时候会空指针异常
         * 先获取到headerview,在获取不会出错
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View view = navigationView.getHeaderView(0);
        micon_image = (CircleImageView) view.findViewById(R.id.icon_image);
        if (micon_image != null) {
            micon_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.showMessage("sa");
                    File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                    try{
                        if (outputImage.exists()){
                            outputImage.delete();   //如果已经存在就删除掉.
                        }
                        outputImage.createNewFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24){  //7.0以后的U日需要路径封装
                        imageUri = FileProvider.getUriForFile(MainActivity.this,"com.example.f.fileprovider",outputImage);
                    }else{
                        imageUri = Uri.fromFile(outputImage);
                    }
                    //---------启动相机------------
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,TAKE_PHOTO);
                }
            });
        }

    }
}
