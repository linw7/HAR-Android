package com.example.lele.protoui;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by TeeKee on 2017/12/18.
 */

public class OfflineFileRW {

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

    public String get(Context context, String file_name) {
        File file = new File(context.getFilesDir(), file_name);
        String read_line = new String();
        String text = new String();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while((read_line = br.readLine()) != null){
                text = text + read_line + "\n";
            }
            text = text.substring(0,text.length() - 1);
            br.close();
            return text;
        } catch (Exception e) {
            return "error";
        }
    }
}
