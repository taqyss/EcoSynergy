package com.example.ecosynergy;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestYourKnowledgeAdapter extends RecyclerView.Adapter<TestYourKnowledgeAdapter.ViewHolder> {
    private List<Questions> questions = DummyData.getDummyQuestions();

    public TestYourKnowledgeAdapter(List<Questions> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_your_knowledge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Correct usage of 'position' to access the current item in the list
        Questions currentQuestion = questions.get(position);
        holder.title.setText(currentQuestion.getTitle());
        holder.question.setText(currentQuestion.getDescription());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, question;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quiz_title);
            question = itemView.findViewById(R.id.quiz_description);
        }
    }
}

