package com.brokepal.fileresponse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

import static android.support.v4.content.FileProvider.getUriForFile;

public class MainActivity extends AppCompatActivity {
    private File mPrivateRootDir;
    private File mImagesDir;
    File[] mImageFiles;
    Intent mResultIntent=new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("-------------"+new DownloadUtils().downFile("http://192.168.1.108:8081/images/2.jpg","images","2.jpg"));
//            }
//        }).start();

        mPrivateRootDir = getFilesDir();
        mImagesDir = new File(mPrivateRootDir, "images");
        mImageFiles = mImagesDir.listFiles();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,mImageFiles));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View
                    v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                Intent inetnt=getIntent();
                String name=getCallingPackage();
                if(getCallingPackage()!=null){//判断是否是其他应用发出请求而打开的Activity
                    File requestFile = mImageFiles[position];
                    Uri imageUri = getUriForFile(MainActivity.this,"com.brokepal.fileresponse.fileprovider",requestFile);
                    if (imageUri != null) {
                        mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//为文件授权
                        mResultIntent.setDataAndType(imageUri, getContentResolver().getType(imageUri));
                        MainActivity.this.setResult(Activity.RESULT_OK, mResultIntent);
                        finish();
                    } else {
                        mResultIntent.setDataAndType(null, "");
                        MainActivity.this.setResult(RESULT_CANCELED, mResultIntent);
                    }
                }
            }
        });
    }
}
