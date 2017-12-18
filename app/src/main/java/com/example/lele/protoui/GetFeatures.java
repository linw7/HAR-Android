package com.example.lele.protoui;

/**
 * Created by TeeKee on 2017/12/18.
 */

public class GetFeatures {
    float[] acc_x_array = new float[20];
    float[] acc_y_array = new float[20];
    float[] acc_z_array = new float[20];
    float[] average = new float[3];
    float[] max = new float [3];
    float[] min = new float[3] ;
    float[] features= new float[9];

    float[] get_features(float x[], float y[],float z[]) {
        acc_x_array = x;
        acc_y_array = y;
        acc_z_array = z;

        this.get_average();
        this.get_max();
        this.get_min();
        this.copy_features();

        return features;
    }

    void get_average() {
        average[0] = cal_average(acc_x_array);
        average[1] = cal_average(acc_y_array);
        average[2] = cal_average(acc_z_array);
    }

    float cal_average(float array[]) {
        float t_average = 0;
        float t_sum = 0;
        for(int i = 0; i < array.length; i++)
            t_sum = t_sum + array[i];
        t_average = t_sum / array.length;
        return t_average;
    }

    void get_max() {
        max[0] = cal_max(acc_x_array);
        max[1] = cal_max(acc_y_array);
        max[2] = cal_max(acc_z_array);
    }

    float cal_max(float array[]) {
        float t_max = 0;
        for(int i = 0; i < array.length; i++)
            if(array[i] > t_max)
                t_max = array[i];
        return t_max;
    }

    void get_min() {
        min[0] = cal_min(acc_x_array);
        min[1] = cal_min(acc_y_array);
        min[2] = cal_min(acc_z_array);
    }

    float cal_min(float array[]) {
        float t_min = array[0];
        for(int i = 1; i < array.length; i++)
            if(array[i] < t_min)
                t_min = array[i];
        return t_min;
    }

    void copy_features() {
        features[0] = average[0];
        features[1] = average[1];
        features[2] = average[2];
        features[3] = max[0];
        features[4] = max[1];
        features[5] = max[2];
        features[6] = min[0];
        features[7] = min[1];
        features[8] = min[2];
    }
}
