package com.example.sps_app_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestingActivity extends AppCompatActivity {

    private Button testingButton;
    private TextView predictedCell;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        // Register the button and text box
        testingButton = (Button) findViewById(R.id.button);
        predictedCell = (TextView) findViewById(R.id.textView5);

        // Button click to collect testing data
        testingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Wifi manager
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            3

            }
        });



    }
}