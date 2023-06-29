package saver.downloader.videos.inshotvideodownload;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment implements View.OnClickListener{
    AppCompatTextView tv_download, txt_title;
    AppCompatTextView download, language, privacy, about_app;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        //
        txt_title = v.findViewById(R.id.txt_title);
        tv_download = v.findViewById(R.id.tv_download);
        tv_download.setText("");
        txt_title.setText("Setting");
        //
        download = v.findViewById(R.id.download);
        language = v.findViewById(R.id.language);
        privacy = v.findViewById((R.id.privacy));
        about_app = v.findViewById((R.id.about_app));

        download.setOnClickListener(this);
        language.setOnClickListener(this);
        privacy.setOnClickListener(this);
        about_app.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id)
        {
            case R.id.download:

                break;
            case R.id.language:
                break;
            case R.id.privacy:
                break;
            case R.id.about_app:
                break;
            default:break;
        }
    }
}
