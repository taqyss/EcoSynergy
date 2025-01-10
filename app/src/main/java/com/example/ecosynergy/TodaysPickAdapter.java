package com.example.ecosynergy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodaysPickAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> data; // Use Object so it can hold Articles, Videos, and Questions

    // Define view types
    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_QUESTION = 2;

    public TodaysPickAdapter(List<Object> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the type of the data at the current position
        if (data.get(position) instanceof Article) {
            return TYPE_ARTICLE;
        } else if (data.get(position) instanceof Video) {
            return TYPE_VIDEO;
        } else if (data.get(position) instanceof Questions) {
            return TYPE_QUESTION;
        }
        return -1; // Return an invalid type if none matches
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ARTICLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_did_you_know, parent, false);
                return new ArticleViewHolder(view);
            case TYPE_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_this_out, parent, false);
                return new VideoViewHolder(view);
            case TYPE_QUESTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_your_knowledge, parent, false);
                return new QuestionViewHolder(view);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object currentItem = data.get(position);

        if (holder instanceof ArticleViewHolder) {
            Article article = (Article) currentItem;
            ((ArticleViewHolder) holder).title.setText(article.getTitle());
            ((ArticleViewHolder) holder).description.setText(article.getSnippet());

        } else if (holder instanceof VideoViewHolder) {
            Video video = (Video) currentItem;
            ((VideoViewHolder) holder).title.setText(video.getTitle());
//            ((VideoViewHolder) holder).thumbnail.setImageResource(video.getThumbnailUrl());

        } else if (holder instanceof QuestionViewHolder) {
            Questions question = (Questions) currentItem;
            ((QuestionViewHolder) holder).questionTitle.setText(question.getTitle());
            ((QuestionViewHolder) holder).question.setText(question.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // View holders for each type
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            description = itemView.findViewById(R.id.article_snippet);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumbnail;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);

//            Glide.with(holder.itemView.getContext())
//                    .load(video.getThumbnailUrl())  // Load the thumbnail URL
//                    .into(videoHolder.thumbnail);
        }
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTitle, question;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTitle = itemView.findViewById(R.id.quiz_title);
            question = itemView.findViewById(R.id.quiz_description);
        }
    }
}
