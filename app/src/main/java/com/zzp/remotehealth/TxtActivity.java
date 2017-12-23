package com.zzp.remotehealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static utils.Constants.patients;
import static utils.Constants.zzpFile;


public class TxtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt);

        LinearLayout layout = findViewById(R.id.layout);
        for( int i = 0 ;i <patients.size();i++){
            Button but = new Button(this);
            final int j = i;
            but.setText(String.valueOf(patients.get(i).number));
            but.setOnClickListener((view)->{
                try{
                    FileReader reader = new FileReader(new File(zzpFile,patients.get(j).number+".txt"));
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String str ;
                    while((str = bufferedReader.readLine()) !=null){
                        sb.append(str);
                    }
                    bufferedReader.close();
                    reader.close();
                }catch (Exception e){

                }
            });
            layout.addView(but);
        }
    }
}
