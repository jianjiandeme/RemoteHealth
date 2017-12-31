package utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import Patient.Patient;

/**
 * Created by zzp on 2017/11/25.
 */

public class Constants {
    public static ArrayList<Patient> patients = new ArrayList<Patient>() ;

    public static String zzpFile = Environment.getExternalStorageDirectory().toString()+"/zzp";

    public static File[] array;

    public static int notifyTime = 0 ;
}
