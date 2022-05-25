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
import android.widget.ImageView;
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






public class TestingActivity extends AppCompatActivity {

    private Button testingButton;
    private TextView predictedCell;
    private WifiManager wifiManager;
    private Integer cellNumber = 15;

    private ImageView pinC1;
    private ImageView pinC2;
    private ImageView pinC3;
    private ImageView pinC4;
    private ImageView pinC5;
    private ImageView pinC6;
    private ImageView pinC7;
    private ImageView pinC8;
    private ImageView pinC9;
    private ImageView pinC10;
    private ImageView pinC11;
    private ImageView pinC12;
    private ImageView pinC13;
    private ImageView pinC14;
    private ImageView pinC15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);


        // Register the button and text box
        testingButton = (Button) findViewById(R.id.buttonTestMeasure);
        predictedCell = (TextView) findViewById(R.id.textViewTestResult);
        pinC1 = (ImageView)findViewById(R.id.imageViewPinC1);
        pinC2 = (ImageView)findViewById(R.id.imageViewPinC2);
        pinC3 = (ImageView)findViewById(R.id.imageViewPinC3);
        pinC4 = (ImageView)findViewById(R.id.imageViewPinC4);
        pinC5 = (ImageView)findViewById(R.id.imageViewPinC5);
        pinC6 = (ImageView)findViewById(R.id.imageViewPinC6);
        pinC7 = (ImageView)findViewById(R.id.imageViewPinC7);
        pinC8 = (ImageView)findViewById(R.id.imageViewPinC8);
        pinC9 = (ImageView)findViewById(R.id.imageViewPinC9);
        pinC10 = (ImageView)findViewById(R.id.imageViewPinC10);
        pinC11 = (ImageView)findViewById(R.id.imageViewPinC11);
        pinC12 = (ImageView)findViewById(R.id.imageViewPinC12);
        pinC13 = (ImageView)findViewById(R.id.imageViewPinC13);
        pinC14 = (ImageView)findViewById(R.id.imageViewPinC14);
        pinC15 = (ImageView)findViewById(R.id.imageViewPinC15);

        // Button click to collect testing data
        testingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pinC1.setVisibility(View.GONE);
                pinC2.setVisibility(View.GONE);
                pinC3.setVisibility(View.GONE);
                pinC4.setVisibility(View.GONE);
                pinC5.setVisibility(View.GONE);
                pinC6.setVisibility(View.GONE);
                pinC7.setVisibility(View.GONE);
                pinC8.setVisibility(View.GONE);
                pinC9.setVisibility(View.GONE);
                pinC10.setVisibility(View.GONE);
                pinC11.setVisibility(View.GONE);
                pinC12.setVisibility(View.GONE);
                pinC13.setVisibility(View.GONE);
                pinC14.setVisibility(View.GONE);
                pinC15.setVisibility(View.GONE);


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
                int maxAt = 0;
                for (int i = 0; i < posterior.length; i++) {
                    maxAt = posterior[i] > posterior[maxAt] ? i : maxAt;
                }
                String outputMessage = String.valueOf(maxAt + 1);

                Float[] posterior1 = oneMeasurementAnalysis(posterior,cellNumber);
                maxAt = 0;
                for (int i = 0; i < posterior1.length; i++) {
                    maxAt = posterior1[i] > posterior1[maxAt] ? i : maxAt;
                }
                outputMessage += String.valueOf(maxAt + 1);

                Float[] posterior2 = oneMeasurementAnalysis(posterior1,cellNumber);
                maxAt = 0;
                for (int i = 0; i < posterior2.length; i++) {
                    maxAt = posterior2[i] > posterior2[maxAt] ? i : maxAt;
                }
                outputMessage += String.valueOf(maxAt + 1);

                Float[] posterior3 = oneMeasurementAnalysis(posterior2,cellNumber);
                maxAt = 0;
                for (int i = 0; i < posterior3.length; i++) {
                    maxAt = posterior3[i] > posterior3[maxAt] ? i : maxAt;
                }
                outputMessage += String.valueOf(maxAt + 1);
                Float[] posteriorFinal = oneMeasurementAnalysis(posterior3,cellNumber);

                maxAt = 0;
                for (int i = 0; i < posteriorFinal.length; i++) {
                    maxAt = posteriorFinal[i] > posteriorFinal[maxAt] ? i : maxAt;
                }
                outputMessage += String.valueOf(maxAt + 1);

                predictedCell.setText(outputMessage);// (row_index,column_index)

                switch(maxAt+1) {
                    case 1:
                        pinC1 .setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        pinC2 .setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        pinC3 .setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        pinC4 .setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        pinC5 .setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        pinC6 .setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        pinC7 .setVisibility(View.VISIBLE);
                        break;
                    case 8:
                        pinC8 .setVisibility(View.VISIBLE);
                        break;
                    case 9:
                        pinC9 .setVisibility(View.VISIBLE);
                        break;
                    case 10:
                        pinC10 .setVisibility(View.VISIBLE);
                        break;
                    case 11:
                        pinC11 .setVisibility(View.VISIBLE);
                        break;
                    case 12:
                        pinC12 .setVisibility(View.VISIBLE);
                        break;
                    case 13:
                        pinC13 .setVisibility(View.VISIBLE);
                        break;
                    case 14:
                        pinC14 .setVisibility(View.VISIBLE);
                        break;
                    case 15:
                        pinC15 .setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }


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
