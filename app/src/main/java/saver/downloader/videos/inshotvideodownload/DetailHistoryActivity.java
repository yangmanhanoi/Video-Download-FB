package saver.downloader.videos.inshotvideodownload;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class DetailHistoryActivity extends AppCompatActivity {
    View mBottomLayout;
    View mVideoLayout;
    FrameLayout video_layout;
    FrameLayout native_ad_word_detail;
    UniversalVideoView universalVideoView;
    UniversalMediaController universalMediaController;
    Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        //
        String link = getIntent().getStringExtra("uri");
        Log.e("link", link.toString());
        //
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        universalVideoView = findViewById(R.id.videoView);
        universalMediaController = findViewById(R.id.media_controller);
        native_ad_word_detail = findViewById(R.id.native_ad_word_detail);
        video_layout = findViewById(R.id.video_layout);
        universalVideoView.setMediaController(universalMediaController);
        ////
        universalVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            private int cachedHeight = 800;
            private boolean isFullscreen;
            @Override
            public void onScaleChange(boolean isFullscreen) {
                this.isFullscreen = isFullscreen;
                if(isFullscreen)
                {
                    ViewGroup.LayoutParams layoutParams = video_layout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    video_layout.setLayoutParams(layoutParams);
                    //GONE the unconcerned views to leave room for video and controller
                    native_ad_word_detail.setVisibility(View.GONE);
                }
                else
                {
                    ViewGroup.LayoutParams layoutParams = video_layout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = this.cachedHeight;
                    video_layout.setLayoutParams(layoutParams);
                    native_ad_word_detail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) {
                Log.e("Pause", "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) {
                Log.e("Start", "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {
                Log.e("BufferStart", "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {
                Log.e("BufferEnd", "onBufferingEnd UniversalVideoView callback");
            }
        });
        Uri myUri = Uri.parse(link);
        universalVideoView.setVideoURI(myUri);
        universalVideoView.requestFocus();
        universalVideoView.start();
    }
}