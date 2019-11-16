package edu.temple.spiapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;

    //test commit, making sure I have version control setup properly.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new AccountFragment(), "account");
        adapter.addFragment(new CameraFragment(), "camera");
        adapter.addFragment(new NotifFragment(), "notifications");
        adapter.addFragment(new VideoFragment(), "videos");
        viewPager.setAdapter(adapter);
    }
}
