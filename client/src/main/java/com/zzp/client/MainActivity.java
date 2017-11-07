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

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private PrintStream output;
    TextView text ;
    StringBuffer getString = new StringBuffer();
    String hostIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendBtn = findViewById(R.id.send);
        Button sendMessageBtn = findViewById(R.id.sendMessage);
        text = findViewById(R.id.text);

        //获得wifi IP地址
        //获取WifiManager
        WifiManager wifiManager=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled())  {
            Toast.makeText(getApplicationContext(), "请连接服务器WiFi",
                    Toast.LENGTH_SHORT).show();
        }else{
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            int i = dhcpInfo.serverAddress;
            //将获取的int转为真正的ip地址,参考的网上的，修改了下
            hostIp = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                    + "." + (i >> 24 & 0xFF);
        }

        text.setText("服务器IP为:"+ hostIp);
        sendBtn.setOnClickListener((v)->{
             //开启线程
            new Thread(runnable).start();
            });
        sendMessageBtn.setOnClickListener((v)->{
                // TODO Auto-generated method stub
                sendMessage("hello,i am from client message");
        }
        );


    }

    Runnable runnable = new Runnable() {

            @Override
            public void run() {
            // TODO Auto-generated method stub
            initClientSocket();
            ReadThread readThread = new ReadThread();
            readThread.start();
        }
    };
    public void initClientSocket() {
        try {
            socket = new Socket(hostIp, 5000);
            output = new PrintStream(socket.getOutputStream(), true, "gbk");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.out.println("请检查端口号是否为服务器IP");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("服务器未开启");
            e.printStackTrace();
        }
        output.println("this is the message from client");
    }
    public byte[] receiveData() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(hostIp, 5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        byte[] data = null;
        BufferedInputStream bufferedInputStream;

        if (socket.isConnected()) {
            try {
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                data = new byte[bufferedInputStream.available()];
                bufferedInputStream.read(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            data = new byte[1];
        }
        return data;
    }
    private void sendMessage(String str) {
        output.println(str);
    }

    public void closeSocket() {
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("error"+e);
        }
    }
    private class ReadThread extends Thread{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            while (true) {
                byte[] data = receiveData();
                if (data.length > 1) {
                    print(new String(data));
                }
            }
        }

        private void print(String str) {
            getString.append(str+"\n");
            text.post(() ->
                    text.setText(getString)
            );
        }
    }

}
