package com.example.ecosynergy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);

        // Initialize views
        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);
        askQuestionInput = view.findViewById(R.id.ask_question_input);
        postButton = view.findViewById(R.id.post_button);

        // Initialize comments list and adapter

        comments = new ArrayList<>(DummyData.getDummyComments());
        commentsAdapter = new CommentsAdapter(comments);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentsAdapter);

        postButton.setOnClickListener(v -> {
            String newCommentText = askQuestionInput.getText().toString().trim();
            if (!newCommentText.isEmpty()) {
                Comment newComment = new Comment("@drawable/ic_profile_pic", "You", newCommentText, 0, "Just now");
                comments.add(newComment);  // Add the new comment to the list
                commentsAdapter.notifyItemInserted(comments.size() - 1);  // Notify adapter of new item
                askQuestionInput.setText(""); // Clear input field
            }
        });

        return view;
    }
}
