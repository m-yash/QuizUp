package com.example.quizup.helper;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TriviaQuestion {
    @SerializedName("question")
    private String question;

    @SerializedName("correct_answer")
    private String correctAnswer;

    @SerializedName("incorrect_answers")
    private List<String> incorrectAnswers;

    // Getters
    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correctAnswer; }
    public List<String> getIncorrectAnswers() { return incorrectAnswers; }
}