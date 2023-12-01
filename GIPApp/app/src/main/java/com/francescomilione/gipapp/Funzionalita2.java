package com.francescomilione.gipapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class Funzionalita2{
    private Activity activity;
    private Notifica notifica;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public Funzionalita2(Activity activity) {
        this.activity = activity;
        notifica = new Notifica(activity);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = sharedPref.edit();

        String selezione = sharedPref.getString(activity.getString(R.string.selezione), "polizia");
        String telefono = sharedPref.getString(activity.getString(R.string.telefono), "+39");
    }

    public void call(){
        String selezione = sharedPref.getString(activity.getString(R.string.selezione), "polizia");
        String telefono = sharedPref.getString(activity.getString(R.string.telefono), "+39");


        if(selezione.compareTo("polizia") == 0)
            call("+0001");
        else
        if(telefono.compareTo("") != 0)
            call(telefono);
    }

    public void sendSms(){
        String telefono = sharedPref.getString(activity.getString(R.string.telefono), "+39");

        if(telefono.compareTo("") != 0)
            sendSms(telefono);
    }

    public void sendSms(String numero){
        GPSTracker gps = new GPSTracker(activity);
        Location gpsTrovato = gps.getLocation();

        /*
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+numero));
        smsIntent.putExtra("sms_body", gpsTrovato.getLatitude()+" "+gpsTrovato.getLongitude());
        activity.startActivity(smsIntent);
         */

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(numero, null, "Aiuto questa è la mia posizione: https://www.google.com/maps/place/"+gpsTrovato.getLatitude()+","+gpsTrovato.getLongitude(), null, null);
    }

    public void call(String numero) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));
        activity.startActivity(intent);
    }

    public void notifica(String messaggio){
        notifica.notifica("SERVIZIO ALLARME", messaggio);
    }
}
