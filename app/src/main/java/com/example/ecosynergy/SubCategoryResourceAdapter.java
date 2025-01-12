package com.example.ecosynergy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SubCategoryResourceAdapter extends BaseAdapter {

    private String category; // Category name
    private final List<DataResource.Subcategory> subcategoryList; // List of subcategories
    private final OnSubcategoryClickListener listener; // Listener for click events

    // Constructor
    public SubCategoryResourceAdapter(String category, List<DataResource.Subcategory> subcategories, OnSubcategoryClickListener listener) {
        this.category = category;
        this.subcategoryList = subcategories;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataResource.Subcategory currentSubcategory = subcategoryList.get(position);
        Log.d("SubCategoryResourceAdapter", "Subcategory Resource: " + currentSubcategory.getArticleTitle());

        // Logging with Log.d() instead of printf
        Log.d("SubCategoryResourceAdapter", currentSubcategory.toString());

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
        // Set click listener on the root view
        convertView.setOnClickListener(v -> listener.onSubcategoryClick(currentSubcategory));
        // Set click listener on the Discussion TextView
        holder.discussionTextView.setOnClickListener(v -> {
            // Open DiscussionActivity
            DiscussionActivity.openDiscussionActivity(
                    parent.getContext(),
                    category,
                    currentSubcategory.getArticleTitle()
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
