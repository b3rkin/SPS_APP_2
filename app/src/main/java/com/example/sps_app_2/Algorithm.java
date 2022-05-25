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
import com.example.sps_app_2.TestingActivity.*;


public class Algorithm{

    /**
     * Create a list of mac and corresponding Rssi values
     * @param scanResults acquired wifi measurement data
     * @return ist of mac and corresponding Rssi values
     */
    static List<Pair<String, Integer>> list_of_pairs(List <ScanResult> scanResults) {

       List <Pair<String,Integer>> pairList = new ArrayList<>();

        for (ScanResult scanResult : scanResults) {

            int RSSI = scanResult.level;
            String macAddress = scanResult.BSSID;

            Pair <String, Integer> macAndSignal = Pair.create(macAddress,RSSI);
            pairList.add(macAndSignal);
            }
        return pairList;
    }

    /**
     *
     * @param wifiData
     * @return
     */
    static Pair<String, Integer> strongest_signal(List <Pair<String,Integer>> wifiData){

        int length = wifiData.size();
        int maxSignalStrength = -100;
        Pair <String,Integer> bestSignal = Pair.create("empty",0);

        for (int i = 0; i<length; i++){
            Pair<String, Integer> signalPair = wifiData.get(i);
            int Rssi  = signalPair.second;

            if (Rssi > maxSignalStrength){
                maxSignalStrength = Rssi;
                bestSignal = signalPair;
            }
        }
        wifiData.remove(bestSignal);
        return bestSignal;
    }


    static Float[] calc_posterior(List<String[]> pmf, Float[] prior, Pair<String, Integer> strongestPair, int cellNumber) {

        Float[] posterior = new Float[cellNumber];

        // Get the strongest Wifi and Mac combination
        Log.i("debug","measurementmac " + strongestPair.first);
        int strongestSignal = strongestPair.second;
//        int strongestSignal = -86;
        Log.i("debug", "strongest" + Integer.toString(strongestSignal));

        // Calculate the posterior
        for (int k = 0; k < cellNumber; k++) {
            posterior[k] = prior[k] * Float.parseFloat(pmf.get(k)[-strongestSignal]);
        }
        // sum the posterior to normalize it
        float sumPosterior = 0;
        for (Float aFloat : posterior) {
            sumPosterior = sumPosterior + aFloat;
        }

        // Check whether the sum is zero, if not then normalize the posterior
        if (sumPosterior == 0) {
            return posterior;
        } else {
            for (int j = 0; j < posterior.length; j++) {
                posterior[j] = posterior[j] / sumPosterior;
            }
        }
        return posterior;
    }
}




