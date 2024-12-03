package com.example.quizup.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class QuizPreferences {
    private static final String PREFS_NAME = "QuizPrefs";
    private static final String CORRECT_KEY = "correct";
    private static final String INCORRECT_KEY = "incorrect";

    private SharedPreferences sharedPreferences;

    public QuizPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void incrementCorrect() {
        int count = sharedPreferences.getInt(CORRECT_KEY, 0);
        sharedPreferences.edit().putInt(CORRECT_KEY, count + 1).apply();
    }

    public void incrementIncorrect() {
        int count = sharedPreferences.getInt(INCORRECT_KEY, 0);
        sharedPreferences.edit().putInt(INCORRECT_KEY, count + 1).apply();
    }

    public int getCorrect() {
        return sharedPreferences.getInt(CORRECT_KEY, 0);
    }

    public int getIncorrect() {
        return sharedPreferences.getInt(INCORRECT_KEY, 0);
    }
}