package com.example.ecosynergy;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ResourceFragmentAdapter extends FragmentStateAdapter {

    public ResourceFragmentAdapter(LearningActivity fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        // Return the same fragment for all positions
        return new ResourcesFragment();  // Replace with your actual fragment
    }

    @Override
    public int getItemCount() {
        return 3;  // Number of tabs (Articles, Reports, Toolkits)
    }
}


