package com.zzp.client;

import android.app.NotificationManager;
import android.content.Context;
import android.net.DhcpInfo;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


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
        String hostIp;

        WifiManager wifiManager=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled())  {
            Toast.makeText( this, "请连接服务器WiFi,并重启应用",
                    Toast.LENGTH_SHORT).show();
            finish();
            hostIp = "0.0.0.0";
        }else{
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            int i = dhcpInfo.serverAddress;
            //将获取的int转为真正的ip地址
            hostIp = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                    + "." + (i >> 24 & 0xFF);
                text.setText("服务器IP为:"+ hostIp);
        }


        sendBtn.setOnClickListener((v)->{
             //开启线程
            if(clientSocket == null) {
                clientSocket = new ClientSocket(
                        text,
                        getApplicationContext(),
                        hostIp,
                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE)
                );
            }
            });
        sendMessageBtn.setOnClickListener((v)-> clientSocket.sendMessage());
        sendErrorMessageBtn.setOnClickListener((v)-> clientSocket.sendErrorMessage());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(clientSocket.timer != null){
            clientSocket.timer.cancel();
        }
    }
}
