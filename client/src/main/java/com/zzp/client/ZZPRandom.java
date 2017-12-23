package com.zzp.client;

import java.text.DecimalFormat;
import java.util.Random;

import Patient.Patient;

import static Patient.Patient.bloodPressureDown;
import static Patient.Patient.bloodPressureUp;
import static Patient.Patient.respirationDown;
import static Patient.Patient.respirationUp;
import static Patient.Patient.temperatureDown;
import static Patient.Patient.temperatureUp;



 class ZZPRandom {
    private String flag;

     ZZPRandom(String flag) {
        this.flag = flag;
    }
     String getRandomData(){
        Random random = new Random();
        String result;
            Patient.bloodPressure = random.nextInt(bloodPressureUp) %
                    (bloodPressureUp-bloodPressureDown) +bloodPressureDown;
        Patient.respiration = random.nextInt(respirationUp) %
                    (respirationUp-respirationDown) +respirationDown;
        DecimalFormat df = new DecimalFormat("0.00");
        Patient.temperature = df.format(random.nextFloat()*
                (temperatureUp - temperatureDown)
                + temperatureDown);
            result = Patient.bloodPressure+","+Patient.respiration+","+Patient.temperature+",";


        if("normal".equals(flag)) {
            return result;
        }

        if(random.nextBoolean()){
            if(random.nextBoolean())
                Patient.bloodPressure = bloodPressureDown - 10;
            else
                Patient.bloodPressure = bloodPressureUp + 10;
        }


        if(random.nextBoolean()){
            if(random.nextBoolean())
                Patient.respiration = respirationDown - 10;
            else
                Patient.respiration = respirationUp + 10;
        }

        if(random.nextBoolean()) {
            if (random.nextBoolean())
                Patient.temperature = String.valueOf(temperatureDown - 2);
            else Patient.temperature = String.valueOf(temperatureDown + 2);
        }
        result = Patient.bloodPressure+","+Patient.respiration+","+Patient.temperature+",";
        return result;
    }
}
