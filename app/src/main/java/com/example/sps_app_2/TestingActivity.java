package com.example.sps_app_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static com.example.sps_app_2.Algorithm.*;





import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

public class TestingActivity extends AppCompatActivity {

    private Button testingButton;
    private TextView predictedCell;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        // Register the button and text box
        testingButton = (Button) findViewById(R.id.buttonTestMeasure);
        predictedCell = (TextView) findViewById(R.id.textViewTestResult);

        // Button click to collect testing data
        testingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                predictedCell.setText("Localizing...");

                // Wifi manager
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                // Start scan
                wifiManager.startScan();

                List<ScanResult> scanResults = wifiManager.getScanResults();

                Integer maxSignalStrength = -100;
                String MacAddress = "";

                for (ScanResult scanResult : scanResults) {

                    Integer RSSI = scanResult.level;

                    if(RSSI < maxSignalStrength ){
                        maxSignalStrength = RSSI;
                        MacAddress = scanResult.BSSID;
                    }

                }
                // TODO: Need to implement getLocation algorithm that returns string with the result
                String location = getLocation(MacAddress, maxSignalStrength);

                predictedCell.setText(location);

            }
        });



    }
}