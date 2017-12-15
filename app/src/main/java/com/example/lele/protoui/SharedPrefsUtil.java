package com.example.lele.protoui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LeLe on 2017/9/6.
 */
public class SharedPrefsUtil {

    private String PATH;

    //保存偏好设置的HashMap
    public void savePreferences(Context context, String dataName, Map<String, int[]> Data) {
        SharedPreferences sp = context.getSharedPreferences(dataName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //从HashMap中遍历int[]result
        for (Map.Entry<String, int[]> entry : Data.entrySet()) {
            String key = entry.getKey();
            int[] result = entry.getValue();
            String preferences = int2string(result);
            editor.putString(key, preferences);
            editor.commit();
        }
    }

    public void savePreferences_login(Context context, String dataName, Map<String, String> Data) {
        SharedPreferences sp = context.getSharedPreferences(dataName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //从HashMap中遍历int[]result
        for (Map.Entry<String, String> entry : Data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            editor.putString(key, value);
            editor.commit();
        }
    }

    //保存用户的头像Uri，由于地址固定，则使用boolean，true表示有头像
    public void savePreferences(Context context, String dataName, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(dataName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        PATH = "/data/data/protoui.sharedpreferences/shared_prefs/" + dataName + ".xml";
        editor.putBoolean("Avatar", value);
        editor.commit();
    }

    public boolean readPreferences(Context context, String dataName) {
        SharedPreferences sp = context.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        boolean str = sp.getBoolean("Avatar", false);
        return str;
    }

    public Map<String, int[]> readPreferencesSetting(Context context, String dataName) {
        Map<String, int[]> Data = new HashMap<String, int[]>();

        SharedPreferences sp = context.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        Map<String, ?> allContent = sp.getAll();

        for (Map.Entry<String, ?> entry : allContent.entrySet()) {
            if (entry.getKey() != "Avatar") {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                int[] result = string2int(value);
                Data.put(key, result);
            }
        }
        return Data;
    }

    public String readPreferences_login(Context context, String dataName){
        SharedPreferences sp = context.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        return sp.getString("username",null);
    }

    private String int2string(int[] in) {
        String out = new String();
        out = out + in[0];
        for (int i = 1; i < in.length; i++) {
            out = out + "," + in[i];
        }
        return out;
    }

    private int[] string2int(String str) {
        String[] result1 = str.split(",");
        int[] result2 = new int[result1.length];
        for (int i = 0; i < result2.length; i++) {
            result2[i] = Integer.parseInt(result1[i]);
        }
        return result2;
    }

    private String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }
}
