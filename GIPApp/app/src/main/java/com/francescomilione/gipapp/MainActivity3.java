package com.francescomilione.gipapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity3 extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private BluetoothAdapter mBluetoothAdapter;
    public UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    BluetoothSocket mmSocket = null;
    BluetoothDevice mmDevice = null;
    InputStream outStream;
    Button btnConnessione;
    boolean connesso;
    Activity activity;
    Funzionalita2 funzionalita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnConnessione = (Button)findViewById(R.id.btn_connessione);
        connesso = false;
        activity = this;

        funzionalita = new Funzionalita2(activity);

        btnConnessione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!connesso) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        btnConnessione.setText("Bluetooth non supportato");
                        btnConnessione.setClickable(false);
                        Log.d("DEBUG", "Bluetooth non supportato");
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Log.d("DEBUG", "Bluetooth non abilitato");
                        } else {
                            mmDevice = mBluetoothAdapter.getRemoteDevice("B8:27:EB:B9:47:83");
                            try {
                                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                            } catch (IOException e) {
                                Log.d("DEBUG", "Errore Collegamento con raspberry");
                            }
                            try {
                                mmSocket.connect();
                                outStream = mmSocket.getInputStream();
                                Thread a = new Thread(new ThreadSocketB(activity, outStream, funzionalita));
                                a.start();
                                btnConnessione.setText("Disconnetti Bracciale");
                                connesso = true;
                            } catch (IOException closeException) {
                                Log.d("DEBUG", "Errore Creazione socket");
                                Log.d("DEBUG", closeException.getMessage());
                                try {
                                    mmSocket.close();
                                } catch (IOException ceXC) {
                                    Log.d("DEBUG", "Errore Chiusura socket");
                                }
                                Log.d("DEBUG", "Errore");
                            }
                        }
                    }
                }else{
                    try {
                        mmSocket.close();
                    } catch (IOException ceXC) {
                        Log.d("DEBUG", "Errore Chiusura socket");
                    }
                    btnConnessione.setText("Connetti Bracciale");
                    connesso = false;
                }
            }
        });






    }


}