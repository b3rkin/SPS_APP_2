package com.example.sps_app_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Declare the widget variables.
    private Button buttonTest;
    private Button buttonTrain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Declare the widget variables.
        // findViewById allows us to grab the button from vies.
        buttonTest = (Button) findViewById(R.id.buttonTesting);
        buttonTrain = (Button) findViewById(R.id.buttonTraining);


        buttonTrain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchToTrainingActivity();
            }
        });

        buttonTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchToTestingActivity();
            }
        });
    }

    private void switchToTrainingActivity() {
        Intent switchActivityIntent = new Intent(this, TrainingActivity.class);
        startActivity(switchActivityIntent);
    }
    private void switchToTestingActivity() {
        Intent switchActivityIntent = new Intent(this, TestingActivity.class);
        startActivity(switchActivityIntent);

    }
}