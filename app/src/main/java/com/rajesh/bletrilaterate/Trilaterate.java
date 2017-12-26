package com.rajesh.bletrilaterate;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Trilaterate extends AppCompatActivity {
    EditText x;
    EditText y;
    TextView mText;
    Button b,b1;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler = new Handler();
    public ScanCallback mScanCallback;
    HashMap<String,Integer> name_rssi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trilaterate);
        x = (EditText) findViewById(R.id.editText2);
        y = (EditText) findViewById(R.id.editText3);
        mText = (TextView) findViewById(R.id.textView3);
        b = (Button) findViewById(R.id.button3);
        b1 = (Button) findViewById(R.id.button4);
        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        name_rssi = new HashMap<>();

        if( !BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported() ) {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show();
            b.setEnabled( false );
            b1.setEnabled( false );

        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trilaterate(view);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               discover();
            }
        });

    }


    public void trilaterate(View view){
        Intent i = new Intent(Trilaterate.this,Final_trilaterate.class);
        Bundle b = new Bundle();
        b.putSerializable("hashmap",name_rssi);
        i.putExtras(b);
        startActivity(i);
    }

    private void discover() {


        ScanFilter filter = new ScanFilter.Builder()
                .setServiceUuid( new ParcelUuid(UUID.fromString( getString(R.string.ble_uuid ) ) ) )
                .build();
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add( filter );

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode( ScanSettings.SCAN_MODE_BALANCED )
                .build();

        mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Toast.makeText(getApplicationContext(),"onScanResult is called",Toast.LENGTH_SHORT).show();
                if( result == null
                        || result.getDevice() == null
                        || TextUtils.isEmpty(result.getDevice().getName()) ) {
                    mText.setText("");
                }

                else {
                    //mText.setText(mText.getText().toString()+"\n"+result.getDevice().getName()+":"+result.getRssi());
                    name_rssi.put(result.getDevice().getName(),result.getRssi());
                    Log.v("HashMap",name_rssi.toString());
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Toast.makeText(getApplicationContext(),"onBatchScanResult is called",Toast.LENGTH_SHORT).show();
                Iterator<ScanResult> ite = results.iterator();
                while(ite.hasNext()){
                    Toast.makeText(getApplicationContext(),ite.next().getDevice().getName()+" "+ite.next().getRssi()+"",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e( "BLE", "Discovery onScanFailed: " + errorCode );
                super.onScanFailed(errorCode);
            }
        };

        mBluetoothLeScanner.startScan(mScanCallback);
        Toast.makeText(this,"Scan has started",Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothLeScanner.stopScan(mScanCallback);
            }
        }, 5000);
    }


    private void advertise() {
        BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_BALANCED )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable(false)
                .build();

        ParcelUuid pUuid = new ParcelUuid( UUID.fromString( getString( R.string.ble_uuid ) ) );
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName( true )
                .addServiceData( pUuid, "hi".getBytes(Charset.forName("UTF-8") ) )
                .build();

        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Toast.makeText(getApplicationContext(),"CallBack called",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
                super.onStartFailure(errorCode);
            }
        };

        advertiser.startAdvertising( settings, data, advertisingCallback );
        Toast.makeText(this,"Advertisement started",Toast.LENGTH_SHORT).show();
    }

}

