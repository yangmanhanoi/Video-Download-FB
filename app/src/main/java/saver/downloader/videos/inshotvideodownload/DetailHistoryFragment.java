package saver.downloader.videos.inshotvideodownload;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

public class DetailHistoryFragment extends Fragment {
    public static DetailHistoryFragment newInstance(String uri)
    {
        DetailHistoryFragment detailHistoryFragment = new DetailHistoryFragment();
        Bundle args = new Bundle();
        args.putString("uri", uri);
        detailHistoryFragment.setArguments(args);
        return detailHistoryFragment;
    }
    View mBottomLayout;
    View mVideoLayout;
    FrameLayout video_layout;
    FrameLayout native_ad_word_detail;
    UniversalVideoView universalVideoView;
    UniversalMediaController universalMediaController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_history, container, false);
        universalVideoView = v.findViewById(R.id.videoView);
        universalMediaController = v.findViewById(R.id.media_controller);
        native_ad_word_detail = v.findViewById(R.id.native_ad_word_detail);
        video_layout = v.findViewById(R.id.video_layout);
        universalVideoView.setMediaController(universalMediaController);
        //
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

        //
        Bundle bundle = getArguments();
        String link = bundle.getString("uri");
        Log.e("ktxx", link.toString());
        //
        Uri myUri = Uri.parse(link);
        universalVideoView.setVideoURI(myUri);
        universalVideoView.requestFocus();
        universalVideoView.start();

        return v;
    }
}