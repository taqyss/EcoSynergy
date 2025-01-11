package com.example.ecosynergy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DidYouKnowAdapter extends RecyclerView.Adapter<DidYouKnowAdapter.ViewHolder> {
    private List<Article> articles;
    private Context context;

    public DidYouKnowAdapter(List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_did_you_know, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.snippet.setText(article.getSnippet());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, snippet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            snippet = itemView.findViewById(R.id.article_snippet);
        }
    }
}
