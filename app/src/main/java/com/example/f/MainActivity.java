package com.example.f;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.f.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private CircleImageView micon_image;//裁剪的图片

    private TextView mTextView; // 登录注册按钮

    public static final int TAKE_PHOTO = 0;

    public static final int CHOOSE_PHOTO = 1;

    private Uri imageUri;

    private final String TAG = "MainActivity";


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
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT >=19){
                        ToastUtil.showMessage(">19");
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                        ToastUtil.showMessage("<19");
                    }
                }
                break;
                default:break;
        }
    }

    /**
     * API19以上调用
     * @param data  相机返回的数据
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getApplicationContext(),uri)){
            //如果是document类型
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                ToastUtil.showMessage("1");
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                ToastUtil.showMessage("2");
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if ("content".equalsIgnoreCase(uri.getScheme())){
                //如果是content类型的,普通方式处理
                ToastUtil.showMessage("3");
                imagePath = getImagePath(uri,null);
            }else if ("file".equalsIgnoreCase(uri.getScheme())){
                ToastUtil.showMessage("4");
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    private void displayImage(String imagePath) {
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            micon_image.setImageBitmap(bitmap);
        }else {
            ToastUtil.showMessage("fail to load");
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);

        if (cursor !=null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        Log.e("ssssss","chucuo le ");
        return path;

    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
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
                    //设置弹窗popupwindow
                    showPopueWindow();
                }
            });
        }

    }

    /**
     * 使用相机拍摄照片作为头像
     */
    private void usePhoto() {
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

    /**
     * param  显示弹窗
     */
    private void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;
        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);
        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CHOOSE_PHOTO);
                }else {
                    useAlbum();
                }
                popupWindow.dismiss();
            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takeCamera(RESULT_CAMERA_IMAGE);
                usePhoto();
                popupWindow.dismiss();
            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);
    }
    private void useAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    useAlbum();
                }else{
                    ToastUtil.showMessage("u deny");
                }
                break;
                default: break;


        }
    }
}
