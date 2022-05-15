package com.example.sps_app_2;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TrainingActivity extends AppCompatActivity {

    private Button DataButton; // Now it is unused
    private WifiManager wifiManager;


    String FILE_NAME = "RSSIdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        DataButton = (Button) findViewById(R.id.buttonTrain2);

        DataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Wifi manager
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                String input_cell = "Cell1";

                Log.i("debug","#1");
                // HashMap to save all the data. Later saved
                HashMap<String,ArrayList<Integer>> DataHash = new HashMap<>();

                // Timing variables
                long beginTime = currentTimeMillis();
                long endLoop = currentTimeMillis();
                Log.i("debug","#2");

                //Start of while loop
                Log.i("debug","#3");
                wifiManager.startScan();
                Log.i("debug1","#4");

                // Store results in a list.
                List<ScanResult> scanResults = wifiManager.getScanResults();
                Log.i("debug", scanResults.toString());

                for (ScanResult scanResult : scanResults) {
                    Log.i("debug","#6");

                    String MacAddress = scanResult.BSSID;
                    Integer RSSI = scanResult.level;

                    if (DataHash.containsKey(MacAddress)) {
                        Log.i("debug","#7");

                        ArrayList<Integer> RSSIValues = new ArrayList<>();
                        RSSIValues.add(RSSI);
                        DataHash.put(MacAddress, RSSIValues);
                    } else {
                        Log.i("debug","#8");

                        ArrayList<Integer> NewList = DataHash.get(MacAddress);
                        assert NewList != null;
                        NewList.add(RSSI);
                        DataHash.put(MacAddress, NewList);
                    }
                }
                endLoop = currentTimeMillis();

//                Toast.makeText(getApplicationContext(),DataHash.toString(),Toast.LENGTH_LONG).show();
                Log.i("debug","#9");


            }
        });
    }
}



