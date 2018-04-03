package com.example.lele.protoui;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by TeeKee on 2017/12/18.
 */

public class OfflineFileRW {
    private int sit_time = 0;
    private int stand_time = 0;
    private int upstairs_time = 0;
    private int downstairs_time = 0;
    private int walk_time = 0;
    private int jog_time = 0;

    private void init(){
        sit_time = 0;
        stand_time = 0;
        upstairs_time = 0;
        downstairs_time = 0;
        walk_time = 0;
        jog_time = 0;
    }

    public String clear(Context context, String file_name) {
        File file = new File(context.getFilesDir(), file_name);
        try {
            FileOutputStream fos = new FileOutputStream(file);
        } catch (Exception e){
            return "error";
        }
        return "success";
    }

    public String save(Context context, String file_name, ArrayList<String> array_record) {
        File file = new File(context.getFilesDir(), file_name);
        String write_line = new String();
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            for(int i = 0; i < array_record.size(); i++)
                write_line = write_line + array_record.get(i);
            fos.write(write_line.getBytes());
            fos.close();
        } catch (Exception e) {
            return "error";
        }
        return file.getAbsolutePath();
    }

    public void resolve(String time){
        String data[] = time.split("-");
        sit_time = sit_time + Integer.valueOf(data[0]).intValue();
        stand_time = stand_time + Integer.valueOf(data[1]).intValue();
        upstairs_time = upstairs_time + Integer.valueOf(data[2]).intValue();
        downstairs_time = downstairs_time + Integer.valueOf(data[3]).intValue();
        walk_time = walk_time + Integer.valueOf(data[4]).intValue();
        jog_time = jog_time + Integer.valueOf(data[5]).intValue();
    }

    public ArrayList get(Context context, String file_name, String input_date) {
        init();
        ArrayList date_data = new ArrayList();
        String text = new String();

        File file = new File(context.getFilesDir(), file_name);
        String read_line = new String();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while((read_line = br.readLine()) != null){
                String data[] = read_line.split(":");
                String date = data[0];
                String time = data[1];
                if(date.equals(input_date)){
                    resolve(time);
                }
                // text = text + read_line + "\n";
            }
            // text = text.substring(0,text.length() - 1);
            br.close();

            date_data.add(sit_time);
            date_data.add(stand_time);
            date_data.add(upstairs_time);
            date_data.add(downstairs_time);
            date_data.add(walk_time);
            date_data.add(jog_time);
            return date_data;
        } catch (Exception e) {
            return date_data;
        }
    }
}
