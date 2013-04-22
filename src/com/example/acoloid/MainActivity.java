package com.example.acoloid;

import java.io.IOException;
import java.io.OutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private BluetoothAdapter bAdapter = null;
	private OutputStream outColor = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        bAdapter = BluetoothAdapter.getDefaultAdapter();

        byte[] cBuffer = new byte[3];
        cBuffer[0] = (byte) 255;
        cBuffer[1] = (byte) 0;
        cBuffer[2] = (byte) 255;
        
        try{
          outColor.write(cBuffer);
        }
        catch(IOException e){   
            Log.d("bluetooth", "An error occurred, please try again..");  
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
