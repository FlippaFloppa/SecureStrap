package com.francescomilione.gipapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.IOException;
import java.io.InputStream;

public class ThreadSocketB implements Runnable {
    private Activity activity;
    private InputStream inSocket;

    private Switch contatto, polizia;
    private EditText phone;

    public ThreadSocketB(Activity activity, InputStream inSocket) {
        this.inSocket = inSocket;
        this.activity = activity;
        contatto = (Switch)activity.findViewById(R.id.contatto);
        polizia = (Switch)activity.findViewById(R.id.polizia);
        phone = (EditText)activity.findViewById(R.id.phone);
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
                    if(polizia.isChecked())
                        call("+0001");
                    if(contatto.isChecked())
                        call(phone.getText().toString());
                }else if(m[0].toUpperCase().compareTo("NOTIFICA") == 0){
                    notifica(m[0]);
                }else if(m[0].toUpperCase().compareTo("SMS") == 0) {
                    if(contatto.isChecked())
                        sendSms(phone.getText().toString());
                }else if(m[0].toUpperCase().compareTo("TUTTO") == 0) {
                    call(phone.getText().toString());
                    notifica(m[0]);
                    sendSms(phone.getText().toString());
                }


            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void sendSms(String numero){
        GPSTracker gps = new GPSTracker(activity);
        Location gpsTrovato = gps.getLocation();
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+numero));
        smsIntent.putExtra("sms_body", gpsTrovato.getLatitude()+" "+gpsTrovato.getLongitude());
        activity.startActivity(smsIntent);
    }

    public void call(String numero) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));
        activity.startActivity(intent);
    }

    public void notifica(String messaggio){
        Notifica n = new Notifica(activity);
        n.notifica("SERVIZIO ALLARME", messaggio);
    }


}
