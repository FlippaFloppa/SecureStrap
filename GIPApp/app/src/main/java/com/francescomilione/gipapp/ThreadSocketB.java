package com.francescomilione.gipapp;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class ThreadSocketB implements Runnable {
    private Activity activity;
    private BluetoothSocket socket;

    public ThreadSocketB(Activity activity, BluetoothSocket socket) {
        this.socket = socket;
        this.activity = activity;
    }


    @Override
    public void run() {
        try{

            while (true){
                Log.d("Thread", "In attesa di richieste");
                String richiesta="";
                int byter;
                while ((byter = socket.getInputStream().read()) != -1) {
                    richiesta += byter;
                    Notifica n = new Notifica(activity);
                    n.notifica("Segnale di soccorso", richiesta);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
