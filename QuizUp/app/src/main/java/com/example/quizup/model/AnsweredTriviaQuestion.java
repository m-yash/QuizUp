package com.example.quizup.model;

import com.example.quizup.helper.TriviaQuestion;

import java.util.List;

public class AnsweredTriviaQuestion {
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;
    private boolean isCorrect;

    private String selectedAnswer;

    public AnsweredTriviaQuestion(TriviaQuestion triviaQuestion, boolean isCorrect, String selectedAnswer) {
        this.question = triviaQuestion.getQuestion();
        this.correctAnswer = triviaQuestion.getCorrectAnswer();
        this.incorrectAnswers = triviaQuestion.getIncorrectAnswers();
        this.isCorrect = isCorrect;
        this.selectedAnswer = selectedAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
