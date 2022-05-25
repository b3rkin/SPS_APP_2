package com.example.sps_app_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
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

import javax.crypto.Mac;

public class TestingActivity extends AppCompatActivity {

    private Button testingButton;
    private TextView predictedCell;
    private WifiManager wifiManager;
    // Pins to show after localization



    private Integer cellNumber = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

    }
}

