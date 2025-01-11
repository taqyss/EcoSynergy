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

    // Constructor
    public SubCategoryModuleAdapter(List<DataModule.Subcategory> subcategories) {
        this.subcategoryList = subcategories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataModule.Subcategory currentSubcategory = subcategoryList.get(position);
        Log.d("SubCategoryActivity", "Subcategory: " + currentSubcategory.getTitle() + ", " + currentSubcategory.getDescription());

        System.out.printf(currentSubcategory.toString());

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            holder = new ViewHolder();

            // Inflating the appropriate layout for subcategories
            convertView = inflater.inflate(R.layout.item_subcategory_module, parent, false);
            holder.titleTextView = convertView.findViewById(R.id.subcategory_title);
            holder.descriptionTextView = convertView.findViewById(R.id.description);
            holder.iconImageView = convertView.findViewById(R.id.ic_video);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the title and description from the subcategory object
        holder.titleTextView.setText(currentSubcategory.getTitle());
        holder.descriptionTextView.setText(currentSubcategory.getDescription());

        Log.d("SubCategoryAdapter", "Subcategory: " + currentSubcategory.getTitle());


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
}

