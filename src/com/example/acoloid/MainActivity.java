package com.example.acoloid;

import java.io.OutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private BluetoothAdapter bAdapter = null;
	private BluetoothSocket bSocket = null;
	private OutputStream outColor = null;
	
	private static String bMAC = "00:00:00:00:00";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
