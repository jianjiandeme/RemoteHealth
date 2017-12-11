package Patient;

/**
 * Created by zzp on 2017/11/22.
 */

public class Patient {
    public String number = String.valueOf(i);
    private static long i = 10000000;
    public int bloodPressure ;
    public int respiration ;
    public float temperature;

    public Patient( int bloodPressure, int respiration, float temperature) {
        this.bloodPressure = bloodPressure;
        this.respiration = respiration;
        this.temperature = temperature;
        i++;
    }


}
