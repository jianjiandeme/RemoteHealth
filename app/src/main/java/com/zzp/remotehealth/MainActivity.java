package com.zzp.remotehealth;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import utils.Constants;


/**
    * @author zzp
    * @version 1.0
     */
public class MainActivity extends AppCompatActivity {

    Button server;
    ServerSocket serverSocket;
    BufferedReader in;
    PrintWriter out;
    static String TAG =  "MainActivity";
    TextView text ;
    StringBuffer getString = new StringBuffer();
    MyReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = findViewById(R.id.server);
        text =  findViewById(R.id.text);
        server.setOnClickListener((v)->{
            print("等待连接");
            regFilter();
            startServer();
        });
    }


    @Override
    public void onDestroy() {
        Log.w(TAG, "onDestroy");
        super.onDestroy();
//        unregisterReceiver

        if (serviceReceiver != null) {
            unregisterReceiver(serviceReceiver);
        }
    }
    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_GET);
        filter.setPriority(1000);
        serviceReceiver = new MyReceiver();
        registerReceiver(serviceReceiver, filter);
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i =
                try {
                    ServerSocket server = new ServerSocket(5000);
                    Socket client ;

                    while(true){
                        client = server.accept();
                        print("连接成功");
                        //获取参数

                        new Thread(new ServerThread(client,getApplicationContext())).start();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
//    private class ServerThread extends Thread {
//        @Override
//        public void run() {
//            try {
//                print("server:waiting for connecting");
//                serverSocket = new ServerSocket(5000);
//                Socket clientSocket;
//                while (true) {
//                    clientSocket = serverSocket.accept();
//                    String remoteIP = clientSocket.getInetAddress().getHostAddress();
//                    int remotePort = clientSocket.getLocalPort();
//                    Log.e(TAG, "A client connected. IP:" + remoteIP + ", Port: " + remotePort);
//                    print("server:receiving");
//
//
//                    in = new BufferedReader(new InputStreamReader(
//                            clientSocket.getInputStream()));
//                    out = new PrintWriter(clientSocket.getOutputStream(), false);
//
//                    // 获得 client 端发送的数据
//                    String tmp = in.readLine();
//                    // String content = new String(tmp.getBytes("utf-8"));
////                    System.out.println("Client message is: " + tmp);
//
//                    print("Client message is: " + tmp);
//                    // 向 client 端发送响应数据
//                    out.println("Your message has been received successfully！.");
//
//                    out.close();
//                    in.close();
//
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }


        private void print(String str) {
            getString.append(str+"\n");
            text.post(() ->
                    text.setText(getString)
            );
        }
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String setParameter = intent.getAction();
            if (setParameter.equals(Constants.ACTION_GET)) {
                final EditText et = new EditText(getApplicationContext());
                Dialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("请输入参数").setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if ("".equals(input)) {
                                    Toast.makeText(getApplicationContext(), "请输入参数！" , Toast.LENGTH_SHORT).show();
                                }else if(input.length()!=9 || ){

                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                alertDialog.show();
            }
        }
    }
}



