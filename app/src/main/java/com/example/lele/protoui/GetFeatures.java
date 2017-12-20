package com.example.lele.protoui;

/**
 * Created by TeeKee on 2017/12/18.
 */

public class GetFeatures {
    final int SAMPLE = 40;
    float[] acc_x_array = new float[SAMPLE];
    float[] acc_y_array = new float[SAMPLE];
    float[] acc_z_array = new float[SAMPLE];
    float[] acc_g_array = new float[SAMPLE];

    // avg, var, std, max, min, kurt, skew, rms
    float[] g_feature = new float[8];

    float[] get_features(float x[], float y[],float z[]) {
        acc_x_array = x;
        acc_y_array = y;
        acc_z_array = z;
        cal_acc_g();

        cal_g_feature();

        return g_feature;
    }

    void cal_acc_g() {
        int win_length = acc_x_array.length;
        for(int i = 0; i < win_length; i++) {
            acc_g_array[i] = (float) Math.sqrt(Math.pow(acc_x_array[i], 2) +
                    Math.pow(acc_y_array[i], 2) + Math.pow(acc_z_array[i], 2));
        }
    }

    void cal_g_feature(){
        g_feature[0] = cal_mean();
        g_feature[1] = cal_var(g_feature[0]);
        g_feature[2] = cal_std(g_feature[1]);
        g_feature[3] = cal_max();
        g_feature[4] = cal_min();
        g_feature[5] = cal_kurt(g_feature[0], g_feature[2]);
        g_feature[6] = cal_skew(g_feature[0], g_feature[2]);
        g_feature[7] = cal_rms();
    }

    float cal_mean() {
        int t_length = acc_g_array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++)
            t_sum = t_sum + acc_g_array[i];;
        return (t_sum / t_length);
    }

    float cal_var(float t_mean){
        int t_length = acc_g_array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++)
            t_sum = t_sum + (float) Math.pow((acc_g_array[i] - t_mean), 2);
        return (t_sum / t_length);
    }

    float cal_std(float t_var){
        return ((float) Math.sqrt(t_var));
    }

    float cal_max() {
        float t_max = 0;
        for(int i = 0; i < acc_g_array.length; i++)
            if(acc_g_array[i] > t_max)
                t_max = acc_g_array[i];
        return t_max;
    }

    float cal_min() {
        float t_min = acc_g_array[0];
        for(int i = 1; i < acc_g_array.length; i++)
            if(acc_g_array[i] < t_min)
                t_min = acc_g_array[i];
        return t_min;
    }

    float cal_kurt(float t_mean, float t_std){
        float t_length = acc_g_array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++) {
            t_sum = t_sum + (float) Math.pow((acc_g_array[i] - t_mean), 4);
        }
        float temp = (t_length - 1) * (float) Math.pow(t_std, 3);
        return (t_sum / temp);
    }

    float cal_skew(float t_mean, float t_std){
        float t_length = acc_g_array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++) {
            t_sum = t_sum + (float) Math.pow((acc_g_array[i] - t_mean), 3);
        }
        float temp = (t_length - 1) * (float) Math.pow(t_std, 3);
        return (t_sum / temp);
    }

    float cal_rms(){
        int t_length = acc_g_array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++) {
            t_sum = t_sum + (float) Math.pow(acc_g_array[i], 2);
        }
        return ((float) Math.sqrt(t_sum / t_length));
    }
}
