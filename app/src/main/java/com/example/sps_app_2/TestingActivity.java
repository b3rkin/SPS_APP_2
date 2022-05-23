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


import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;

public class TestingActivity extends AppCompatActivity {

    private Button testingButton;
    private TextView predictedCell;
    private WifiManager wifiManager;
    private Integer cellNumber = 15;

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
                Float[] prior = new Float[cellNumber]; // Prior of the model
                Float initBelieve = 1f / cellNumber.floatValue();
                Arrays.fill(prior, initBelieve);
                Float[] posterior = new Float[cellNumber];
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

                MacAddress = "d0_4d_c6_f2_49_01"; // Comment to use actual strongest MAC
                maxSignalStrength = -55;

                /**
                 * vanaf hier leest hij de pmf csv
                 **/
                String next[] = {};
                List<String[]> list = new ArrayList<String[]>();
                try {
                    CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("MACpmf/"+ MacAddress.replace(":", "_")+".csv")));
                    //in open();
                    for(;;) {
                        next = reader.readNext();
                        if(next != null) {
                            list.add(next);
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    Log.i("debug","Ex exception occured");
                    e.printStackTrace();
                }

                // Initial prior is the initial believe
                // Obtain
                for(int k = 0; k<cellNumber; k++){
                    posterior[k] = prior[k] * Float.parseFloat(list.get(k)[-maxSignalStrength]);
                }
                Log.i("debug","test 1");

                Integer maxAt = 0;
                for (int i = 0; i < posterior.length; i++) {
                    maxAt = posterior[i] > posterior[maxAt] ? i : maxAt;
                }
                String outputMessage = String.valueOf(maxAt+1);
                // The elements of the opened pmf can be fetched with list.get(row_index)[column_index]
                predictedCell.setText(outputMessage);// (row_index,column_index)

                // TODO: Need to implement getLocation algorithm that returns string with the result
                // String location = getLocation(MacAddress, maxSignalStrength);

                // predictedCell.setText(location);


            }
        });
    }
}

