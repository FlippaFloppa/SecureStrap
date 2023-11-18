package com.francescomilione.gipapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread implements Runnable {
    private final BluetoothServerSocket mmServerSocket;
    private Activity activity;

    public AcceptThread(BluetoothAdapter bluetoothAdapter, Activity activity) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        this.activity = activity;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("GIPApp", UUID.randomUUID());
            Log.e("LOGAPPFRA", "Socket avviata");
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;

        while (true) {
            Log.e("LOGAPPFRA", "ATTENDO...");
            Log.e("LOGAPPFRA", String.valueOf(mmServerSocket.getPsm()));
            try {
                Log.e("LOGAPPFRA", "Provo connessione");
                socket = mmServerSocket.accept();
                Log.e("LOGAPPFRA", "Connessione avviata");
            } catch (IOException e) {
                Log.e("LOGAPPFRA", "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {

                Thread t = new Thread(new ThreadSocketB(activity, socket));
                t.start();
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}