package com.example.sps_app_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

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
        testingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Wifi manager
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                // Create initial belief for the first measurement
                Float[] initBelief = new Float[cellNumber];
                Float initBeliefValue = 1f / cellNumber.floatValue();
                Arrays.fill(initBelief, initBeliefValue);

                // Initialize the posterior
                Float[] posterior = new Float[cellNumber];
                Log.i("debug", "#1");

                posterior = oneMeasurementAnalysis(initBelief,cellNumber);
                Float[] posterior1 = oneMeasurementAnalysis(posterior,cellNumber);
                Float[] posterior2 = oneMeasurementAnalysis(posterior1,cellNumber);

                int maxAt = 0;
                for (int i = 0; i < posterior2.length; i++) {
                    maxAt = posterior2[i] > posterior2[maxAt] ? i : maxAt;
                }
                String outputMessage = String.valueOf(maxAt + 1);

                predictedCell.setText(outputMessage);// (row_index,column_index)

            }

        //TODO: Need to implement getLocation algorithm that returns string with the result
        });
    }

    private List<String[]> read_csv(String MacAddress) {

        String next[] = {};
        List<String[]> list = new ArrayList<String[]>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("MACpmf/" + MacAddress.replace(":", "_") + ".csv")));
            //in open();
            for (; ; ) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            Log.i("debug", "Ex exception occured");
            e.printStackTrace();
        }
        return list;
    }
    private Float[] oneMeasurementAnalysis(Float[] prior,int cellNumber){

        // Each measurement must have an interval of a second
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start scan
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        Log.i("debug", "#2");

        Float[] posterior = new Float[cellNumber];
        // Order acquired wifi data in list of pairs
        List<Pair<String, Integer>> wifiDataPairs = list_of_pairs(scanResults);

        boolean flag = true;
        int iter = 0;
        Log.i("debug", "#3");

        while (flag == true) {

            flag = false;

            // Get the best MAC
            Pair<String, Integer> strongestPair = wifiDataPairs.get(iter);
            iter = iter + 1;
            Log.i("debug", "measurementmac " + strongestPair.first);

            // The elements of the opened pmf can be fetched with list.get(row_index)[column_index]
            Log.i("debug", "#10");

            List<String[]> pmf = read_csv(strongestPair.first);

            // Calc posterior
            posterior = calc_posterior(pmf, prior, strongestPair, cellNumber);
            Log.i("debug", "#10111111");

            // If posterior is zero stay in while loop and go to next mac
            float sumPosterior = 0;
            for (Float aFloat : posterior) {
                sumPosterior = sumPosterior + aFloat;
            }
            if (sumPosterior == 0) {
                flag = true;
            }
            Log.i("debug", "#5");
        }
        return posterior;
    }
}
