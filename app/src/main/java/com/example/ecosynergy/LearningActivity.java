package com.example.ecosynergy;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LearningActivity extends BaseActivity {
    private TabLayout topTabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        // Set up the toolbar with back button enabled
        setupToolbar(true);

        //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Modules and Resources");

        // Set up bottom navigation
        setupBottomNavigation();

        // Initialize views
        viewPager = findViewById(R.id.view_pager);
        topTabLayout = findViewById(R.id.top_tab_layout);

        // Set up ViewPager with the adapter
        viewPager.setAdapter(new LearningPagerAdapter(this));

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(topTabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Today's Pick");
                    break;
                case 1:
                    tab.setText("Modules");
                    break;
                case 2:
                    tab.setText("Resources");
                    break;
            }
        }).attach();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_learning; // This is the ID of the "Learning" menu item
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

