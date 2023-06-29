package saver.downloader.videos.inshotvideodownload;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
//    private ActivitySplashBinding binding;
//    private Language currentLanguageSelected;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_splash));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
