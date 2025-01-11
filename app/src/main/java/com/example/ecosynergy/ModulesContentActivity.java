package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModulesContentActivity extends BaseActivity {

    private ImageButton favoriteButton, downloadButton, transcriptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_content_modules);

        favoriteButton = findViewById(R.id.favorite_button);
        downloadButton = findViewById(R.id.download_button);
        transcriptButton = findViewById(R.id.transcript_button);


        // Set up click listener for the favorite button
        favoriteButton.setOnClickListener(view -> {
            // Handle favorite action here
            Toast.makeText(this, "Favorite button clicked!", Toast.LENGTH_SHORT).show();
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra("SUBCATEGORY_TITLE");
        String description = intent.getStringExtra("SUBCATEGORY_DESCRIPTION");

        TextView titleTextView = findViewById(R.id.detail_title);
        TextView descriptionTextView = findViewById(R.id.detail_description);

        if (title != null) {
            titleTextView.setText(title);
        }

        if (description != null) {
            descriptionTextView.setText(description);
        }
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
