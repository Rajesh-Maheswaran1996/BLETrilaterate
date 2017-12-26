package com.rajesh.bletrilaterate;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    Button b1;
    Button b2;
    EditText e1;

    public BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.button);
        b2 = (Button)findViewById(R.id.button2);
        e1 = (EditText) findViewById(R.id.editText);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter = null;
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                String name = e1.getText().toString();
                bluetoothAdapter.setName(name);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Trilaterate.class);
                startActivity(i);
            }
        });


    }





}
