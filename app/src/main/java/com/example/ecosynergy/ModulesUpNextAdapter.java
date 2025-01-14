package com.example.ecosynergy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModulesUpNextAdapter extends RecyclerView.Adapter<ModulesUpNextAdapter.ViewHolder> {

    private List<DataModule.Subcategory> subcategories;
    private OnSubcategoryClickListener clickListener;

    public ModulesUpNextAdapter(List<DataModule.Subcategory> subcategories) {
        this.subcategories = subcategories != null ? subcategories : new ArrayList<>();
    }

    public void setOnSubcategoryClickListener(OnSubcategoryClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_up_next, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModule.Subcategory subcategory = subcategories.get(position);  // Use the current position

        // Bind data to the views
        holder.upNextTwo.setText(subcategory.getVideoTitle());
        holder.descriptionTextView.setText("Discussion");

        // Set click listener for the subcategory title
        holder.upNextTwo.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onSubcategoryClick(subcategory);
            }
        });

        // Set click listener for the discussion link
        holder.descriptionTextView.setOnClickListener(v -> {
            if (v.getContext() != null) {
                DiscussionActivity.openDiscussionActivity(v.getContext(), subcategory.getTitle(), CommentType.MODULE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView upNextTwo, descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            upNextTwo = itemView.findViewById(R.id.UpNextTwo);
            descriptionTextView = itemView.findViewById(R.id.DiscussionUpNext);
        }
    }

    // Interface for click listener
    public interface OnSubcategoryClickListener {
        void onSubcategoryClick(DataModule.Subcategory subcategory);
    } }
