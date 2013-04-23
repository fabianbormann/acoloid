package com.example.acoloid;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public AcceptThread(BluetoothAdapter adapter) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app’s UUID string, also used by the client code.
            tmp = adapter.listenUsingRfcommWithServiceRecord("acoloid", MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // if a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread).
                manageConnectedSocket(socket);
                try {
					mmServerSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                break;
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
		
	}

	/** Will cancel the listening socket, and cause the thread to finish. */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}