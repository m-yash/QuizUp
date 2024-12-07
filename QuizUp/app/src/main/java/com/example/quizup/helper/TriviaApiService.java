package com.example.quizup.helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//public interface TriviaApiService {
//    @GET("/")
//    Call<List<TriviaQuestion>> getRandomQuestion();
//}

public interface TriviaApiService {
    @GET("/")
    Call<List<TriviaQuestion>> getQuestionsByCategory(@Query("search") String search, @Query("category") String category);
}
