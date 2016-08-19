package com.brokepal.fileresponse;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by chenchao on 16/8/4.
 */
public class ImageAdapter extends BaseAdapter {
    File[] mImageFiles;
    private Context mContext;


    public ImageAdapter(Context c,File[] files) {
        mContext = c;
        mImageFiles = files;
    }

    public int getCount() {
        return mImageFiles.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(240, 240));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        try {
            imageView.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(mImageFiles[position])));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imageView;
    }


}
