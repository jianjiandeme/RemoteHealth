package com.zzp.remotehealth;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import Patient.Patient;

import static utils.Constants.patients;

/**
 * Created by zzp on 2017/11/25.
 */

public class ServerThread implements Runnable {
    private Socket client = null;
    private Context context;
    private Patient patient;
    private String number;
    private NotificationManager manager;
    private int notifyTime = 1;
    private String zzpFile = Environment.getExternalStorageDirectory().toString() + "/zzp";

    ServerThread(Socket client,Context context,NotificationManager manager) {
        this.client = client;
        this.context = context;
        this.manager = manager;

    }

    @Override
    public void run() {
        PrintStream out ;
        BufferedReader buf ;
        try {
            buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintStream(client.getOutputStream());
            boolean flag = true;

            while (flag) {
                String str = buf.readLine();
                if (str == null || "".equals(str)) {
                    flag = false;
                } else if ("end".equals(str)) {
                        flag = false;
                    } else if(str.contains("Patient")){
                    String[] data = str.split(",");
                    if(patient == null){
                        patient = new Patient(Integer.parseInt(data[1]),
                                Integer.parseInt(data[2]),Float.parseFloat(data[3]));
                        patients.add(patient);
                    }
                    else
                    {
                        patient.bloodPressure = Integer.parseInt(data[1]);
                        patient.respiration = Integer.parseInt(data[2]);
                        patient.temperature = Float.parseFloat(data[3]);
                    }
                    boolean isError = false;
                    StringBuilder parameter = new StringBuilder();
                    StringBuilder sb = new StringBuilder();
                    parameter.append(patient.bloodPressureDown).append(",").
                            append(patient.bloodPressureUp).append(",").
                            append(patient.respirationDown).append(",").
                            append(patient.respirationUp).append(",").
                            append(patient.temperatureDown).append(",").
                            append(patient.temperatureUp).append(",").
                            append(patient.number).append(",").
                            append(patient.frequence).append(" ");

                    if(patient.bloodPressure<patient.bloodPressureDown) {
                        sb.append("血压过低,");
                        isError = true;
                    }
                    if(patient.bloodPressure>patient.bloodPressureUp){
                        sb.append("血压过高,");
                        isError = true;
                    }

                    if(patient.respiration<patient.respirationDown){
                        sb.append("呼吸过慢,");
                        isError = true;
                    }
                    if(patient.respiration>patient.respirationUp){
                        sb.append("呼吸过快,");
                        isError = true;
                    }
                    if(patient.temperature<patient.temperatureDown){
                        sb.append("体温过低,");
                        isError = true;
                    }
                    if(patient.temperature>patient.temperatureUp){
                        sb.append("体温过高,");
                        isError = true;
                    }
                    if(isError){
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                //设置小图标
                                .setSmallIcon(R.mipmap.ic_launcher)
                                //设置通知标题
                                .setContentTitle(patient.number)
                                //设置通知内容
                                .setContentText(sb.toString())
                                .setVibrate(new long[]{0,100,100,100});
                        manager.notify(notifyTime++ ,builder.build());
                    }


                    FileWriter writer ;

                    StringBuilder txtString = new StringBuilder();
                    File file = new File(zzpFile,patient.number+".txt");
                    if(!file.exists()){
                        file.createNewFile();
                        writer = new FileWriter(file,true);
                        writer.write(patient.number+"/n  \t\t时间\t\t\t\t\t\t血压\t\t\t\t呼吸\t\t\t\t体温\n");
                        writer.close();
                    }

                    writer = new FileWriter(file,true);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    txtString.append(df.format(new Date())).append("\t").
                            append("\t").append(patient.bloodPressure).append("\t").
                            append("\t").append(patient.respiration).append("\t").
                            append("\t").append(patient.temperature).append("\n");
                    writer.write(txtString.toString()+sb);
                    writer.close();
                    out.println(parameter.append(sb));
                    }
            }

            out.close();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}