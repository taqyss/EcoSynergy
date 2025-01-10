package com.example.ecosynergy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdvancedAdapter extends BaseAdapter {

    private NavigationUtils.OnCategorySelectedListener listener;
    private final List<String> categoryList;
    // Constructor
    public AdvancedAdapter(List<String> items) {
        this.categoryList = items;
    }

    public AdvancedAdapter(List<String> items, NavigationUtils.OnCategorySelectedListener listener) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory, parent, false);
        }

        // Set data to views
        TextView textView = convertView.findViewById(R.id.subcategory_title);
        textView.setText(categoryList.get(position));

        return convertView;
    }
}
