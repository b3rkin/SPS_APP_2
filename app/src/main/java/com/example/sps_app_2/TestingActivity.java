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
import android.widget.Toast;

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


        // Register the button, text box and pins for on the map
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

                // Calc the posterior through three measurements
                Float[] posterior = oneMeasurementAnalysis(initBelief,cellNumber);
                Float[] posterior1 = oneMeasurementAnalysis(posterior,cellNumber);
                Float[] posterior2 = oneMeasurementAnalysis(posterior1,cellNumber);

                int maxAt = 0;
                for (int i = 0; i < posterior.length; i++) {
                    maxAt = posterior[i] > posterior[maxAt] ? i : maxAt;
                }

                maxAt = 0;
                for (int i = 0; i < posterior2.length; i++) {
                    maxAt = posterior2[i] > posterior2[maxAt] ? i : maxAt;
                }

                String outputMessage = String.valueOf(maxAt + 1);//

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

        // Between two measurements must be an interval of a second
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start scan
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();

        Float[] posterior = new Float[cellNumber];
        Float[] posteriorCalc;

        // Order acquired wifi data in list of pairs
        List<Pair<String, Integer>> wifiDataPairs = list_of_pairs(scanResults);

        // Set it to router pairs
        List<Pair<String, Integer>> wifiRouterPairs = router_address(wifiDataPairs);

        // Number of router addresses
        //=====testing

//        List<Pair<String,Integer>> wifiRouterPairs = new ArrayList<>();
//        Pair<String,Integer> test1 = Pair.create("1c:28:af:61:f9:0", -57);
//        Pair<String,Integer> test2 = Pair.create("1c:28:af:61:f9:0", -58);
//        Pair<String,Integer> test3 = Pair.create("1c:28:af:61:f9:0", -59);
//        Pair<String,Integer> test4 = Pair.create("1c:28:af:61:f9:0", -60);
//
//        wifiRouterPairs.add(test1);
//        wifiRouterPairs.add(test2);
//        wifiRouterPairs.add(test3);
//        wifiRouterPairs.add(test4);

        //========

        int numRouters = wifiRouterPairs.size();

        boolean flag = true;
        int iterMAC = 0;
        int numUsedMAC = 12;

        while (flag) {

            flag = false;
            iterMAC++;

            // To prevent it is looking at too many routers
            if (iterMAC>numRouters){
                break;
            }
            // Get the best MAC
            Pair<String, Integer> strongestPair = wifiRouterPairs.get(0);

            // The elements of the opened pmf can be fetched with list.get(row_index)[column_index]

            List<String[]> pmf = read_csv(strongestPair.first);
            // Calc posterior

            posteriorCalc = calc_posterior(pmf, prior, strongestPair, cellNumber);

            // If posterior is zero stay in while loop and go to next mac
            float sumPosterior = 0;
            for (Float aFloat : posteriorCalc) {
                sumPosterior = sumPosterior + aFloat;
            }

            // To prevent that the posterior becomes zero through a false measurement it is checked whether the sum is zero.
            if (sumPosterior == 0) {
                flag = true;
                iterMAC--;
            } else{
                posterior = posteriorCalc;
            }
            if (numUsedMAC > iterMAC){
                flag = true;
            }

            // By removing the strongest signal the test data can be walked through in an ascending order.
            Log.i("debug",wifiRouterPairs.toString());
            wifiRouterPairs.remove(strongestPair);
        }
        return posterior;
    }
}
