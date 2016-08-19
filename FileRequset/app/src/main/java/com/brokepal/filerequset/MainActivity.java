package com.brokepal.filerequset;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private ImageView imageView;
    private Intent mRequestFileIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        imageView=(ImageView)findViewById(R.id.image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFile();
            }
        });
    }

    protected void requestFile() {
        /**
         * When the user requests a file, send an Intent to the
         * server app.
         * files.
         */
        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");
        startActivityForResult(mRequestFileIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            Uri returnUri = returnIntent.getData();

            //获取文件相关信息
            //获取文件的MIME类型
            String mimeType = getContentResolver().getType(returnUri);
            //获取文件名及文件大小
            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String name=returnCursor.getString(nameIndex);
            String size=returnCursor.getString(sizeIndex);

            try {
                //把拿到的图片放到ImageView中
                ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
                FileDescriptor fd = mInputPFD.getFileDescriptor();
                imageView.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(fd)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity", "File not found.");
                return;
            }
        }
    }
}
