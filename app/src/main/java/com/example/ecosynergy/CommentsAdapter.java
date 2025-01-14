package com.example.ecosynergy;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private FirebaseDataFetcher dataFetcher;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
        this.dataFetcher = new FirebaseDataFetcher();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentText.setText(comment.getCommentText());
        holder.commentTime.setText(comment.getTimestamp());
        holder.voteCount.setText(comment.getVoteCount() + " votes");

        holder.upvoteButton.setOnClickListener(v -> {
            comment.upvote();
            notifyItemChanged(position);
            dataFetcher.updateVoteCount(comment);  // Update vote count in Firebase
        });

        holder.replyButton.setOnClickListener(v -> {
            // Open a dialog for adding a reply
            showAddCommentDialog(holder.itemView.getContext(), "subcategory");
        });

        // Display replies
        holder.repliesContainer.removeAllViews();
        for (Comment reply : comment.getReplies()) {
            TextView replyView = new TextView(holder.itemView.getContext());
            replyView.setText(reply.getCommentText());
            replyView.setTextSize(14);
            replyView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primaryColor));
            replyView.setPadding(8, 4, 8, 4);
            holder.repliesContainer.addView(replyView);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView commentText, voteCount, commentTime;
        ImageButton upvoteButton;
        Button replyButton;
        LinearLayout repliesContainer;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            commentText = itemView.findViewById(R.id.comment_text);
            voteCount = itemView.findViewById(R.id.upvotes);
            commentTime = itemView.findViewById(R.id.comment_time);
            upvoteButton = itemView.findViewById(R.id.upvote_button);
            replyButton = itemView.findViewById(R.id.reply_button);
            repliesContainer = itemView.findViewById(R.id.replies_container);
        }
    }

    // Show dialog to add a new comment
    private void showAddCommentDialog(Context context, String subcategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Comment");

        final EditText input = new EditText(context);
        input.setHint("Write your comment...");
        builder.setView(input);

        builder.setPositiveButton("Post", (dialog, which) -> {
            String commentText = input.getText().toString().trim();
            if (!commentText.isEmpty()) {
                // Get the username from Firebase Authentication
                String username = getUserDisplayName();

                // Create a new Comment object with a drawable avatar
                Comment newComment = new Comment(
                        ContextCompat.getDrawable(context, R.drawable.ic_default_avatar), // Set default avatar drawable
                        username, // Use the actual username here
                        commentText,
                        0, // Vote count, or other field if needed
                        "Just now" // Timestamp or other logic for time
                );

                // Add the new comment to the list
                comments.add(newComment);
                notifyItemInserted(comments.size() - 1);

                // Call the storeComment method in DiscussionActivity
                if (context instanceof DiscussionActivity) {
                    ((DiscussionActivity) context).storeComment(subcategory, newComment);
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // Helper method to get the username from Firebase
    private String getUserDisplayName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // If the user is authenticated, use their display name or email
            return user.getDisplayName() != null ? user.getDisplayName() : user.getEmail();
        } else {
            // If no user is logged in, return "Anonymous"
            return "Anonymous";
        }
    }

}
