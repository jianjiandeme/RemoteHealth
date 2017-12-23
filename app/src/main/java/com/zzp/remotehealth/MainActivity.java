package com.zzp.remotehealth;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.io.File;

import Patient.Patient;

import static utils.Constants.patients;
import static utils.Constants.zzpFile;


/**
    * @author zzp
    * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    Button fragment, houTai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = findViewById(R.id.fragment);
        houTai = findViewById(R.id.txtFile);
        File file1=new File(zzpFile);
        if(!file1.exists()){
            file1.mkdir();
        }
        Patient patient = new Patient(90,18,42);
        patients.add(patient);


        startService(new Intent(this, MyService.class));
        fragment.setOnClickListener((v) -> {
            Intent intent = new Intent(this, InformationActivity.class);
            intent.putExtra("type",true);
            startActivity(intent);
        });
        houTai.setOnClickListener((v) -> {
            Intent intent = new Intent(this, InformationActivity.class);
            intent.putExtra("type",false);
            startActivity(intent);
        });
    }
}

