package Patient;

/**
 * Created by zzp on 2017/11/22.
 */

public class Patient {
    public String number = String.valueOf(i);
    private static long i = 100000000;
    public int bloodPressure ;
    public int respiration ;
    public float temperature;

    public int bloodPressureUp=1000,bloodPressureDown=0,respirationUp=1000,respirationDown=0;
    public float temperatureDown=0f,temperatureUp=42f;

    public Patient( int bloodPressure, int respiration, float temperature) {
        this.bloodPressure = bloodPressure;
        this.respiration = respiration;
        this.temperature = temperature;
        i++;
    }


}
