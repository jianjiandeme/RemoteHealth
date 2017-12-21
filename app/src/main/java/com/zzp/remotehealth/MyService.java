package com.zzp.remotehealth;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
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

                    String zzpFile = Environment.getExternalStorageDirectory().toString() + "/zzp";
                    File file1=new File(zzpFile);
                    if(!file1.exists()){
                        file1.mkdir();
                    }
                    ServerSocket server = new ServerSocket(5000);
                    Socket client ;
                    while(true){
                        client = server.accept();
                        //获取参数
                        new Thread(new ServerThread(client,getApplicationContext(),(NotificationManager)getSystemService(NOTIFICATION_SERVICE))).start();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
