package com.example.quizup;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity implements DataClient.OnDataChangedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("correct") && intent.hasExtra("incorrect")) {
            int correct = intent.getIntExtra("correct", 0);
            int incorrect = intent.getIntExtra("incorrect", 0);

            TextView correctView = findViewById(R.id.correctTextView);
            TextView incorrectView = findViewById(R.id.incorrectTextView);

            correctView.setText("Correct Answers: " + correct);
            incorrectView.setText("Incorrect Answers: " + incorrect);
        }
        // Register data listener
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED && "/quiz_results".equals(event.getDataItem().getUri().getPath())) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                int correct = dataMapItem.getDataMap().getInt("correct");
                int incorrect = dataMapItem.getDataMap().getInt("incorrect");

                runOnUiThread(() -> {
                    TextView correctView = findViewById(R.id.correctTextView);
                    TextView incorrectView = findViewById(R.id.incorrectTextView);

                    correctView.setText("Correct Answers: " + correct);
                    incorrectView.setText("Incorrect Answers: " + incorrect);
                });
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Wearable.getDataClient(this).removeListener(this);
    }
}