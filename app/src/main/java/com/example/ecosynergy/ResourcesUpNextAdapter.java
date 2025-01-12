package com.example.ecosynergy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResourcesUpNextAdapter extends RecyclerView.Adapter<ResourcesUpNextAdapter.ViewHolder> {

    private List<DataResource.Subcategory> subcategories;
    private OnSubcategoryClickListener clickListener;

    public ResourcesUpNextAdapter(List<DataResource.Subcategory> subcategories) {
        this.subcategories = subcategories != null ? subcategories : new ArrayList<>();
    }

    public void setOnSubcategoryClickListener(OnSubcategoryClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ResourcesUpNextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_up_next, parent, false);
        return new ResourcesUpNextAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourcesUpNextAdapter.ViewHolder holder, int position) {
        DataResource.Subcategory subcategory = subcategories.get(position);  // Use the current position

        holder.upNextTwo.setText(subcategory.getArticleTitle());  // Set the title
        holder.upNextTwo.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onSubcategoryClick(subcategory);  // Pass the clicked subcategory
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategories.size();  // Return the correct size of the list
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView upNextTwo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            upNextTwo = itemView.findViewById(R.id.UpNextTwo);
        }
    }

    public interface OnSubcategoryClickListener {
        void onSubcategoryClick(DataResource.Subcategory subcategory);
    }
}
