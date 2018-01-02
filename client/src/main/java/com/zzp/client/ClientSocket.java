package com.zzp.client;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import Patient.Patient;

/**
 * Created by zzp on 2017/11/22.
 */

 class ClientSocket {
    private Socket socket;
    private PrintStream output;
    private TextView text ;
    private StringBuffer getString = new StringBuffer();
    private String hostIp;
    private Context context ;
    private NotificationManager manager;
    public Timer timer = new Timer();
    private int notiTime = 1;
    private String data ;

     ClientSocket(TextView text,Context context,String hostIp,NotificationManager manager) {
        this.text = text ;
        this.context = context ;
        this.hostIp = hostIp;
        this.manager = manager;
        new Thread(()->{
            initClientSocket();
            ReadThread readThread = new ReadThread();
            readThread.start();
        }).start();
    }


    private void initClientSocket() {
        try {
            socket = new Socket(hostIp, 5000);
            output = new PrintStream(socket.getOutputStream(), true, "gbk");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Toast.makeText(context,"请检查连接",Toast.LENGTH_SHORT).show();
            System.out.println("请检查端口号是否为服务器IP");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(context,"请检查连接",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage();
            }
        }, 0, Patient.frequent * 1000);

    }




    private String receiveData() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(hostIp, 5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BufferedReader buf ;
        String str;
        if (socket.isConnected()) {
            try {
                buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                str= buf.readLine();
                return str;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            return null;
    }



      void sendMessage() {
         data = new ZZPRandom("normal").getRandomData();
        new Thread(()->output.println("Patient,"+data)).start();
        print("血压："+Patient.bloodPressure+" 呼吸："+Patient.respiration+"体温 "+Patient.temperature);
    }

     void sendErrorMessage() {
         data = new ZZPRandom("Error").getRandomData();
        new Thread(()->output.println("Patient,"+data)).start();
    }

    public void senEnd() {
         data = "end";
        new Thread(()->output.println(data)).start();
    }


    private class ReadThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (true) {
                String str = receiveData();
                if (str!=null && str.length() > 1) {
                    String[] limits ;
                    String errors;
                    if(str.contains("过")){
                        String[] two = str.split(" ");
                        limits = two[0].split(",");
                        errors = two[1];
                        sendNotification(errors);
                    }else {
                        limits = str.split(",");
                    }

                    Patient.bloodPressureDown = Integer.parseInt(limits[0]);
                    Patient.bloodPressureUp = Integer.parseInt(limits[1]);
                    Patient.respirationDown = Integer.parseInt(limits[2]);
                    Patient.respirationUp = Integer.parseInt(limits[3]);
                    Patient.temperatureDown = Float.parseFloat(limits[4]);
                    Patient.temperatureUp = Float.parseFloat(limits[5]);
                    Patient.number = limits[6];
                    int tempFrequent =  Integer.parseInt(limits[7].trim());
                    if(tempFrequent !=Patient.frequent ){
                        Patient.frequent = tempFrequent;
                        if(timer!=null)timer.cancel();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendMessage();
                            }
                        }, 0, Patient.frequent * 1000);
                    }

                }
            }
        }


    }
    private void print(String str) {
        getString.append(str).append("\n");
        text.post(() ->
                text.setText(getString)
        );
    }
    private void sendNotification(String errors) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle(Patient.number)
                //设置通知内容
                .setContentText(errors)
                .setVibrate(new long[]{0,100,100,100});
        manager.notify( notiTime++%10 , builder.build() );
    }

    public void closeSocket(){
         try{
             if(timer!= null){
                 timer.cancel();
                 socket.close();
             }
         }catch (Exception e){
         }
    }
}
