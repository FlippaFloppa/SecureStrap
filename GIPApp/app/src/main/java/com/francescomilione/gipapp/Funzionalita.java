package com.francescomilione.gipapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;



public class Funzionalita {
    private Activity activity;
    private Notifica notifica;

    private RadioGroup radio;
    private RadioButton polizia, contatto;
    private EditText phone;
    private Button salva, button2, button3;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public Funzionalita(Activity activity) {
        this.activity = activity;
        notifica = new Notifica(activity);

        radio = (RadioGroup) activity.findViewById(R.id.radio);
        polizia = (RadioButton) activity.findViewById(R.id.polizia);
        contatto = (RadioButton) activity.findViewById(R.id.contatto);
        phone = (EditText)activity.findViewById(R.id.phone);
        salva = (Button) activity.findViewById((R.id.salva));
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        String selezione = sharedPref.getString(activity.getString(R.string.selezione), "polizia");
        String telefono = sharedPref.getString(activity.getString(R.string.telefono), "+39");

        phone.setText(telefono);
        if(selezione.compareTo("contatto")==0)
            contatto.setChecked(true);
        else
            contatto.setChecked(false);

        if(selezione.compareTo("polizia")==0)
            polizia.setChecked(true);
        else
            polizia.setChecked(false);


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phone.getText().toString().compareTo("") != 0) {
                    editor.putString(activity.getString(R.string.telefono), phone.getText().toString());
                    editor.apply();
                }

                if(radio.getCheckedRadioButtonId() == R.id.polizia) {
                    editor.putString(activity.getString(R.string.selezione), "polizia");
                }else{
                    editor.putString(activity.getString(R.string.selezione), "contatto");
                }
                editor.apply();
            }
        });


        button2 = (Button) activity.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        button3 = (Button) activity.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms();
            }
        });
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
        manager.sendTextMessage(numero, null, "Aiuto questa è la mia posizione:https://www.google.com/maps/place/"+gpsTrovato.getLatitude()+","+gpsTrovato.getLongitude(), null, null);
    }

    public void call(String numero) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));
        activity.startActivity(intent);
    }

    public void notifica(String messaggio){
        notifica.notifica("SERVIZIO ALLARME", messaggio);
    }
}
