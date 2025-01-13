package com.example.ecosynergy;

import java.util.List;

public class QuestionSet {
    private List<String> questions;

    private String correctAnswer;
    private List<String> answers; // Could be extended to store correct answers or options

    // Constructor
    public QuestionSet(List<String> questions, List<String> answers, String correctAsnwer) {
        this.questions = questions;
        this.answers = answers;
        this.correctAnswer = correctAsnwer;
    }

    public QuestionSet(List<String> questions, List<String> answers) {
        this.questions = questions;
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}

