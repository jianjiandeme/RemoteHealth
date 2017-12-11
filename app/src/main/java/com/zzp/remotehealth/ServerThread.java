package com.zzp.remotehealth;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import Patient.Patient;

/**
 * Created by zzp on 2017/11/25.
 */

public class ServerThread implements Runnable {
    private Socket client = null;
    private Context context;
    private Patient patient;
    private String number;

    public ServerThread(Socket client,Context context) {
        this.client = client;
        this.context = context;

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
                    }
                    else
                    {
                        patient.bloodPressure = Integer.parseInt(data[1]);
                        patient.respiration = Integer.parseInt(data[2]);
                        patient.temperature = Float.parseFloat(data[3]);
                    }
                    out.println("Echo:" + str);
                    }
            }
            out.close();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}