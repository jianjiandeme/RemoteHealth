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
            data[0] = random.nextInt(120) % 40 + 80;
            data[1] = random.nextInt(20) % 4 + 16;
            data[2] = random.nextInt(372) % 10 + 363;
            result = data[0]+","+data[1]+","+((float)data[2])/10f+",";
            return result;
        }
        data[0] = random.nextInt(120) % 40 + 90;
        data[1] = random.nextInt(20) % 4 + 18;
        data[2] = random.nextInt(372) % 50 + 363;
        result = data[0]+","+data[1]+","+((float)data[2])/10f+",";
        return result;
    }
}
