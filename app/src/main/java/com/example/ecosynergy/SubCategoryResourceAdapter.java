package com.example.ecosynergy;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class SubCategoryResourceAdapter extends BaseAdapter {

    private DataSnapshot dataSnapshot;
    private String category, branch; // Category name
    private final List<DataResource.Subcategory> subcategoryList; // List of subcategories
    private final OnSubcategoryClickListener listener; // Listener for click events

    // Constructor
    public SubCategoryResourceAdapter(String category, List<DataResource.Subcategory> subcategories, OnSubcategoryClickListener listener) {
        this.category = category;
        this.subcategoryList = subcategories;
        this.listener = listener;
    }

    // branch = level
    public SubCategoryResourceAdapter(String category, String branch, List<DataResource.Subcategory> subcategories, DataSnapshot dataSnapshot, OnSubcategoryClickListener listener) {
        this.category = category;
        this.branch = branch;
        this.listener = listener;
        this.subcategoryList = subcategories;
        this.dataSnapshot = dataSnapshot;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataResource.Subcategory currentSubcategory = subcategoryList.get(position);

        Log.d("SubCategoryResourceAdapter", "Subcategory Resource: " + currentSubcategory.getArticleTitle());

        // Logging with Log.d() instead of printf
        Log.d("SubCategoryResourceAdapter", currentSubcategory.toString());

        Log.d("SubCategoryResourceAdapter", "Category: " + category);
        Log.d("SubCategoryResourceAdapter", "Branch: " + branch);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            holder = new ViewHolder();

            // Inflating the appropriate layout for subcategories
            convertView = inflater.inflate(R.layout.item_subcategory_resource, parent, false);
            holder.titleTextView = convertView.findViewById(R.id.articleTitle);
            holder.iconImageView = convertView.findViewById(R.id.ic_article);
            holder.discussionTextView = convertView.findViewById(R.id.DiscussionUpNext);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the title, description, and icon for the subcategory
        holder.titleTextView.setText(currentSubcategory.getArticleTitle());

        // Logging for debugging
        Log.d("SubCategoryResourceAdapter", "subcategoryId: " + currentSubcategory.getId());
        Log.d("SubCategoryResourceAdapter", "Article Title: " + currentSubcategory.getArticleTitle());


        // Handle click on article title to go to ResourceContentActivity
        convertView.setOnClickListener(v -> {
            Intent detailIntent = new Intent(parent.getContext(), ResourceContentActivity.class);
            detailIntent.putExtra("Category", category);
            Log.d("SubCategoryResourceAdapter", "Category Passed: " + category);
            detailIntent.putExtra("subcategory", currentSubcategory.getArticleTitle());
            Log.d("SubCategoryResourceAdapter", "Subcategory Passed: " + currentSubcategory.getArticleTitle());
            detailIntent.putExtra("HIERARCHY", branch);
            Log.d("SubCategoryResourceAdapter", "Level Passed: " + branch);
            detailIntent.putExtra("subcategoryId", currentSubcategory.getId());
            detailIntent.putExtra("articleTitle", currentSubcategory.getArticleTitle());
            parent.getContext().startActivity(detailIntent);
        });

        // Set click listener on the Discussion TextView
        holder.discussionTextView.setOnClickListener(v -> {
            Log.d("SubCategoryResourceAdapter", "Discussion TextView clicked for: " + currentSubcategory.getArticleTitle());
            // Open DiscussionActivity with the type
            DiscussionActivity.openDiscussionActivity(
                    parent.getContext(),
                    currentSubcategory.getArticleTitle(),
                    CommentType.RESOURCE // Pass the type here
            );
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        ImageView iconImageView;
        TextView discussionTextView;
    }

    @Override
    public int getCount() {
        return subcategoryList != null ? subcategoryList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return subcategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnSubcategoryClickListener {
        void onSubcategoryClick(DataResource.Subcategory subcategory);
    }
}
