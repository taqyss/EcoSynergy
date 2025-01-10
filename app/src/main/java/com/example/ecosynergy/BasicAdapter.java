package com.example.ecosynergy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BasicAdapter extends BaseAdapter {

    private final List<String> categoryList;
    private OnCategorySelectedListener onCategorySelectedListener;
    // Constructor
    public BasicAdapter(List<String> categories) {
        super();
        this.categoryList = categories;
    }

    public BasicAdapter(List<String> categories, OnCategorySelectedListener listener) {
        this.categoryList = categories;
        this.onCategorySelectedListener = listener;
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
        String category = categoryList.get(position);
        TextView textView = convertView.findViewById(R.id.subcategory_title);
        textView.setText(category);

        return convertView;
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(String category);
    }
}
