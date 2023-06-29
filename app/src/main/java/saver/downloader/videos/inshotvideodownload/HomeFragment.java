package saver.downloader.videos.inshotvideodownload;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.icu.text.CaseMap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {
    String text;
    MediaPlayer mp;
     long rand;
     int min = 1, max= 100;
     int range = max - min + 1;
     LinearLayout layout_checklink_result, onProgress;
     ContentLoadingProgressBar progressBar;
    AppCompatTextView tv_download, txt_title, title, tv_time, txt, txt_download, percent, txt_result_download;
    AppCompatTextView link, paste, tv_check, no_data;
    AppCompatImageView ic_nodata, thumb;
    Context context;
    Guideline guideline;
    ScrollView background;
    TextView tv_percent, currentTime;
    String videoInfor;
    public  static final String myfolder = "/DemoReadWriteImage";
    File folder;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //
        link = v.findViewById(R.id.link);
        paste = v.findViewById(R.id.paste);
        layout_checklink_result = v.findViewById(R.id.view_result);
        tv_percent = v.findViewById(R.id.percent);
        guideline = v.findViewById(R.id.guideline);
        no_data = v.findViewById(R.id.tv_nodata);
        ic_nodata = v.findViewById(R.id.ic_nodata);
        txt_title = v.findViewById(R.id.txt_title);
        tv_download = v.findViewById(R.id.tv_download);
        //
        txt_result_download = layout_checklink_result.findViewById(R.id.txt_result_download);
        percent = layout_checklink_result.findViewById(R.id.percent);
        progressBar = layout_checklink_result.findViewById(R.id.progress);
        txt_download = layout_checklink_result.findViewById(R.id.txt_download);
        thumb = layout_checklink_result.findViewById(R.id.thumb);
        title = layout_checklink_result.findViewById(R.id.tv_title);
        tv_time = layout_checklink_result.findViewById(R.id.tv_time);
        txt = layout_checklink_result.findViewById(R.id.txt);
        //
        tv_download.setTextSize(20);
        tv_download.setVisibility(View.VISIBLE);
        txt_title.setVisibility(View.GONE);
        //
        context = v.getContext();
        tv_check = v.findViewById(R.id.tv_check);
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
                text = (String) cm.getText();
                link.setText(text);
            }
        });
        tv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rand = System.currentTimeMillis();
                String url = (String) link.getText();
                //
                ic_nodata.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                layout_checklink_result.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                percent.setVisibility(View.VISIBLE);
                //
                String serverUrl = "https://download-video.merryblue.llc/api/v1/download/link";
                //checkPermission("https://fb.gg/v/gQioNvi-yW/");
                getDataFromServer(serverUrl, url);
            }
        });
        return  v;
    }
    private void getDataFromServer(String serverUrl, String url) {
        Ion.with(HomeFragment.this)
                .load(serverUrl)
                .setBodyParameter("url", url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            Log.e("kt", result);
                            ///
                            ArrayList<HashMap<String, String>> userList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(result);
                            //////
                            Log.e( "onCompleted: ",jsonObject.toString());
                            JSONObject jsonArray = jsonObject.getJSONObject("links");
                            HashMap<String,String> option = new HashMap<>();
                            option.put("hd", jsonArray.getString("hd"));
                            Log.e("jsonhd", jsonArray.getString("hd"));
                            userList.add(option);
                            String res = jsonArray.getString("hd");
                            Log.e("xxx", res);
                            ///// Get thumbnail
                            String thumbnailString = jsonObject.getString("thumb");
                            Glide.with(context).load(thumbnailString).thumbnail(0.1f).into(thumb);
                            Log.e("thumbnail", thumbnailString);
                            // Get title and name
                            videoInfor = jsonObject.getString("title");
                            Log.e("title", videoInfor);
                            // mp = MediaPlayer.create(context, Uri.parse(text));
                            checkPermission(res);
                        }catch (Exception exception){
                            Log.e("error", "Exception");
                        }
                    }
                });
    }
    private void checkPermission(String url) {
        Dexter.withContext(context.getApplicationContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            DownloadFile(url);
                        }
                        else
                        {
                            Toast.makeText(context.getApplicationContext(), "Please give permission first", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    private void DownloadFile(String url) {
        // Chạy vào folder mới có tên là DemoReadWriteImage
        folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + myfolder);
        // String url = "https://drive.google.com/file/d/1cZXhGSHwxEJtyY4suPS_Ovg7eotN1j7Z/view?usp=sharing";String url = "https://video-iad3-1.xx.fbcdn.net/v/t39.25447-2/308972057_2155007701327901_7058151639585917177_n.mp4?_nc_cat=107&vs=b166a68d295ee40&_nc_vs=HBksFQAYJEdCbUthaElkb0liVTk2Y0hBUGtJWEVDRmwtTmhibWRqQUFBRhUAAsgBABUAGCRHRVRsTHhIRzhYdVBrNjhDQVB6X2t0YXd6RFo1YnY0R0FBQUYVAgLIAQBLBogScHJvZ3Jlc3NpdmVfcmVjaXBlATENc3Vic2FtcGxlX2ZwcwAQdm1hZl9lbmFibGVfbnN1YgAgbWVhc3VyZV9vcmlnaW5hbF9yZXNvbHV0aW9uX3NzaW0AKGNvbXB1dGVfc3NpbV9vbmx5X2F0X29yaWdpbmFsX3Jlc29sdXRpb24AEWRpc2FibGVfcG9zdF9wdnFzABUAJQAcAAAmttPdoJLDxgIVAigCQzMYC3Z0c19wcmV2aWV3HBdAfHrhR64UexgnZGFzaF9yMl9hdmNfZ2VuMWF2Y19sY19xNzBfZnJhZ18yX3ZpZGVvEgAYGHZpZGVvcy52dHMuY2FsbGJhY2sucHJvZDgSVklERU9fVklFV19SRVFVRVNUGwqIFW9lbV90YXJnZXRfZW5jb2RlX3RhZwZvZXBfaGQTb2VtX3JlcXVlc3RfdGltZV9tcwEwDG9lbV9jZmdfcnVsZQd1bm11dGVkE29lbV9yb2lfcmVhY2hfY291bnQGOTU0MzU1EW9lbV9pc19leHBlcmltZW50AAxvZW1fdmlkZW9faWQPNzA1MzYzNDIzODc3MTE4Em9lbV92aWRlb19hc3NldF9pZBAxMTg0NDM2ODgyMzk2NjA1FW9lbV92aWRlb19yZXNvdXJjZV9pZA83MTgwMzUwODI3ODYwMTEcb2VtX3NvdXJjZV92aWRlb19lbmNvZGluZ19pZBAxMDcxODg3OTYwMTY3MjgzDnZ0c19yZXF1ZXN0X2lkACUCHAAlvgEbB4gBcwM1OTYCY2QKMjAyMi0wNi0yMQNyY2IGOTU0MzAwA2FwcAZWaWRlb3MCY3QZQ09OVEFJTkVEX1BPU1RfQVRUQUNITUVOVBNvcmlnaW5hbF9kdXJhdGlvbl9zCjQ1NS43MDEzMzMCdHMVcHJvZ3Jlc3NpdmVfZW5jb2RpbmdzAA%3D%3D&ccb=1-7&_nc_sid=189a0e&efg=eyJ2ZW5jb2RlX3RhZyI6Im9lcF9oZCJ9&_nc_ohc=cQe53jT3Ad0AX84wvN0&_nc_ht=video-iad3-1.xx&oh=00_AT-LhQHZJoC02KJn-KXVjLygKJ8jK8-dSxoR2c9Oj2AKBQ&oe=6338A9DB&_nc_rid=481188551463677&dl=1";
       // String url = "https://th.bing.com/th/id/OIP.x10SlwDGUtqmv9yK-t838AHaNw?pid=ImgDet&rs=1";
        // url = "https://www.facebook.com/100028017675936/videos/846070206413983/";
        String fileName = "FB_" + rand + ".mp4";
        PRDownloader.download(url,folder.getPath(), fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                }).setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long currentBytes = progress.currentBytes;
                        progressBar.setProgress((int) currentBytes);
                        long MAXBytes = progress.totalBytes;
                        long percentX = (currentBytes / MAXBytes) * 100;
                        percent.setText(percentX +"%");
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(context.getApplicationContext(), "download complete", Toast.LENGTH_LONG).show();
                        txt_result_download.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        percent.setVisibility(View.GONE);
                        title.setText(videoInfor);
                        txt.setText(videoInfor);
//                         //Get video time
//                        int duration =  mp.getDuration();
//                        String videoTime = String.format("%d%d", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration));
//                        tv_time.setText(videoTime);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(context.getApplicationContext(), "download uncomplete", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
