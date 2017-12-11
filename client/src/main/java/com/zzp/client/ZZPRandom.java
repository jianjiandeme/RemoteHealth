package com.zzp.client;

import java.util.Random;

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
        int[] data = new int[3];
        if("normal".equals(flag)) {
            data[1] = random.nextInt(120) % 40 + 80;
            data[2] = random.nextInt(20) % 4 + 16;
            data[3] = random.nextInt(372) % 10 + 363;
            result = data[1]+","+data[2]+","+((float)data[3])/10f+",";
            return result;
        }
        data[1] = random.nextInt(120) % 40 + 90;
        data[2] = random.nextInt(20) % 4 + 18;
        data[3] = random.nextInt(372) % 50 + 363;
        result = data[1]+","+data[2]+","+((float)data[3])/10f+",";
        return result;
    }
}
