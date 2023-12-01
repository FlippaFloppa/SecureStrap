package com.francescomilione.gipapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Records extends AppCompatActivity {

    ImageView btnHome, btnSetting, btnRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnSetting = (ImageView)findViewById(R.id.btnSetting);
        btnRecord = (ImageView)findViewById(R.id.btnRecord);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Records.this, Setting.class);
                startActivity(sendIntent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Records.this, MainActivity.class);
                startActivity(sendIntent);
            }
        });

    }



}