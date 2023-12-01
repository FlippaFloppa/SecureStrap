package com.francescomilione.gipapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    private Activity activity;
    private RadioGroup radio;
    private RadioButton polizia, contatto;
    private EditText phone;
    private Button salva, button2, button3, button;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    ImageView btnHome, btnSetting, btnRecord;
    Funzionalita2 funzionalita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        funzionalita = new Funzionalita2(this);

        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnSetting = (ImageView)findViewById(R.id.btnSetting);
        btnRecord = (ImageView)findViewById(R.id.btnRecord);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Setting.this, MainActivity.class);
                startActivity(sendIntent);
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Setting.this, Records.class);
                startActivity(sendIntent);
            }
        });

        activity = this;


        radio = (RadioGroup) activity.findViewById(R.id.radio);
        polizia = (RadioButton) activity.findViewById(R.id.polizia);
        contatto = (RadioButton) activity.findViewById(R.id.contatto);
        phone = (EditText)activity.findViewById(R.id.phone);
        salva = (Button) activity.findViewById((R.id.salva));

        sharedPref = sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
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
                funzionalita.call();
            }
        });

        button3 = (Button) activity.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funzionalita.sendSms();
            }
        });

        button = (Button) activity.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funzionalita.notifica("Test", activity);
            }
        });


    }




}