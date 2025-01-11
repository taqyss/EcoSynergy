package com.example.ecosynergy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SubCategoryModuleAdapter extends BaseAdapter {

    private final List<DataModule.Subcategory> subcategoryList; // List of subcategories
    private final OnSubcategoryClickListener listener; // Listener for click events

    // Constructor
    public SubCategoryModuleAdapter(List<DataModule.Subcategory> subcategories, OnSubcategoryClickListener listener) {
        this.subcategoryList = subcategories;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataModule.Subcategory currentSubcategory = subcategoryList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            holder = new ViewHolder();

            // Inflate the layout
            convertView = inflater.inflate(R.layout.item_subcategory_module, parent, false);
            holder.titleTextView = convertView.findViewById(R.id.subcategory_title);
            holder.descriptionTextView = convertView.findViewById(R.id.description);
            holder.iconImageView = convertView.findViewById(R.id.ic_video);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind data to views
        holder.titleTextView.setText(currentSubcategory.getTitle());
        holder.descriptionTextView.setText(currentSubcategory.getDescription());

        // Set click listener on the root view
        convertView.setOnClickListener(v -> listener.onSubcategoryClick(currentSubcategory));

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
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

    // Interface for click events
    public interface OnSubcategoryClickListener {
        void onSubcategoryClick(DataModule.Subcategory subcategory);
    }
}
