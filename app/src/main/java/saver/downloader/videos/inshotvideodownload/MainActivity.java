package saver.downloader.videos.inshotvideodownload;

import static android.os.Environment.getExternalStoragePublicDirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.LiveFolders;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Tìm đến folder chứa các video -> Lấy toàn bộ các video và hiển thị lên giao diện
// Progress
public class MainActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public  static final String myfolder = "/DemoReadWriteImage";
    File folder;
    BottomNavigationViewEx bottomNavigationViewEx;
    HistoryFragment historyFragment;
    SettingFragment settingFragment;
    HomeFragment homeFragment;
    AppCompatTextView tv_download, txt_title;
    DrawerLayout drawerLayout;
    ViewPager ViewPager;
    AppCompatTextView tvDownLoad;
    PRDownloader prDownloader;
    FrameLayout left_drawer, right_drawer;
    ArrayList<String> pathArrList;
    RecyclerView rcl_video;
    ArrayList<File> fileArrayList;
    BottomNavigationView bottomNavigationView;
    String FolderDirectory;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_layout);
       // setContentView(R.layout.temp_layout);
        txt_title = findViewById(R.id.txt_title);
       // bottomNavigationViewEx = findViewById(R.id.bottomNav);
        bottomNavigationView = findViewById(R.id.bottomNav);

       // ViewPager = findViewById(R.id.viewpager);
        tv_download = findViewById(R.id.tv_download);
//        drawerLayout = findViewById(R.id.drawer_layout);
//        left_drawer = findViewById(R.id.left_drawer);
        ViewPager = findViewById(R.id.viewpager);
//        right_drawer = findViewById(R.id.right_drawer);
        AdapterViewPagerMain adapterViewPagerMain = new AdapterViewPagerMain(getSupportFragmentManager());
        ViewPager.addOnPageChangeListener(this);
       // bottomNavigationViewEx.setupWithViewPager(ViewPager, true);
       // DownloadFile();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    switch (id)
                    {
                        case R.id.home:
                            Log.e("1", "menu_tab");
                            ViewPager.setCurrentItem(0);
                            break;
                        case R.id.message:
                            Log.e("2", "menu_progress");
                            ViewPager.setCurrentItem(1);
                            break;
                        case R.id.settings:
                            Log.e("3", "menu_finished");
                            ViewPager.setCurrentItem(2);
                            break;
                    }
                    return true;
                }
        });
        ViewPager.setAdapter(adapterViewPagerMain);
        adapterViewPagerMain.notifyDataSetChanged();
    }

    private void checkPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            DownloadFile();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please give permission first", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void getVideoList()
    {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                pathArrList.add(cursor.getString(0));
            }
            cursor.close();
        }
        Log.e("pathList", pathArrList.toString());
    }
    private void getAllFile(String FolderDirectory)
    {
        File[] getList = folder.listFiles();
        for(File x : getList)
        {
            if(x.isFile())
            {
                fileArrayList.add(x);
                Log.e("List", x.toString());
            }
            else if(x.isDirectory())
            {
                getAllFile(x.getAbsolutePath());
            }
        }
    }

    private void DownloadFile() {
        // Chạy vào file mới có tên là myMusic.mp4
        folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + myfolder);
       // String url = "https://drive.google.com/file/d/1cZXhGSHwxEJtyY4suPS_Ovg7eotN1j7Z/view?usp=sharing";String url = "https://video-iad3-1.xx.fbcdn.net/v/t39.25447-2/308972057_2155007701327901_7058151639585917177_n.mp4?_nc_cat=107&vs=b166a68d295ee40&_nc_vs=HBksFQAYJEdCbUthaElkb0liVTk2Y0hBUGtJWEVDRmwtTmhibWRqQUFBRhUAAsgBABUAGCRHRVRsTHhIRzhYdVBrNjhDQVB6X2t0YXd6RFo1YnY0R0FBQUYVAgLIAQBLBogScHJvZ3Jlc3NpdmVfcmVjaXBlATENc3Vic2FtcGxlX2ZwcwAQdm1hZl9lbmFibGVfbnN1YgAgbWVhc3VyZV9vcmlnaW5hbF9yZXNvbHV0aW9uX3NzaW0AKGNvbXB1dGVfc3NpbV9vbmx5X2F0X29yaWdpbmFsX3Jlc29sdXRpb24AEWRpc2FibGVfcG9zdF9wdnFzABUAJQAcAAAmttPdoJLDxgIVAigCQzMYC3Z0c19wcmV2aWV3HBdAfHrhR64UexgnZGFzaF9yMl9hdmNfZ2VuMWF2Y19sY19xNzBfZnJhZ18yX3ZpZGVvEgAYGHZpZGVvcy52dHMuY2FsbGJhY2sucHJvZDgSVklERU9fVklFV19SRVFVRVNUGwqIFW9lbV90YXJnZXRfZW5jb2RlX3RhZwZvZXBfaGQTb2VtX3JlcXVlc3RfdGltZV9tcwEwDG9lbV9jZmdfcnVsZQd1bm11dGVkE29lbV9yb2lfcmVhY2hfY291bnQGOTU0MzU1EW9lbV9pc19leHBlcmltZW50AAxvZW1fdmlkZW9faWQPNzA1MzYzNDIzODc3MTE4Em9lbV92aWRlb19hc3NldF9pZBAxMTg0NDM2ODgyMzk2NjA1FW9lbV92aWRlb19yZXNvdXJjZV9pZA83MTgwMzUwODI3ODYwMTEcb2VtX3NvdXJjZV92aWRlb19lbmNvZGluZ19pZBAxMDcxODg3OTYwMTY3MjgzDnZ0c19yZXF1ZXN0X2lkACUCHAAlvgEbB4gBcwM1OTYCY2QKMjAyMi0wNi0yMQNyY2IGOTU0MzAwA2FwcAZWaWRlb3MCY3QZQ09OVEFJTkVEX1BPU1RfQVRUQUNITUVOVBNvcmlnaW5hbF9kdXJhdGlvbl9zCjQ1NS43MDEzMzMCdHMVcHJvZ3Jlc3NpdmVfZW5jb2RpbmdzAA%3D%3D&ccb=1-7&_nc_sid=189a0e&efg=eyJ2ZW5jb2RlX3RhZyI6Im9lcF9oZCJ9&_nc_ohc=cQe53jT3Ad0AX84wvN0&_nc_ht=video-iad3-1.xx&oh=00_AT-LhQHZJoC02KJn-KXVjLygKJ8jK8-dSxoR2c9Oj2AKBQ&oe=6338A9DB&_nc_rid=481188551463677&dl=1";
        String url = "https://th.bing.com/th/id/OIP.x10SlwDGUtqmv9yK-t838AHaNw?pid=ImgDet&rs=1";
        PRDownloader.download(url,folder.getPath(), "myfile.png")
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

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(getApplicationContext(), "download complete", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(getApplicationContext(), "download uncomplete", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position)
        {
            case 0:
                bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                break;
            case 1:
                bottomNavigationView.getMenu().findItem(R.id.message).setChecked(true);
                break;
            case 2:
                bottomNavigationView.getMenu().findItem(R.id.settings).setChecked(true);
                break;
            default: break;
        }
        ViewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {

    }
}