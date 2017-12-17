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
    Button fragment,houtai;
    static String TAG =  "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = findViewById(R.id.fragment);
        houtai =  findViewById(R.id.houtai);
        startService(new Intent(this,MyService.class));
        fragment.setOnClickListener((v)->{
            startActivity(new Intent(this,InformationActivity.class));
        });
        houtai.setOnClickListener((v)->{
            startActivity(new Intent(this,InformationActivity.class));
        });
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


//        private void print(String str) {
//            getString.append(str+"\n");
//            text.post(() ->
//                    text.setText(getString)
//            );
//        }
}



