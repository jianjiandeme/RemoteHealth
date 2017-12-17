package com.zzp.client;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by zzp on 2017/11/22.
 */

public class ClientSocket {


    private Socket socket;
    private PrintStream output;
    TextView text ;
    StringBuffer getString = new StringBuffer();
    String hostIp;
    Context context ;
    //是否从服务端得到信息
    boolean isGetInformation = false;

    public ClientSocket(TextView text,Context context) {
        this.text = text ;
        this.context = context ;
        new Thread(runnable).start();
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

        //获得wifi IP地址
        //获取WifiManager
        WifiManager wifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled())  {
            Toast.makeText( context, "请连接服务器WiFi",
                    Toast.LENGTH_SHORT).show();
        }else{
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            int i = dhcpInfo.serverAddress;
            //将获取的int转为真正的ip地址
            hostIp = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                    + "." + (i >> 24 & 0xFF);
        }
        text.post(()->
                text.setText("服务器IP为:"+ hostIp)
        );

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
    public  void sendMessage() {
        String data = new ZZPRandom("normal").getRandomData();
        output.println("Patient,"+data);
    }
    public void sendErrorMessage() {
        String data = new ZZPRandom("Error").getRandomData();
        output.println("Patient,"+data);
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
