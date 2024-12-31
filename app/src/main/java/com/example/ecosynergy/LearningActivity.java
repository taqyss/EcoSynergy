package com.example.ecosynergy;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LearningActivity extends BaseActivity {

    private ViewPager2 viewPagerModules, viewPagerResources;
    private TabLayout topTabLayout;
    private TabLayout subTabLayoutModules, subTabLayoutResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_nav); // Use your layout file

        // Initialize your views
        viewPagerModules = findViewById(R.id.view_pager_modules);
        viewPagerResources = findViewById(R.id.view_pager_resources);
        topTabLayout = findViewById(R.id.top_tab_layout);
        subTabLayoutModules = findViewById(R.id.sub_tab_layout_modules);
        subTabLayoutResources = findViewById(R.id.sub_tab_layout_resources);

        // Hide both sub-tab layouts by default
        subTabLayoutModules.setVisibility(View.GONE);
        subTabLayoutResources.setVisibility(View.GONE);

        // Set up the ViewPager adapter for Modules and Resources
        viewPagerModules.setAdapter(new ModuleFragmentAdapter(this));
        viewPagerResources.setAdapter(new ResourceFragmentAdapter(this));

        // Set up the top TabLayout with ViewPager
        new TabLayoutMediator(topTabLayout, viewPagerModules, (tab, position) -> {
            tab.setText(position == 0 ? "Today's Pick" : position == 1 ? "Modules" : "Resources");
        }).attach();

        // Set default to "Today's Pick" (first tab)
        topTabLayout.selectTab(topTabLayout.getTabAt(0));
        viewPagerModules.setCurrentItem(0);  // Set ViewPager to "Today's Pick" tab by default

        // Listen for tab changes on the top TabLayout
        topTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Show the sub-tab layout for "Modules" or "Resources" based on the selected tab
                if (tab.getPosition() == 1) {  // "Modules" tab
                    subTabLayoutModules.setVisibility(View.VISIBLE);
                    subTabLayoutResources.setVisibility(View.GONE);  // Hide Resources sub-tabs
                } else if (tab.getPosition() == 2) {  // "Resources" tab
                    subTabLayoutResources.setVisibility(View.VISIBLE);
                    subTabLayoutModules.setVisibility(View.GONE);  // Hide Modules sub-tabs
                } else {
                    subTabLayoutModules.setVisibility(View.GONE);
                    subTabLayoutResources.setVisibility(View.GONE);  // Hide both for other tabs
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle unselected tabs if necessary
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle reselection if necessary
            }
        });

        // Set up the sub TabLayout for Modules with ViewPager
        new TabLayoutMediator(subTabLayoutModules, viewPagerModules, (tab, position) -> {
            tab.setText(position == 0 ? "Basic" : position == 1 ? "Intermediate" : "Advanced");
        }).attach();

        // Set up the sub TabLayout for Resources with ViewPager
        new TabLayoutMediator(subTabLayoutResources, viewPagerResources, (tab, position) -> {
            tab.setText(position == 0 ? "Articles" : position == 1 ? "Reports" : "Toolkits");
        }).attach();

        viewPagerModules.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                topTabLayout.selectTab(topTabLayout.getTabAt(position));
            }
        });
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_learning; // This is the ID of the "Learning" menu item
    }
}

