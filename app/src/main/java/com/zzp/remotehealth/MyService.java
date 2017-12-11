package com.zzp.remotehealth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.ServerSocket;
import java.net.Socket;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSocket();
    }

    private void initSocket() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket server = new ServerSocket(5000);
                    Socket client ;
                    while(true){
                        client = server.accept();
                        //获取参数
                        new Thread(new ServerThread(client,getApplicationContext())).start();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
