package com.example.sps_app_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training2);

        //package com.example.sps_app_2;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Button cell1ButtonTest;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        cell1ButtonTest.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                sampleCount1 += 1;
//                cell1Sample.setText(String.format(String.valueOf(sampleCount1)) );
//
//                // Set wifi manager.
//                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                // Start a wifi scan.
//                wifiManager.startScan();
//                // Store results in a list.
//                List<ScanResult> scanResults = wifiManager.getScanResults();
//                // Write results to a label
//                List <String> wifi_desired = new ArrayList<>();
//
//                wifi_desired.add("\nRSSI,Level");
//                for (ScanResult scanResult : scanResults) {
//                    wifi_desired.add("\n" + scanResult.BSSID);
//                    wifi_desired.add(Integer.toString(scanResult.level));
//                }
//                //wifi_desired.add("End measurement");
//                String data = wifi_desired.toString();
//
//                Toast.makeText(getApplicationContext(),"omgezet naar string",Toast.LENGTH_SHORT).show();
//                FileOutputStream fos = null;
//
//                try{
//                    fos = openFileOutput(FILE_NAME1,MODE_APPEND);
//
//
//                    fos.write(data.getBytes());
//                    Toast.makeText(getApplicationContext(), "Saved to " + getFilesDir() + "/" + FILE_NAME1, Toast.LENGTH_LONG).show();
//                } catch (FileNotFoundException e){
//                    e.printStackTrace();
//                } catch (IOException e){
//                    e.printStackTrace();
//                } finally{
//                    if (fos != null){
//                        try{
//                            fos.close();
//                        } catch(IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            }
//        });
//    }
//}
    }
}