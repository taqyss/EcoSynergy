package com.example.ecosynergy;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ModuleFragmentAdapter extends FragmentStateAdapter {

    public ModuleFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        return new ModulesFragment();
    }

    @Override
    public int getItemCount() {
        // Return the number of pages for modules
        return 3;  // Example: 3 pages - Today's Pick, Basic, Advanced
    }
}

