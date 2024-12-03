package com.example.quizup.helper;

import android.content.Intent;
import android.util.Log;

import com.example.quizup.MainActivity;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class QuizWearListenerService extends WearableListenerService {
    private static final String LAUNCH_REQUEST_PATH = "/quiz_results";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (LAUNCH_REQUEST_PATH.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    int correct = dataMapItem.getDataMap().getInt("correct");
                    int incorrect = dataMapItem.getDataMap().getInt("incorrect");

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }
    }
}

//
//public class QuizWearListenerService extends WearableListenerService {
//    private static final String TAG = "QuizWearListenerService";
//    private static final String LAUNCH_REQUEST_PATH = "/quiz_results";
//
//    @Override
//    public void onDataChanged(DataEventBuffer dataEvents) {
//        for (DataEvent event : dataEvents) {
//            if (event.getType() == DataEvent.TYPE_CHANGED) {
//                String path = event.getDataItem().getUri().getPath();
//                if (LAUNCH_REQUEST_PATH.equals(path)) {
//                    Intent intent = new Intent(this, MainActivity.class); // Your main activity
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }
//        }
//    }
//
//
//}
