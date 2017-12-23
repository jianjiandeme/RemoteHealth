package Patient;

/**
 * Created by zzp on 2017/11/22.
 */

public class Patient {
    public String number = String.valueOf(i);
    private static long i = 100000000;
    public int frequent = 5;
    public int bloodPressure ;
    public int respiration ;
    public float temperature;
    public int bloodPressureUp=120,
            bloodPressureDown=80,
            respirationUp=20,
            respirationDown=16;
    public float temperatureDown=35f,
            temperatureUp=40f;


    public Patient( int bloodPressure, int respiration, float temperature) {
        this.bloodPressure = bloodPressure;
        this.respiration = respiration;
        this.temperature = temperature;
        i++;
    }


}
