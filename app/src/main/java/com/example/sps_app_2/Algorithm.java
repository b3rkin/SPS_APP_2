package com.example.sps_app_2;


import java.io. * ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Scanner;

import com.google.android.gms.common.util.Strings;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStream;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

public class Algorithm{

    /**
     * To get the three strongest mac addresses
     * @param scanResults acquired wifi measurement data
     * @return the three strongest mac addresses
     */
    static Pair<String, Integer> strongest_mac_value(List <ScanResult> scanResults) {

        int maxSignalStrength = -100;
        Pair <String, Integer> maxSignal = Pair.create("test",3);
        for (ScanResult scanResult : scanResults) {

            int RSSI = scanResult.level;
            String macAddress = scanResult.BSSID;

            Pair <String, Integer> macAndSignal = Pair.create(macAddress,RSSI);

            if (RSSI < maxSignalStrength) {
                maxSignal = macAndSignal;

            }
        }
        return maxSignal;
    }

    static void calc_posterior(List<Strings> sortedTestPoint, List<Integer> prior) {
    }
}




