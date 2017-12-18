package com.example.lele.protoui;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by TeeKee on 2017/12/18.
 */

public class OfflineFileRW {
    private ArrayList<String> array_record_line = new ArrayList<String>();
    private String raw_data = new String();
    String path;
    public String write_raw_data(ArrayList<String> array_record_line){
        this.array_record_line = array_record_line;

        boolean is_mounted = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if(is_mounted) {
            try {
                get_data();
                write_raw_sd();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("sd error", "have no sd ！");
        }
        return path;
    }

    public void get_data() {
        for(int i = 0; i < this.array_record_line.size(); i++) {
            raw_data = raw_data + this.array_record_line.get(i);
        }
    }

    public void write_raw_sd()  throws IOException{
        File parent_path = Environment.getExternalStorageDirectory();
        File file_dir = new File(parent_path.getAbsoluteFile(), "HAR");
        file_dir.mkdir();

        this.path = file_dir.getPath();

        File file = new File(this + "2017.csv");

        // file.createNewFile();
        byte[] buffer = raw_data.getBytes();

        // Error
        FileOutputStream fos = new FileOutputStream(file);

        fos.write(buffer, 0, buffer.length);
        fos.flush();
        fos.close();
    }

}
