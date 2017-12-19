package com.example.lele.protoui;

/**
 * Created by TeeKee on 2017/12/18.
 */

public class GetFeatures {
    float[] acc_x_array = new float[20];
    float[] acc_y_array = new float[20];
    float[] acc_z_array = new float[20];
    float[] acc_g_array = new float[20];

    // avg, var, std, rms, skew, kurt, max, min
    float[] x_feature = new float[8];
    float[] y_feature = new float[8];
    float[] z_feature = new float[8];
    float[] g_feature = new float[8];

    float[] mean_feature = new float[8];
    float[] var_feature = new float[8];
    float[] std_feature = new float[8];
    float[] rms_feature = new float[8];
    float[] skew_feature = new float[8];
    float[] kurt_feature = new float[8];
    float[] max_feature = new float[8];
    float[] min_feature = new float[8];

    float[] features= new float[32];

    float[] get_features(float x[], float y[],float z[]) {
        acc_x_array = x;
        acc_y_array = y;
        acc_z_array = z;
        cal_acc_g();

        cal_g_feature();
        cal_x_feature();
        cal_y_feature();
        cal_z_feature();

        copy_features();

        return features;
    }

    void cal_acc_g() {
        int win_length = acc_x_array.length;
        for(int i = 0; i < win_length; i++) {
            acc_g_array[i] = (float) Math.sqrt(Math.pow(acc_x_array[i], 2) +
                    Math.pow(acc_y_array[i], 2) + Math.pow(acc_z_array[i], 2));
        }
    }

    void cal_g_feature(){
        g_feature[0] = cal_mean(acc_g_array);
        g_feature[1] = cal_var(acc_g_array, g_feature[0]);
        g_feature[2] = cal_std(g_feature[1]);
        g_feature[3] = cal_rms(acc_g_array);
        g_feature[4] = cal_skew(acc_g_array, g_feature[0], g_feature[2]);
        g_feature[5] = cal_kurt(acc_g_array, g_feature[0], g_feature[2]);
        g_feature[6] = cal_max(acc_g_array);
        g_feature[7] = cal_min(acc_g_array);

        mean_feature[0] = g_feature[0];
        var_feature[0] = g_feature[1];
        std_feature[0] = g_feature[2];
        rms_feature[0] = g_feature[3];
        skew_feature[0] = g_feature[4];
        kurt_feature[0] = g_feature[5];
        max_feature[0] = g_feature[6];
        min_feature[0] = g_feature[7];
    }

    void cal_x_feature(){
        x_feature[0] = cal_mean(acc_x_array);
        x_feature[1] = cal_var(acc_x_array, x_feature[0]);
        x_feature[2] = cal_std(x_feature[1]);
        x_feature[3] = cal_rms(acc_x_array);
        x_feature[4] = cal_skew(acc_x_array, x_feature[0], x_feature[2]);
        x_feature[5] = cal_kurt(acc_x_array, x_feature[0], x_feature[2]);
        x_feature[6] = cal_max(acc_x_array);
        x_feature[7] = cal_min(acc_x_array);

        mean_feature[1] = x_feature[0];
        var_feature[1] = x_feature[1];
        std_feature[1] = x_feature[2];
        rms_feature[1] = x_feature[3];
        skew_feature[1] = x_feature[4];
        kurt_feature[1] = x_feature[5];
        max_feature[1] = x_feature[6];
        min_feature[1] = x_feature[7];
    }

    void cal_y_feature(){
        y_feature[0] = cal_mean(acc_y_array);
        y_feature[1] = cal_var(acc_y_array, y_feature[0]);
        y_feature[2] = cal_std(y_feature[1]);
        y_feature[3] = cal_rms(acc_y_array);
        y_feature[4] = cal_skew(acc_y_array, y_feature[0], y_feature[2]);
        y_feature[5] = cal_kurt(acc_y_array, y_feature[0], y_feature[2]);
        y_feature[6] = cal_max(acc_y_array);
        y_feature[7] = cal_min(acc_y_array);

        mean_feature[2] = y_feature[0];
        var_feature[2] = y_feature[1];
        std_feature[2] = y_feature[2];
        rms_feature[2] = y_feature[3];
        skew_feature[2] = y_feature[4];
        kurt_feature[2] = y_feature[5];
        max_feature[2] = y_feature[6];
        min_feature[2] = y_feature[7];
    }

