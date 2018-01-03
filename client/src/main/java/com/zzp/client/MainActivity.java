package com.zzp.client;

import android.app.NotificationManager;
import android.content.Context;
import android.net.DhcpInfo;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author zzp
 */
public class MainActivity extends AppCompatActivity {
    TextView text ;
    ClientSocket clientSocket ;
    String hostIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendMessageBtn = findViewById(R.id.sendMessage);
        Button sendErrorMessageBtn = findViewById(R.id.sendErrorMessage);
        Button sendEnd = findViewById(R.id.sendEnd);
        Button restart = findViewById(R.id.restart);
        text = findViewById(R.id.text);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());

        WifiManager wifiManager=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled())  {
            Toast.makeText( this, "请连接服务器WiFi,并重启应用",
                    Toast.LENGTH_SHORT).show();

        }else {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            int i = dhcpInfo.serverAddress;
            //将获取的int转为真正的ip地址
            hostIp = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                    + "." + (i >> 24 & 0xFF);

            startClient();
        }
        sendMessageBtn.setOnClickListener((v)-> {
            startClient();
            clientSocket.sendMessage();
        });
        sendErrorMessageBtn.setOnClickListener((v)-> clientSocket.sendErrorMessage());
        sendEnd.setOnClickListener((v -> {
            clientSocket.senEnd();
            clientSocket = null ;
        }));
        restart.setOnClickListener((v)-> startClient());}

    private void startClient() {

        if (clientSocket == null) {
            clientSocket = new ClientSocket(
                    text,
                    getApplicationContext(),
                    hostIp,
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE)
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(clientSocket!= null){
            clientSocket.senEnd();
            clientSocket.closeSocket();
        }
    }
}

