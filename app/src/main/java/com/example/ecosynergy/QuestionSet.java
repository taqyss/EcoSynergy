package com.example.ecosynergy;

import java.util.List;

public class QuestionSet {
    private List<String> questions;
    private String correctAnswer;
    private List<String> answers; // This list contains answer options

    // Constructor with correctAnswer
    public QuestionSet(List<String> questions, List<String> answers, String correctAnswer) {
        this.questions = questions;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    // Constructor without correctAnswer (for some cases)
    public QuestionSet(List<String> questions, List<String> answers) {
        this.questions = questions;
        this.answers = answers;
    }

    // Getter for the correct answer
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // Setter for the correct answer
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // Getter for the list of questions
    public List<String> getQuestions() {
        return questions;
    }

    // Setter for the list of questions
    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    // Getter for the list of answers (options)
    public List<String> getAnswers() {
        return answers;
    }

    // Setter for the list of answers (options)
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    // New method to get the answer options
    public List<String> getOptions() {
        return answers; // returning answer options (answers)
    }
}
