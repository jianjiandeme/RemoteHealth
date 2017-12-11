package Patient;

/**
 * Created by zzp on 2017/11/22.
 */

public class Patient {
    public String number ;
    public int bloodPressure ;
    public int respiration ;
    public float temperature;

    public Patient(String number, int bloodPressure, int respiration, float temperature) {
        this.number = number;
        this.bloodPressure = bloodPressure;
        this.respiration = respiration;
        this.temperature = temperature;
    }

}
