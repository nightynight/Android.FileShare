package com.brokepal.fileresponse;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/8/9.
 */
public class FileUtils {
    private String DataDirectory;

    public FileUtils() {
        // 得到内部存储设备的目录
        /*
        Environment.getDataDirectory()
        你想在/data文件夹下一级目录进行操作是不被允许的。
        能操作文件夹只有两个地方：
        1.sdcard
        2./data/data/<package_name>/files/
         */
        DataDirectory = Environment.getDataDirectory().toString()+File.separator+"data/com.brokepal.fileresponse/files/";
    }


    /**
     * 在内部存储设备上创建文件
     *
     * @throws IOException
     */
    public File createFile(String fileName, String dir)
            throws IOException {
        File file = new File(DataDirectory + dir + File.separator + fileName);
        System.out.println("file---->" + file);
        file.createNewFile();
        return file;
    }
    public File createFile(String fileName)
            throws IOException {
        File file = new File(DataDirectory + fileName);
        System.out.println("file---->" + file);
        file.createNewFile();
        return file;
    }

    /**
     * 在内部存储设备上创建目录
     *
     * @param dir
     */
    public File createDir(String dir) {
        File dirFile = new File(DataDirectory + dir + File.separator);
        System.out.println(dirFile.mkdirs());
        return dirFile;
    }

    /**
     * 判断文件夹是否存在
     */
    public boolean isFileExist(String fileName, String path) {
        File file = new File(DataDirectory + path + File.separator + fileName);
        return file.exists();
    }
    public boolean isFileExist(String fileName) {
        File file = new File(DataDirectory  + fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到内部存储设备中
     */
    public File writeFromInput(String path, String fileName,InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            try{
                createDir(path);
            }catch (Exception e){
                e.printStackTrace();
            }
            file = createFile(fileName, path);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int temp;
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    public File writeFromInput(String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            file = createFile(fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int temp;
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
