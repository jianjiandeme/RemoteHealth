package com.zzp.client;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author zzp
 */
public class MainActivity extends AppCompatActivity {
    TextView text ;
    ClientSocket clientSocket ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendBtn = findViewById(R.id.send);
        Button sendMessageBtn = findViewById(R.id.sendMessage);
        Button sendErrorMessageBtn = findViewById(R.id.sendErrorMessage);
        text = findViewById(R.id.text);


        sendBtn.setOnClickListener((v)->{
             //开启线程
            clientSocket = new ClientSocket(text,getApplicationContext());
            });
        sendMessageBtn.setOnClickListener((v)->{
            clientSocket.sendMessage();
        });
        sendErrorMessageBtn.setOnClickListener((v)->{
            clientSocket.sendErrorMessage();
        });
    }

}
