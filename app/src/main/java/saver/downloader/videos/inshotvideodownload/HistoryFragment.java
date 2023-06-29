package saver.downloader.videos.inshotvideodownload;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    AppCompatTextView tv_download, txt_title;
    RecyclerView rcl_video;
    AppCompatImageView image_nodata;
    AppCompatTextView txt_nodata;
    ArrayList<File> pathArrList = new ArrayList<>();
    String name;
    public  static final String myfolder = "/DemoReadWriteImage";
    String FolderDirectory;
    File folder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        rcl_video = v.findViewById(R.id.rcl_video);
        image_nodata = v.findViewById(R.id.image_nodata);
        txt_title = v.findViewById(R.id.txt_title);
        tv_download = v.findViewById(R.id.tv_download);
        txt_nodata = v.findViewById(R.id.txt_nodata);
        FolderDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + myfolder;
        Log.e("a", FolderDirectory);
        folder = new File(FolderDirectory);
        Context context = this.getContext();

        tv_download.setText("");
        txt_title.setText("History");

        if(FolderDirectory.length() != 0) {
            getAllFile(FolderDirectory);
            AdapterVideo adapterVideo = new AdapterVideo(context, pathArrList, R.layout.custom_detail_2);
            rcl_video.setAdapter(adapterVideo);
            adapterVideo.notifyDataSetChanged();
        }
        else
        {
            image_nodata.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.VISIBLE);
        }
        return v;
    }
    private void getAllFile(String FolderDirectory)
    {
        pathArrList.clear();
        File[] getList = folder.listFiles();
            Log.e("List",  "getAllFile - FolderDirectory " + FolderDirectory);
            Log.e("List",  "getAllFile - count " + getList.length);
            for(File x : getList)
            {
                if(x.isFile())
                {
                    pathArrList.add(x);
                    image_nodata.setVisibility(View.GONE);
                    txt_nodata.setVisibility(View.GONE);
                }
                else if(x.isDirectory())
                {
                    getAllFile(x.getAbsolutePath());
                }
            }
    }
}
