package com.zzp.remotehealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        server = findViewById(R.id.server);
        text =  findViewById(R.id.text);

        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerThread().start();
            }
        });
    }
    private class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                print("server:waiting for connecting");
                serverSocket = new ServerSocket(5000);
                Socket clientSocket;
                while (true) {
                    clientSocket = serverSocket.accept();
                    String remoteIP = clientSocket.getInetAddress().getHostAddress();
                    int remotePort = clientSocket.getLocalPort();
                    Log.e(TAG, "A client connected. IP:" + remoteIP + ", Port: " + remotePort);
                    print("server:receiving");


                    in = new BufferedReader(new InputStreamReader(
                            clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), false);

                    // 获得 client 端发送的数据
                    String tmp = in.readLine();
                    // String content = new String(tmp.getBytes("utf-8"));
//                    System.out.println("Client message is: " + tmp);

                    print("Client message is: " + tmp);
                    // 向 client 端发送响应数据
                    out.println("Your message has been received successfully！.");

                    out.close();
                    in.close();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
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


