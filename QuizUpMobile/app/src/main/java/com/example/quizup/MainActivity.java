package com.example.quizup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import com.example.quizup.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements DataClient.OnDataChangedListener{

    private static final String TAG = "MainActivity";
    private static final String PATH_PREFIX = "/quiz_results";

    private TextView correctView, incorrectView;

    // ViewBinding object for accessing views
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // Set the root view of the binding

        // Register data listener
        Wearable.getDataClient(this).addListener(this);

        // Fetch the latest data immediately on app launch
        fetchLatestQuizResults();

//        // Retrieve and display data passed from the WearableListenerService (if available)
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("correct") && intent.hasExtra("incorrect")) {
//            int correct = intent.getIntExtra("correct", 0);
//            int incorrect = intent.getIntExtra("incorrect", 0);
//
//            TextView correctView = findViewById(R.id.correctTextView);
//            TextView incorrectView = findViewById(R.id.incorrectTextView);
//
//            correctView.setText("Correct Answers: " + correct);
//            incorrectView.setText("Incorrect Answers: " + incorrect);
//        }

    }


    // Fetch the latest data from Wearable
    private void fetchLatestQuizResults() {
        // Query the Wearable API for data changes
        Wearable.getDataClient(this).getDataItems().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Process the data items received
                for (DataItem dataItem : task.getResult()) {
                    // Check if the data item's path matches the expected path
                    if (dataItem.getUri().getPath().startsWith(PATH_PREFIX)) {
                        DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                        int correct = dataMap.getInt("correct", 0);
                        int incorrect = dataMap.getInt("incorrect", 0);


                        // Update the UI with the data received from Wearable
                        binding.correctTextView.setText("Correct: " + correct);
                        binding.incorrectTextView.setText("Incorrect: " + incorrect);
                    }
                }
            } else {
                Log.e(TAG, "Failed to fetch data items");
            }
        });
    }

    // Handle data changes from Wearable
    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            // Check if the event is a change and if the path matches the expected path
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().startsWith(PATH_PREFIX)) {
                // Process the data when changes are detected
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                int correct = dataMapItem.getDataMap().getInt("correct");
                int incorrect = dataMapItem.getDataMap().getInt("incorrect");

                runOnUiThread(() -> {
                    // Update the UI on the main thread
                    binding.correctTextView.setText("Correct: " + correct);
                    binding.incorrectTextView.setText("Incorrect: " + incorrect);
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the listener when the activity is destroyed
        Wearable.getDataClient(this).removeListener(this);
    }


//    @Override
//    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
//        for (DataEvent event : dataEvents) {
//            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().startsWith(PATH_PREFIX)) {
//                // Log the received data for debugging
//                Log.d(TAG, "Data received: " + event.getDataItem().getUri());
//
//                // Process the data and update UI
//                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
//                int correct = dataMapItem.getDataMap().getInt("correct");
//                int incorrect = dataMapItem.getDataMap().getInt("incorrect");
//
//                runOnUiThread(() -> {
//                    TextView correctView = findViewById(R.id.correctTextView);
//                    TextView incorrectView = findViewById(R.id.incorrectTextView);
//
//                    correctView.setText("Correct Answers: " + correct);
//                    incorrectView.setText("Incorrect Answers: " + incorrect);
//                });
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Unregister the listener when the activity is destroyed
//        Wearable.getDataClient(this).removeListener(this);
//    }

//    @Override
//    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
//        for (DataEvent event : dataEvents) {
//            if (event.getType() == DataEvent.TYPE_CHANGED && "/quiz_results".equals(event.getDataItem().getUri().getPath())) {
//                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
//                int correct = dataMapItem.getDataMap().getInt("correct");
//                int incorrect = dataMapItem.getDataMap().getInt("incorrect");
//
//                runOnUiThread(() -> {
//                    TextView correctView = findViewById(R.id.correctTextView);
//                    TextView incorrectView = findViewById(R.id.incorrectTextView);
//
//                    correctView.setText("Correct Answers: " + correct);
//                    incorrectView.setText("Incorrect Answers: " + incorrect);
//                });
//            }
//        }
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Wearable.getDataClient(this).removeListener(this);
//    }
}