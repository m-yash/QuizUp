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

import com.example.quizup.databinding.ActivityStartBinding;

import com.example.quizup.R;
import com.example.quizup.helper.QuizPreferences;
import com.example.quizup.utils.ConfirmUtils;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public class StartActivity extends AppCompatActivity {

    private QuizPreferences quizPreferences;

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizPreferences = new QuizPreferences(this);

        // Set click listeners
        binding.entertainmentButton.setOnClickListener(v -> openQuiz("Entertainment"));
        binding.geographyButton.setOnClickListener(v -> openQuiz("Geography"));

        binding.resetButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResetDataActivity.class);
            startActivity(intent);
        });

        // click listener for the "Get Results" button
        binding.getResultsButton.setOnClickListener(v -> sendResultsToPhone());

        // Initialize statistics
        updateStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh statistics when the activity resumes
        updateStatistics();
    }

    // function to fetch and update statistics
    private void updateStatistics() {
        binding.correctCount.setText(String.valueOf(quizPreferences.getCorrect()));
        binding.incorrectCount.setText(String.valueOf(quizPreferences.getIncorrect()));
    }

    // function to open the quiz activity based upon category
    private void openQuiz(String category) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }

    // function to send results to the phone
    private void sendResultsToPhone() {
        // Create the DataMap with the current results
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/quiz_results");
        DataMap dataMap = dataMapRequest.getDataMap();
        dataMap.putInt("correct", quizPreferences.getCorrect());
        dataMap.putInt("incorrect", quizPreferences.getIncorrect());
        dataMap.putString("launch_request", "open_app");

        // Push the results to the wearable's data layer
        Wearable.getDataClient(this)
                .putDataItem(dataMapRequest.asPutDataRequest())
                .addOnSuccessListener(unused ->
                        ConfirmUtils.showSuccessMessage("Results sent to phone!", this))
                .addOnFailureListener(e ->
                        ConfirmUtils.showFailureMessage("Failed to send results", this));
    }
}