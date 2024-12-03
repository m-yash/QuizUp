package com.example.quizup.helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TriviaApiService {
    @GET("/")
    Call<List<TriviaQuestion>> getRandomQuestion();
}
