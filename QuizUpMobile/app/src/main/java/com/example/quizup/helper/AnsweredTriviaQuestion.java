package com.example.quizup.helper;

public class AnsweredTriviaQuestion {
    private final String question;
    private final String selectedAnswer;
    private final String correctAnswer;
    private final boolean isCorrect;

    public AnsweredTriviaQuestion(String question, String selectedAnswer, String correctAnswer, boolean isCorrect) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    @Override
    public String toString() {
        return "Question: " + question + "\n" +
                "Your Answer: " + selectedAnswer + "\n" +
                "Correct Answer: " + correctAnswer + "\n" +
                "Result: " + (isCorrect ? "Correct" : "Incorrect");
    }
}

