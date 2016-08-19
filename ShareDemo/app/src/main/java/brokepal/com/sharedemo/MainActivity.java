package brokepal.com.sharedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.support.v4.content.FileProvider.getUriForFile;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("-------------"+new DownloadUtils().downFile("http://192.168.1.108:8081/mp3/1.jpg","1.jpg"));
//            }
//        }).start();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share: openShare(); break;
            case R.id.action_share_binary:  openShareBinary(); break;
            case R.id.action_share_multiple:  openShareMultiple(); break;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    //分享文本内容(Send Text Content)
    public void openShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //为intent设置一些标准的附加值，例如：EXTRA_EMAIL, EXTRA_CC, EXTRA_BCC, EXTRA_SUBJECT等。
        // 如果接收程序没有针对那些做特殊的处理，则不会有对应的反应。
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "send title"));
    }

    //分享二进制内容(Send Binary Content)
    public  void openShareBinary(){
//        Uri imageUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/1.jpg"));
        File imagePath = new File(getFilesDir(), "images");
        File newFile = new File(getFilesDir(), "images/1.jpg");
        Uri imageUri = getUriForFile(this,"brokepal.com.sharedemo.fileprovider",newFile);
        Log.i("test","-------------+++++++++"+imageUri.toString());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent, "send picture"));
    }

    //发送多块内容(Send Multiple Pieces of Content)
    public void openShareMultiple(){
        //如果分享3张JPEG的图片，那么MIME类型仍然是image/jpeg。
        //如果是不同图片格式的话，应该是用image/*来匹配那些可以接收任何图片类型的activity。
        //如果需要分享多种不同类型的数据，可以使用*/*来表示MIME。
        Uri imageUri1=null,imageUri2=null;
        //TODO 设置Uri
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        imageUris.add(imageUri1); // Add your image URIs here
        imageUris.add(imageUri2);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Log.i("test",sharedText);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            //TODO 如何根据Uri获取到图片资源，把它展示到UI中
            Log.i("test","-------------+++++++++"+imageUri.toString());
            imageView = (ImageView) this.findViewById(R.id.image);
            try {
                //BitmapFactory.decodeStream(new FileInputStream(new File(imageUri.getPath())))的形式获取图片需要文件的真实Uri
//                imageView.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(new File(imageUri.getPath()))));
                //通过下面的方法可以从一个FileProvider提供的Uri中获取到文件
                ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(imageUri, "r");
                FileDescriptor fd = mInputPFD.getFileDescriptor();
                imageView.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(fd)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }

}
