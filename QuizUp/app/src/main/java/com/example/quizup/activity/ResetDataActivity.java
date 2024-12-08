package com.example.quizup.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.wear.widget.CircularProgressLayout;

import com.example.quizup.R;
import com.example.quizup.helper.QuizPreferences;

public class ResetDataActivity extends AppCompatActivity implements View.OnClickListener, CircularProgressLayout.OnTimerFinishedListener {

    private CircularProgressLayout circularProgressLayout;
    private QuizPreferences quizPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_data);

        quizPreferences = new QuizPreferences(this);
        circularProgressLayout = findViewById(R.id.circularProgress);

        circularProgressLayout.setTotalTime(3000); // 3 seconds for reset timer
        circularProgressLayout.setOnClickListener(this);
        circularProgressLayout.setOnTimerFinishedListener(this);
        circularProgressLayout.startTimer();
    }

    @Override
    public void onTimerFinished(CircularProgressLayout layout) {
        if (layout == circularProgressLayout) {
            // Reset shared preferences
            quizPreferences.resetStatistics();
            finish(); // Close the activity after reset
        }
    }

    @Override
    public void onClick(View v) {
        if (v == circularProgressLayout && circularProgressLayout.isTimerRunning()) {
            circularProgressLayout.stopTimer();
            circularProgressLayout.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.light_red));
            finish(); // Cancel the reset and close the activity
        }
    }
}