    void cal_z_feature(){
        z_feature[0] = cal_mean(acc_z_array);
        z_feature[1] = cal_var(acc_z_array, z_feature[0]);
        z_feature[2] = cal_std(z_feature[1]);
        z_feature[3] = cal_rms(acc_z_array);
        z_feature[4] = cal_skew(acc_z_array, z_feature[0], z_feature[2]);
        z_feature[5] = cal_kurt(acc_z_array, z_feature[0], z_feature[2]);
        z_feature[6] = cal_max(acc_z_array);
        z_feature[7] = cal_min(acc_z_array);

        mean_feature[3] = z_feature[0];
        var_feature[3] = z_feature[1];
        std_feature[3] = z_feature[2];
        rms_feature[3] = z_feature[3];
        skew_feature[3] = z_feature[4];
        kurt_feature[3] = z_feature[5];
        max_feature[3] = z_feature[6];
        min_feature[3] = z_feature[7];
    }


    float cal_mean(float array[]) {
        int t_length = array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++)
            t_sum = t_sum + array[i];;
        return (t_sum / array.length);
    }

    float cal_var(float array[], float t_mean){
        int t_length = array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++)
            t_sum = t_sum + (float) Math.pow((array[i] - t_mean), 2);
        return (t_sum / t_length);
    }

    float cal_std(float t_var){
        return ((float) Math.sqrt(t_var));
    }

    float cal_rms(float array[]){
        int t_length = array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++) {
            t_sum = t_sum + (float) Math.pow(array[i], 2);
        }
        return ((float) Math.sqrt(t_sum / t_length));
    }

    float cal_skew(float array[], float t_mean, float t_std){
        float t_length = array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++) {
            t_sum = t_sum + (float) Math.pow((array[i] - t_mean), 3);
        }
        float temp = (t_length - 1) * (float) Math.pow(t_std, 3);
        return (t_sum / temp);
    }

    float cal_kurt(float array[], float t_mean, float t_std){
        float t_length = array.length;
        float t_sum = 0;
        for(int i = 0; i < t_length; i++) {
            t_sum = t_sum + (float) Math.pow((array[i] - t_mean), 4);
        }
        float temp = (t_length - 1) * (float) Math.pow(t_std, 3);
        return (t_sum / temp);
    }

    float cal_max(float array[]) {
        float t_max = 0;
        for(int i = 0; i < array.length; i++)
            if(array[i] > t_max)
                t_max = array[i];
        return t_max;
    }

    float cal_min(float array[]) {
        float t_min = array[0];
        for(int i = 1; i < array.length; i++)
            if(array[i] < t_min)
                t_min = array[i];
        return t_min;
    }

    void copy_features() {
        features[0] = g_feature[0];
        features[1] = g_feature[1];
        features[2] = g_feature[2];
        features[3] = g_feature[3];
        features[4] = g_feature[4];
        features[5] = g_feature[5];
        features[6] = g_feature[6];
        features[7] = g_feature[7];
        features[8] = x_feature[0];
        features[9] = x_feature[1];
        features[10] = x_feature[2];
        features[11] = x_feature[3];
        features[12] = x_feature[4];
        features[13] = x_feature[5];
        features[14] = x_feature[6];
        features[15] = x_feature[7];
        features[16] = y_feature[0];
        features[17] = y_feature[1];
        features[18] = y_feature[2];
        features[19] = y_feature[3];
        features[20] = y_feature[4];
        features[21] = y_feature[5];
        features[22] = y_feature[6];
        features[23] = y_feature[7];
        features[24] = z_feature[0];
        features[25] = z_feature[1];
        features[26] = z_feature[2];
        features[27] = z_feature[3];
        features[28] = z_feature[4];
        features[29] = z_feature[5];
        features[30] = z_feature[6];
        features[31] = z_feature[7];
    }
}
