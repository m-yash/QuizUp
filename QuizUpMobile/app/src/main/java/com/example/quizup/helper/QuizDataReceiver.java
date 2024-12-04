package com.example.quizup.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.quizup.MainActivity;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;

public class QuizDataReceiver extends BroadcastReceiver {
    private static final String TAG = "QuizDataReceiver";
    private static final String LAUNCH_REQUEST_PATH = "/quiz_results";

    @Override
    public void onReceive(Context context, Intent intent) {
        Wearable.getDataClient(context).addListener((DataClient.OnDataChangedListener) dataEvents -> {
            for (DataEvent event : dataEvents) {
                String path = event.getDataItem().getUri().getPath();
                if (LAUNCH_REQUEST_PATH.equals(path)) {
                    Intent launchIntent = new Intent(context, MainActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntent);
                }
            }
        });
    }
}
