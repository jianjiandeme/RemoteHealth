package com.zzp.remotehealth;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;

import Patient.Patient;

import static utils.Constants.patients;
import static utils.Constants.zzpFile;


/**
    * @author zzp
    * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    LinearLayout fragment, houTai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fragment = findViewById(R.id.fragment);
        houTai = findViewById(R.id.txtFile);
        File file1=new File(zzpFile);
        if(!file1.exists()){
            file1.mkdir();
        }

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

