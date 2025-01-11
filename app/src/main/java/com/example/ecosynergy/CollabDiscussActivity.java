package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CollabDiscussActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_discuss1);

        // Set up the toolbar with a back button
        setupToolbar(true);
        getSupportActionBar().setTitle("Discussion Forum");

        // Set up bottom navigation
        setupBottomNavigation();

        // Setup Create Group Button
        Button createGroupButton = findViewById(R.id.CreateGroupButton);
        createGroupButton.setOnClickListener(view -> {
            Intent intent = new Intent(CollabDiscussActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // ID of the collaboration navigation item
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

