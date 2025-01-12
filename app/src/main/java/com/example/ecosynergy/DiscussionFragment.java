package com.example.ecosynergy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiscussionFragment extends Fragment {

    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> comments;
    private EditText askQuestionInput;
    private Button postButton;
    private String category;
    private String subcategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);

        // Retrieve arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            category = arguments.getString("CATEGORY");  // Use instance variables
            subcategory = arguments.getString("SUBCATEGORY");  // Use instance variables
        }

        updateCategoryAndSubcategory(category, subcategory);
        // Initialize views
        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);
        askQuestionInput = view.findViewById(R.id.ask_question_input);
        postButton = view.findViewById(R.id.post_button);

        // Fetch existing comments tied to the subcategory
        if (category != null && subcategory != null) {
            comments = DataModule.getCommentsForSubcategory(category, subcategory);
        }

        // Ensure comments list is not null
        if (comments == null) {
            comments = new ArrayList<>();
        }

        // Initialize RecyclerView and adapter
        commentsAdapter = new CommentsAdapter(comments);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentsAdapter);

        // Post button click listener
        postButton.setOnClickListener(v -> {
            String newCommentText = askQuestionInput.getText().toString().trim();
            if (!newCommentText.isEmpty()) {
                Comment newComment = new Comment("R.drawable.ic_profile_pic", "You", newCommentText, 0, "Just now");
                comments.add(newComment);  // Add the new comment to the list
                commentsAdapter.notifyItemInserted(comments.size() - 1);  // Notify adapter of new item
                askQuestionInput.setText(""); // Clear input field
                DataModule.addCommentToSubcategory(category, subcategory, newComment);
                // Update DataModule with the new comment
            }
        });

        return view;
    }

    // Method to dynamically update category and subcategory
    public void updateCategoryAndSubcategory(String newCategory, String newSubcategory) {
        this.category = newCategory;
        this.subcategory = newSubcategory;
    }
}
