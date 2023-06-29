package saver.downloader.videos.inshotvideodownload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReadWriteImageDemo {
    public static final String myFolder = "/DemoReadWriteImage";
    public boolean writeImagetoInternal(Bitmap bitmap, String fileName, Context context)
    {
        try
        {
            FileOutputStream fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            // Save image to internal memory
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
        }catch (Exception e)
        {
            return false;
        }
        return true;

    }
    public boolean writeImagetoSD(Bitmap bitmap, String fileName, Context context)
    {
        try
        {
            // Get full path of folder
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + myFolder;
            // Create new folder
            File dirs = new File(fullPath);
            if(!dirs.exists())
            {
                dirs.mkdirs();
            }
            // Create a new file
            File myfile = new File(fullPath, fileName);
            if(!myfile.exists())
            {
                myfile.createNewFile();
            }
            // Create FileOutputStream
            FileOutputStream fout = new FileOutputStream(myfile);
            // Save image to SD
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
        }catch (Exception e)
        {
            return  false;
        }
        return  true;
    }
    private boolean isSDcardready()
    {
        boolean isSDcardready = false;
        String sdCardstate = Environment.getExternalStorageState();
        if(sdCardstate.equals(Environment.MEDIA_MOUNTED) || sdCardstate.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            return true;
        }
        else  return  false;
    }
    public Bitmap readBitmap(String fileName, Context context)
    {
        Bitmap img = null;
        // Read img from SD card
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + myFolder + "/" + fileName;
        try
        {
            img = BitmapFactory.decodeFile(fullPath);
        }catch (Exception e)
        {
            Log.i("DemoReadWriteImage", "Can not read image from SD card");
        }
        // if can not read from sd card we try to read from internal memory
        try
        {
            File myFile = context.getFileStreamPath(fileName);
            FileInputStream fin = new FileInputStream(myFile);
            img = BitmapFactory.decodeStream(fin);
        }catch (Exception e)
        {
            Log.i("DemoReadWriteImage", "Can not read image from internal memory");
        }
        return  img;
    }
    public void deleteImage(String fileName, Context context)
    {
        // internal memory
        context.deleteFile(fileName);
        // SD card
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + myFolder + "/" + fileName;
        File myFile = new File(fullPath);
        myFile.delete();
    }



}
