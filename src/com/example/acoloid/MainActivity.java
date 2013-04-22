package com.example.acoloid;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

	BluetoothAdapter btAdapter;
	Set<BluetoothDevice> pairedDevices;
	ArrayList<String> btAdapterArray;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {         

       super.onCreate(savedInstanceState);    
       setContentView(R.layout.activity_main);
       initBluetooth();
   }

	private void initBluetooth() {
		// Check if bluetooth is available on this device
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
		    Toast.makeText(getApplicationContext(), "Nice try but bluetooth is not on your phone...", Toast.LENGTH_SHORT).show();
		    finish();
		}
		// Check if bluetooth is enabled
		if (!btAdapter.isEnabled()) {
		    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(intent, 1);
		}
		
		//Search for pairedDevices
		pairedDevices = btAdapter.getBondedDevices();
		// if exists
		if (pairedDevices.size() > 0) {
		    // add to an array
		    for (BluetoothDevice device : pairedDevices) {
		        btAdapterArray.add(device.getName() + "\n" + device.getAddress());
		    }
		}
		
		Log.i("acoloid", btAdapterArray.get(0));
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		   if (resultCode == Activity.RESULT_OK) {
		      // Bluetooth is enabled.
		   } else {
			   Toast.makeText(getApplicationContext(), "Bluetooth must be enabled!", Toast.LENGTH_SHORT).show();
			   finish();
		   }
		}
}