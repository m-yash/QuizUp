package com.example.quizup.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizup.R;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizup.helper.QuizPreferences;
import com.example.quizup.helper.TriviaApiService;
import com.example.quizup.helper.TriviaQuestion;
import com.example.quizup.utils.ConfirmUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public class QuizActivity extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private ProgressBar loadingIndicator;
    private QuizPreferences quizPreferences;

    private String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        optionsGroup = findViewById(R.id.optionsGroup);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        quizPreferences = new QuizPreferences(this);

        // Button to send results to the phone
        ImageButton getResultsButton = findViewById(R.id.getResultsButton);
        getResultsButton.setOnClickListener(v -> sendResultsToPhone());

        // Update the results immediately on the wearable
//        sendResultsToPhone();
        fetchQuestion();
    }

    private void fetchQuestion() {

        showLoading(true);

        String category = getIntent().getStringExtra("CATEGORY");
        if (category == null) category = "Entertainment"; // Default to Entertainment

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beta-trivia.bongobot.io/") // Ensure no extra spaces
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TriviaApiService apiService = retrofit.create(TriviaApiService.class);

        // Request questions for the "Entertainment" category
        // use Entertainment, Geography
        apiService.getQuestionsByCategory("cat", category).enqueue(new Callback<List<TriviaQuestion>>() {
            @Override
            public void onResponse(Call<List<TriviaQuestion>> call, Response<List<TriviaQuestion>> response) {

                showLoading(false);

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Display the first question from the response
                    displayQuestion(response.body().get(0));
                } else {
                    Toast.makeText(QuizActivity.this, "No questions found for this category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TriviaQuestion>> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Failed to fetch question: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayQuestion(TriviaQuestion question) {
        questionTextView.setVisibility(View.VISIBLE);
        optionsGroup.setVisibility(View.VISIBLE);

        questionTextView.setText(question.getQuestion());
        correctAnswer = question.getCorrectAnswer();

        optionsGroup.removeAllViews();
        List<String> options = question.getIncorrectAnswers();
        options.add(correctAnswer);
        Collections.shuffle(options);

        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);

            // Set text color to white
            radioButton.setTextColor(getResources().getColor(android.R.color.white));
            radioButton.setPadding(8, 8, 8, 8);
            optionsGroup.addView(radioButton);
        }
    }
    private void showLoading(boolean isLoading) {
        // Show or hide the progress bar
        loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);

        // Show or hide the question text and options group
        questionTextView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        optionsGroup.setVisibility(isLoading ? View.GONE : View.VISIBLE);

        // Show or hide the Submit button and the Get Results button
        Button submitButton = findViewById(R.id.submitButton);
        ImageButton getResultsButton = findViewById(R.id.getResultsButton);
        if (submitButton != null) {
            submitButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
        if (getResultsButton != null) {
            getResultsButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }



        public void submitAnswer(View view) {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        // Check if no radio button is selected
        if (selectedId == -1) {
            ConfirmUtils.showInfoMessage("Please select an answer", this);
//            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);

        // Ensure selectedButton is not null before calling getText()
        if (selectedButton != null) {
            String selectedAnswer = selectedButton.getText().toString();

            // Check if the answer is correct
            if (selectedAnswer.equals(correctAnswer)) {
                ConfirmUtils.showSuccessMessage("Correct Answer!", this);
                quizPreferences.incrementCorrect();
            } else {
                String message = correctAnswer;
                ConfirmUtils.showFailureMessage(message, this);
                quizPreferences.incrementIncorrect();
            }

            // Update the results immediately on the wearable
            sendResultsToPhoneWithoutConfirmation();
            // Fetch next question
            fetchQuestion();
        } else {
            // If somehow the selected button is null, show an error
            ConfirmUtils.showInfoMessage("Please select an answer", this);
        }
    }
    private void sendResultsToPhoneWithoutConfirmation() {
        // Create the DataMap with the current results
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/quiz_results");
        DataMap dataMap = dataMapRequest.getDataMap();
        dataMap.putInt("correct", quizPreferences.getCorrect());
        dataMap.putInt("incorrect", quizPreferences.getIncorrect());
        dataMap.putString("launch_request", "open_app");

        // Push the results to the wearable's data layer without showing confirmation activity
        Wearable.getDataClient(this)
                .putDataItem(dataMapRequest.asPutDataRequest())
                .addOnSuccessListener(unused -> {
                    
                })
                .addOnFailureListener(e -> {
                    
                });
    }

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

//    private void sendResultsToPhone() {
//        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/quiz_results");
//        DataMap dataMap = dataMapRequest.getDataMap();
//        dataMap.putInt("correct", quizPreferences.getCorrect());
//        dataMap.putInt("incorrect", quizPreferences.getIncorrect());
//        dataMap.putString("launch_request", "open_app");
//        Wearable.getDataClient(this).putDataItem(dataMapRequest.asPutDataRequest())
//                .addOnSuccessListener(unused ->
//                        ConfirmUtils.showSuccessMessage("Results sent to phone!", this))
//                .addOnFailureListener(e ->
//                        ConfirmUtils.showFailureMessage("Failed to send results", this));
//    }
}
