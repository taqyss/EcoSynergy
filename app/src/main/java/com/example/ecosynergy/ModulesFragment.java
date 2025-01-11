package com.example.ecosynergy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class ModulesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_module, container, false);

        TabLayout tabLayout = rootView.findViewById(R.id.sub_tab_layout_modules);
        tabLayout.setVisibility(View.VISIBLE);

        // Set up the first fragment initially
        Fragment basicFragment = MainLearningFragmentsHandler.newInstance("Basic");
        replaceFragment(basicFragment);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:  // Basic
                        fragment = MainLearningFragmentsHandler.newInstance("Basic");
                        break;
                    case 1:  // Intermediate
                        fragment = MainLearningFragmentsHandler.newInstance("Intermediate");
                        break;
                    case 2:  // Advanced
                        fragment = MainLearningFragmentsHandler.newInstance("Advanced");
                        break;
                }
                replaceFragment(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: Handle tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle tab reselection
            }
        });


        return rootView;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_modules, fragment);
        transaction.commit();
    }
}

