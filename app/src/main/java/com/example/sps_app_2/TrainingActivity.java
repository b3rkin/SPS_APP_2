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
import android.widget.EditText;
import android.widget.Toast;
import com.example.sps_app_2.Algorithm.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrainingActivity extends AppCompatActivity {

    private Button dataButton;
    private WifiManager wifiManager;
    private EditText cell;
    private EditText day;
    private EditText direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Register the button and text boxes
        dataButton = (Button) findViewById(R.id.buttonTrain2);
        cell = (EditText) findViewById(R.id.editText);
        day = (EditText) findViewById(R.id.editText2);
        direction = (EditText) findViewById(R.id.editText3);

        // Button click to start the measurement
        dataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Wifi manager
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                // Get Cell, day and direction
                String input_cell = cell.getText().toString() + "_" + day.getText().toString() +"_" + direction.getText().toString();


                // HashMap to save all the data. Later saved
                HashMap<String,ArrayList<Integer>> DataHash = new HashMap<>();
                boolean testFlag = true;

                // Store data to test training model as using hashmap is inconvenient
                List <String> testWifiDesired = new ArrayList<>();

                // Timing variables
                long beginTime = currentTimeMillis();
                long endLoop = currentTimeMillis();
                int iter = 0;

                Toast.makeText(getApplicationContext(),"measurement started!",Toast.LENGTH_LONG).show();

                //Start of while loop
                while (endLoop-beginTime < 30000) {

                    // Start scan
                    wifiManager.startScan();

                    iter = iter + 1;

                    // Store results in a list.
                    List<ScanResult> scanResults = wifiManager.getScanResults();


                    for (ScanResult scanResult : scanResults) {

                        String MacAddress = scanResult.BSSID;
                        Integer RSSI = scanResult.level;

                        if (testFlag){
                            testWifiDesired.add("\n" + scanResult.BSSID);
                            testWifiDesired.add(Integer.toString(scanResult.level));
                            Log.i("debug","passed test flag #1");

                        }

                        // Check if we already measured the mac address
                        if (DataHash.containsKey(MacAddress)) {
                            ArrayList<Integer> NewList = DataHash.get(MacAddress);
                            assert NewList != null;
                            NewList.add(RSSI);
                            DataHash.put(MacAddress, NewList);
                        } else {
                            ArrayList<Integer> RSSIValues = new ArrayList<>();
                            RSSIValues.add(RSSI);
                            DataHash.put(MacAddress, RSSIValues);
                        }
                    }

                    if (testFlag){
                        Log.i("debug","passed test flag #2");

                        break;
                    }


                    // Each measurement takes one second
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("debug",Integer.toString(iter));

                    endLoop = currentTimeMillis();
                }

                if (testFlag){
                    String testData = testWifiDesired.toString();
                    save_file_test(testData,"testData" + input_cell + ".txt");
                    Log.i("debug","passed test flag #3");
                }

                // Save the received data
                save_file(DataHash,input_cell);
                }
        });
    }

    /**
     * Saving the retrieved data in a correct format
     * @param Data the RSSI values ordered per MAC address
     * @param aspects Useful aspects of the measurement
     */
    void save_file(HashMap<String,ArrayList<Integer>> Data, String aspects){

        String FILE_NAME = "saved_data_cell" + aspects + ".txt";

        String finalData = "";

        // Iterate through loop and put it in correct file
        for (HashMap.Entry<String, ArrayList<Integer>> set :
                Data.entrySet()) {
            finalData = finalData + set.getKey() + "," + set.getValue() + "\n";
        }

        FileOutputStream fos = null;

        try{
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(finalData.getBytes());
            Toast.makeText(getApplicationContext(), "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (fos != null){
                try{
                    fos.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    void save_file_test(String testData, String FILE_NAME){

        FileOutputStream fos = null;

        try{
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(testData.getBytes());
            Toast.makeText(getApplicationContext(), "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (fos != null){
                try{
                    fos.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}



