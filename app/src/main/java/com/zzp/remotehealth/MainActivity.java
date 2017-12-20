package com.zzp.remotehealth;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


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
        houTai = findViewById(R.id.houtai);
        startService(new Intent(this, MyService.class));
        fragment.setOnClickListener((v) -> {
            startActivity(new Intent(this, InformationActivity.class));
        });
        houTai.setOnClickListener((v) -> {
            startActivity(new Intent(this, InformationActivity.class));
        });


    }
}

