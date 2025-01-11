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

    private final List<DataResource.Subcategory> subcategoryList; // List of subcategories

    // Constructor
    public SubCategoryResourceAdapter(List<DataResource.Subcategory> subcategories) {
        this.subcategoryList = subcategories;
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
            holder.iconImageView = convertView.findViewById(R.id.ic_video);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the title, description, and icon for the subcategory
        holder.titleTextView.setText(currentSubcategory.getArticleTitle());
        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        ImageView iconImageView;
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
}
