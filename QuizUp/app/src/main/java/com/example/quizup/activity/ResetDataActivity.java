package com.example.quizup.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.wear.widget.CircularProgressLayout;

import com.example.quizup.R;
import com.example.quizup.helper.QuizPreferences;

import com.example.quizup.databinding.ActivityResetDataBinding;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public class ResetDataActivity extends AppCompatActivity implements View.OnClickListener, CircularProgressLayout.OnTimerFinishedListener {

    private ActivityResetDataBinding binding;

    private CircularProgressLayout circularProgressLayout;
    private QuizPreferences quizPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityResetDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizPreferences = new QuizPreferences(this);

        // Configure the circular progress layout
        binding.circularProgress.setTotalTime(3000); // 3 seconds for reset timer
        binding.circularProgress.setOnClickListener(this);
        binding.circularProgress.setOnTimerFinishedListener(this);
        binding.circularProgress.startTimer();
    }

    @Override
    public void onTimerFinished(CircularProgressLayout layout) {
        if (layout == binding.circularProgress) {
            // Reset shared preferences
            quizPreferences.resetStatistics();

            // Send reset data to the mobile app
            sendResetResultsToPhone();

            finish(); // Close the activity after reset
        }
    }

    private void sendResetResultsToPhone() {
        // Create the DataMap with the reset results
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/quiz_results");
        DataMap dataMap = dataMapRequest.getDataMap();
        dataMap.putInt("correct", 0);
        dataMap.putInt("incorrect", 0);
        dataMap.putString("reset_request", "reset_data");

        // Push the reset results to the wearable's data layer without showing confirmation activity
        Wearable.getDataClient(this)
                .putDataItem(dataMapRequest.asPutDataRequest())
                .addOnSuccessListener(unused -> {

                })
                .addOnFailureListener(e -> {

                });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.circularProgress && binding.circularProgress.isTimerRunning()) {
            binding.circularProgress.stopTimer();
            binding.circularProgress.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.light_red));
            finish(); // Cancel the reset and close the activity
        }
    }
}