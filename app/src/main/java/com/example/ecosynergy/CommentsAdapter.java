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

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
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
        });

        holder.replyButton.setOnClickListener(v -> {
            // Open a dialog for adding a reply
            showReplyDialog(holder.itemView.getContext(), comment, position);
        });

        // Display replies
        holder.repliesContainer.removeAllViews(); // Clear previous replies
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

    private void showReplyDialog(Context context, Comment comment, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Reply to Comment");

        final EditText input = new EditText(context);
        input.setHint("Write your reply...");
        builder.setView(input);

        builder.setPositiveButton("Post", (dialog, which) -> {
            String replyText = input.getText().toString().trim();
            if (!replyText.isEmpty()) {
                // Create a new Comment object for the reply
                Comment reply = new Comment("default_avatar_url", "username", replyText, 0, "Just now");
                comment.addReply(reply); // Add the reply to the comment's replies list
                notifyItemChanged(position); // Refresh the comment to show the new reply
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
