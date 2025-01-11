package com.example.ecosynergy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_content_modules);

        String subCategory = getIntent().getStringExtra("SUBCATEGORY_NAME");

        TextView title = findViewById(R.id.detail_title);
        TextView description = findViewById(R.id.detail_description);

        title.setText(subCategory); // Set the title dynamically
        description.setText(DummyData.getContentForSubCategoryBasic(subCategory)); // Fetch and display relevant content
    }
}