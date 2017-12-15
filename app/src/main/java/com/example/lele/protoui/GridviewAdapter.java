package com.example.lele.protoui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by LeLe on 2017/8/25.
 */
public class GridviewAdapter extends SimpleAdapter {
    private Context mContext;
    private int[] mResult;//保存选择数据

    public GridviewAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int[] result) {
        super(context, data, resource, from, to);
        this.mContext = context;
        this.mResult = result;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.grid_image_cell, null);
        }
        if (mResult[position] == 1) {
            LinearLayout linearlayout = (LinearLayout) convertView.findViewById(R.id.grid_image_cell_layout);
            linearlayout.setBackgroundResource(R.drawable.editor_rec);
        }
        return super.getView(position, convertView, parent);
    }
}
