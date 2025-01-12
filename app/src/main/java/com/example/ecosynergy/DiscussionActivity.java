package com.example.ecosynergy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

public class DiscussionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        // Set up Toolbar
        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Discussion");
        }

        setupBottomNavigation();

        // Get data passed via Intent
        String topicId = getIntent().getStringExtra("TOPIC_ID");
        String topicTitle = getIntent().getStringExtra("TOPIC_TITLE");

        // Pass data to the fragment
        Bundle arguments = new Bundle();
        arguments.putString("TOPIC_ID", topicId);
        arguments.putString("TOPIC_TITLE", topicTitle);

        DiscussionFragment fragment = new DiscussionFragment();
        fragment.setArguments(arguments);

        // Load the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public static void openDiscussionActivity(Context context, String category, String subcategory) {
        Intent intent = new Intent(context, DiscussionActivity.class);
        intent.putExtra("CATEGORY", category);
        intent.putExtra("SUBCATEGORY", subcategory);
        context.startActivity(intent);
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
