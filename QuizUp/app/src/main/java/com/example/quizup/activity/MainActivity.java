package com.example.quizup.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizup.R;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizup.helper.QuizPreferences;
import com.example.quizup.helper.TriviaApiService;
import com.example.quizup.helper.TriviaQuestion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private QuizPreferences quizPreferences;

    private String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        optionsGroup = findViewById(R.id.optionsGroup);
        quizPreferences = new QuizPreferences(this);

        fetchQuestion();
    }

    private void fetchQuestion() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beta-trivia.bongobot.io/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TriviaApiService apiService = retrofit.create(TriviaApiService.class);
        apiService.getRandomQuestion().enqueue(new Callback<List<TriviaQuestion>>() {
            @Override
            public void onResponse(Call<List<TriviaQuestion>> call, Response<List<TriviaQuestion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayQuestion(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<TriviaQuestion>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch question", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayQuestion(TriviaQuestion question) {
        questionTextView.setText(question.getQuestion());
        correctAnswer = question.getCorrectAnswer();

        optionsGroup.removeAllViews();
        List<String> options = question.getIncorrectAnswers();
        options.add(correctAnswer);
        Collections.shuffle(options);

        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            optionsGroup.addView(radioButton);
        }
    }

    public void submitAnswer(View view) {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();

        if (selectedAnswer.equals(correctAnswer)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            quizPreferences.incrementCorrect();
        } else {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            quizPreferences.incrementIncorrect();
        }

        fetchQuestion(); // Load the next question
    }
}
