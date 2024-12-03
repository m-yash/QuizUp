package com.example.quizup.utils;

import android.content.Context;
import android.content.Intent;

import androidx.wear.activity.ConfirmationActivity;

public class ConfirmUtils {
//    public static void showSavedMessage(String message, Context context){
//        Intent intent = new Intent(context, ConfirmationActivity.class);
//        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
//        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
//        context.startActivity(intent);
//
//    }
    public static void showMessage(String message, int animationType, Context context) {
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animationType);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }

    public static void showSuccessMessage(String message, Context context) {
        showMessage(message, ConfirmationActivity.SUCCESS_ANIMATION, context);
    }

    public static void showFailureMessage(String message, Context context) {
        showMessage(message, ConfirmationActivity.FAILURE_ANIMATION, context);
    }

    public static void showInfoMessage(String message, Context context) {
        showMessage(message, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION, context);
    }
}
