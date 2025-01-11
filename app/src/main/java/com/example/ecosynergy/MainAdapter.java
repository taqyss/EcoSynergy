package com.example.ecosynergy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MainAdapter extends BaseAdapter {

    private NavigationUtils.OnCategorySelectedListener listener;
    private final List<String> categoryList;
    // Constructor
    public MainAdapter(List<String> items) {
        this.categoryList = items;
    }

    public MainAdapter(List<String> items, NavigationUtils.OnCategorySelectedListener listener) {
        this.categoryList = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainAdapter.ViewHolder holder;

        if (convertView == null) {
            // Inflate the list item layout
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

            // Initialize the ViewHolder and store references to views
            holder = new MainAdapter.ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.item_title);

            // Attach the holder to the convertView
            convertView.setTag(holder);
        } else {
            // Reuse the ViewHolder
            holder = (MainAdapter.ViewHolder) convertView.getTag();
        }

        // Populate data for the current position
        holder.titleTextView.setText(categoryList.get(position)); // Example: set text
        // Optionally set an image resource
        // holder.iconImageView.setImageResource(R.drawable.some_image);

        return convertView;
    }

    // ViewHolder class to cache references to views
    private static class ViewHolder {
        TextView titleTextView;
    }

}

