package com.francescomilione.gipapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.io.IOException;
import java.io.InputStream;

public class ThreadSocketB implements Runnable {
    private Activity activity;
    private InputStream inSocket;
    private Funzionalita f;


    public ThreadSocketB(Activity activity, InputStream inSocket, Funzionalita funzionalita) {
        this.inSocket = inSocket;
        this.activity = activity;
        this.f = funzionalita;
    }


    @Override
    public void run() {

        try{

            while (true){
                int size = 32;
                byte[] dataBuffer = new byte[size];
                inSocket.read(dataBuffer, 0, size);
                String message = new String(dataBuffer, "UTF-8");
                String[] m = message.split("#");
                Log.e("TAG", "Ricevo richiesta: " + m[0]);

                if(m[0].toUpperCase().compareTo("CHIAMA") == 0){
                    f.call();

                }else if(m[0].toUpperCase().compareTo("NOTIFICA") == 0){
                    f.notifica(m[0]);

                }else if(m[0].toUpperCase().compareTo("SMS") == 0) {
                    f.sendSms();

                }else if(m[0].toUpperCase().compareTo("TUTTO") == 0) {
                    f.call();
                    f.sendSms();
                    f.notifica(m[0]);
                }


            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }


}
