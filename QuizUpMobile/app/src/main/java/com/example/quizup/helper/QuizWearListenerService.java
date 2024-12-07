package com.example.quizup.helper;

import android.content.Intent;
import android.util.Log;

import com.example.quizup.MainActivity;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

public class QuizWearListenerService extends WearableListenerService {
    private static final String TAG = "QuizWearListenerService";
    private static final String DATA_PATH = "/quiz_results"; 
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            // Check if the event is a change and if the path matches the expected path
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals(DATA_PATH)) {

                Log.d(TAG, "Data received: " + DATA_PATH);

                // Launch the phone app when data is received
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
//public class QuizWearListenerService extends WearableListenerService {
//    private static final String TAG = "QuizWearListenerService";
//    private static final String DATA_PATH = "/quiz_results"; // Ensure this matches the Wear app's path
//
//    @Override
//    public void onDataChanged(DataEventBuffer dataEvents) {
//        for (DataEvent event : dataEvents) {
//            if (event.getType() == DataEvent.TYPE_CHANGED &&
//                    event.getDataItem().getUri().getPath().equals(DATA_PATH)) {
//
//                Log.d(TAG, "Data received: " + DATA_PATH);
//
//                // Launch the phone app when data is received
//                Intent intent = new Intent(this, MainActivity.class); // Replace with your activity
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        }
//    }
//}
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
//}
