package com.zzp.client;

import java.util.Random;

import Patient.Patient;

import static Patient.Patient.bloodPressureDown;
import static Patient.Patient.bloodPressureUp;
import static Patient.Patient.respirationDown;
import static Patient.Patient.respirationUp;
import static Patient.Patient.temperatureDown;
import static Patient.Patient.temperatureUp;

/**
 * Created by zzp on 2017/11/25.
 */

public class ZZPRandom {
    String flag;

    public ZZPRandom(String flag) {
        this.flag = flag;
    }
    public String getRandomData(){
        Random random = new Random();
        String result;
        int bloodPressure,respiration;
        float temperature;


        if("normal".equals(flag)) {
            bloodPressure = random.nextInt(bloodPressureUp) %
                    (bloodPressureUp-bloodPressureDown) +bloodPressureUp;
            respiration = random.nextInt(respirationUp) %
                    (respirationUp-respirationDown) +respirationDown;
            temperature = random.nextFloat()*
                    (temperatureUp - temperatureDown)
                     + temperatureDown;
            result = bloodPressure+","+respiration+","+temperature+",";
            return result;
        }

        if(random.nextBoolean())
            bloodPressure = bloodPressureDown - 10;
        else
            bloodPressure = bloodPressureUp - 10;


        if(random.nextBoolean())
            respiration = respirationDown - 10;
        else
            respiration = respirationUp - 10;

        if(random.nextBoolean())
            temperature = temperatureDown - 2;
        else temperature = temperatureUp + 2;

        result = bloodPressure+","+respiration+","+temperature+",";
        return result;
    }
}
