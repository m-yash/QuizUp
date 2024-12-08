package com.example.quizup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizup.R;
import com.example.quizup.helper.QuizPreferences;

public class StartActivity extends AppCompatActivity {

    private QuizPreferences quizPreferences;
    private TextView correctCount;
    private TextView incorrectCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        quizPreferences = new QuizPreferences(this);

        Button entertainmentButton = findViewById(R.id.entertainmentButton);
        Button geographyButton = findViewById(R.id.geographyButton);
        ImageButton resetButton = findViewById(R.id.resetButton);
        correctCount = findViewById(R.id.correctCount);
        incorrectCount = findViewById(R.id.incorrectCount);

        // Set click listeners
        entertainmentButton.setOnClickListener(v -> openQuiz("Entertainment"));
        geographyButton.setOnClickListener(v -> openQuiz("Geography"));

        resetButton.setOnClickListener(v -> {
//            quizPreferences.resetStatistics();
//            updateStatistics();
            Intent intent = new Intent(this, ResetDataActivity.class);
            startActivity(intent);
        });

        // Initialize statistics
        updateStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh statistics when the activity resumes
        updateStatistics();
    }

    private void updateStatistics() {
        correctCount.setText(String.valueOf(quizPreferences.getCorrect()));
        incorrectCount.setText(String.valueOf(quizPreferences.getIncorrect()));
    }

    private void openQuiz(String category) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}