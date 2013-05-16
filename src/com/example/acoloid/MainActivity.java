package com.example.acoloid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final int MESSAGE_READ = 1;
	protected static final byte[] HANDSHAKE = {42};
	protected static final byte[] CODE_WAITING_MODE = {99,98,97,99};
	
	private final Handler mHandler = new Handler(new Handler.Callback(){
	      @Override
	      public boolean handleMessage(Message msg) {
	    	  byte[] readMessage = (byte[]) msg.obj;
	          if (msg.arg1 > 0) {
	        	  countOfLEDs = readMessage[0];
	        	  fillComboboxWithItems();
	        	  return true;
	          }
			return false;
	      }
	});

	//variables for bluetooth connection
	BluetoothAdapter btAdapter;
	Set<BluetoothDevice> pairedDevices;
	ArrayList<String> btAdapterArray;
	ArrayList<BluetoothDevice> pbtDevices;
	ConnectedThread conThread;
	
	ColorPickerView colorPicker;
	byte[] sentBytes = new byte[4];
	int countOfLEDs = 0;
	Spinner spinner;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {         
       super.onCreate(savedInstanceState);  
       setContentView(R.layout.activity_main);
      
       initBluetooth();
       setUpColorPicker();
   }

	@Override
	public void onDestroy(){
		setArduinoInWaitingMode();
		
		if(conThread != null){
			conThread.cancel();
		}
		
		super.onDestroy();
	}
	
	private void setArduinoInWaitingMode(){
		if(conThread != null){
			conThread.write(CODE_WAITING_MODE);
		}
	}
	
	private void fillComboboxWithItems() {
		spinner = (Spinner) findViewById(R.id.spinner1);
		
		String[] spinnerElements= new String[countOfLEDs];
		for(int i = 0; i < countOfLEDs; i++){
			spinnerElements[i]="LED "+Integer.toString(i+1);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_dropdown_item, spinnerElements);
		spinner.setAdapter(adapter);	
	}
	
	private void setUpColorPicker() {
		colorPicker = (ColorPickerView)findViewById(R.id.colorPickerView1);
		colorPicker.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				if(conThread != null){
					sentBytes[0] = (byte) Color.red(color);
					sentBytes[1] = (byte) Color.green(color);
					sentBytes[2] = (byte) Color.blue(color);
					sentBytes[3] = (byte) (spinner.getSelectedItemPosition()+1);
		    		conThread.write(sentBytes);
		    	}
		    	else{
		    		Toast.makeText(getApplicationContext(), "The connection is not established yet", Toast.LENGTH_LONG).show();
		    	}
			}
		});	
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
		btAdapterArray = new ArrayList<String>();
		pbtDevices = new ArrayList<BluetoothDevice>();
		// if exists
		if (pairedDevices.size() > 0) {
		    // add to an array
		    for (BluetoothDevice device : pairedDevices) {
		        btAdapterArray.add(device.getName() + "\n" + device.getAddress());
		        pbtDevices.add(device);
		    }
		}
		
		Log.i("acoloid", btAdapterArray.get(0));
		
		//Connect to a remote Device
		ConnectThread cThread = new ConnectThread(pbtDevices.get(0));
		cThread.start();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		   if (resultCode == Activity.RESULT_OK) {
		      // Bluetooth is enabled.
		   } else {
			   Toast.makeText(getApplicationContext(), "Bluetooth must be enabled!", Toast.LENGTH_SHORT).show();
			   finish();
		   }
		}
	
	private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;

	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(uuid);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	    	Log.i("acoloid", "run...");
	        // Cancel discovery because it will slow down the connection
	        btAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	        	Log.i("acoloid", "Connection failed...");
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) {
	            	
	            }
	            return;
	        }
	 
	        conThread = new ConnectedThread(mmSocket);
	        conThread.sendHandshake();
	        conThread.run();
	    }
	}
	 
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void sendHandshake(){
	    	this.write(HANDSHAKE);
	    }
	    
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
	                Log.i("acoloid", "detected action: "+bytes);
	                // Send the obtained bytes to the UI activity
	                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	                        .sendToTarget();
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
}