package saver.downloader.videos.inshotvideodownload;

import android.text.Layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

public class AdapterViewPagerMain extends FragmentStatePagerAdapter {
    HistoryFragment historyFragment;
    HomeFragment homeFragment;
    SettingFragment settingFragment;
    public AdapterViewPagerMain(@NonNull FragmentManager fm)
    {
        super(fm);
        homeFragment = new HomeFragment();
        historyFragment = new HistoryFragment();
        settingFragment = new SettingFragment();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return homeFragment;
            case 1:
                return historyFragment;
            case 2:
                return settingFragment;
            default:
                 return  null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
