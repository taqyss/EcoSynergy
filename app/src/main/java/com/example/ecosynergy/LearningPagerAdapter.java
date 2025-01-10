package com.example.ecosynergy;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LearningPagerAdapter extends FragmentStateAdapter {

    public LearningPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new TodaysPickFragment();
            case 1: return new ModulesFragment();
            case 2: return new ResourcesFragment();
            default: return new TodaysPickFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;  // Today's Pick, Modules, Resources
    }
}
