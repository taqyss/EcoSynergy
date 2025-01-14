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

public class SubCategoryModuleAdapter extends BaseAdapter {
    private boolean hasQuestionSets = true;
    private String category, level;

    private DataSnapshot dataSnapshot;
    private final List<DataModule.Subcategory> subcategoryList;
    private final OnSubcategoryClickListener listener;

    // Constructor
    public SubCategoryModuleAdapter(String category, String level, List<DataModule.Subcategory> subcategories, DataSnapshot dataSnapshot, boolean hasQuestionSets, OnSubcategoryClickListener listener) {
        this.subcategoryList = subcategories;
        this.listener = listener;
        this.category = category;
        this.level = level;
        this.dataSnapshot = dataSnapshot;
        this.hasQuestionSets = hasQuestionSets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataModule.Subcategory currentSubcategory = subcategoryList.get(position);

        if (currentSubcategory == null || level == null || category == null) {
            Log.e("SubCategoryAdapter", "Invalid state: subcategory, level, or category is null");
            return convertView == null ? new View(parent.getContext()) : convertView;
        }

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.item_subcategory_module, parent, false);
            holder.titleTextView = convertView.findViewById(R.id.subcategory_title);
            holder.descriptionTextView = convertView.findViewById(R.id.description);
            holder.iconImageView = convertView.findViewById(R.id.ic_video);
            holder.discussionTextView = convertView.findViewById(R.id.DiscussionUpNext);
            holder.questionTitleTextView = convertView.findViewById(R.id.LevelAndCategoryName);
            holder.recapTextView = convertView.findViewById(R.id.Recap);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(currentSubcategory.getTitle());
        holder.descriptionTextView.setText(currentSubcategory.getDescription());
        holder.questionTitleTextView.setText("Recap " + category);

        // Check if there are question sets
        if (hasQuestionSets) {
            convertView.findViewById(R.id.DiscussionUpNextRecap).setVisibility(View.GONE);
            convertView.findViewById(R.id.ic_discussion_recap).setVisibility(View.GONE);
            convertView.findViewById(R.id.question_subcat).setVisibility(View.VISIBLE);
            holder.questionTitleTextView.setOnClickListener(v -> {
                if (dataSnapshot != null && category != null && level != null) {
                    Log.d("SubcategoryAdapter", "Opening quiz with category: " + category + " and level: " + level);
                    QuizActivity.openQuizActivity(parent.getContext(), category, level, dataSnapshot);
                } else {
                    Log.e("SubcategoryAdapter", "Missing category, level, or dataSnapshot. Cannot open QuizActivity.");
                }
            });
        }
        else {
            convertView.findViewById(R.id.question_subcat).setVisibility(View.GONE);
        }

        // Set listener for discussion
        holder.discussionTextView.setOnClickListener(v ->
                DiscussionActivity.openDiscussionActivity(
                        parent.getContext(),
                        currentSubcategory.getTitle(),
                        CommentType.MODULE
                )
        );

        // Set click listener for the whole subcategory item
        convertView.setOnClickListener(v ->
                listener.onSubcategoryClick(currentSubcategory, false)
        );

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView, recapTextView;
        TextView questionTitleTextView;
        TextView descriptionTextView;
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
        void onSubcategoryClick(DataModule.Subcategory subcategory, boolean isQuiz);
    }
}
