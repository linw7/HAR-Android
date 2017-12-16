package com.example.lele.protoui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private LineChart line_chart;
    private XAxis xAxis;
    private YAxis yAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        line_chart = (LineChart)findViewById(R.id.line_chart);
        xAxis = line_chart.getXAxis();
        yAxis = line_chart.getAxisLeft();

        // 获取显示数据
        LineData line_data = get_line_data();
        // 其他设置
        show_chart(line_chart, line_data);
    }
    private void show_chart(LineChart line_chart, LineData line_data) {
        // 图表概览
        // 右下角图表描述、颜色、位置、字体、大小
        line_chart.setDescription("HAR");
        line_chart.setDescriptionColor(Color.rgb(227, 135, 0));
        //  line_chart.setDescriptionPosition(400f,600f);
        // line_chart.setDescriptionTypeface();
        line_chart.setDescriptionTextSize(8);
        // line_chart.setNoDataTextDescription("没有数据呢(⊙o⊙)");

        // 是否显示格子背景
        // line_chart.setDrawGridBackground(false);
        // 设置背景色
        line_chart.setBackgroundColor(Color.WHITE);
        // 边框
        line_chart.setDrawBorders(true);
        line_chart.setBorderColor(Color.rgb(57, 135, 200));   //上面的边框颜色
        line_chart.setBorderWidth(2);       //上面边框的宽度，float类型，dp单位

        // 交互设置
        // 是否可触摸
        line_chart.setTouchEnabled(true);
        // 是否可拖拽
        line_chart.setDragEnabled(true);
        // 是否可缩放
        line_chart.setScaleEnabled(true);
        // 是否可同时缩放
        line_chart.setPinchZoom(true);
        // 是否双击方法图表
        line_chart.setDoubleTapToZoomEnabled(true);
        // 是否自动缩放
        line_chart.setAutoScaleMinMaxEnabled(true);

        line_chart.setViewPortOffsets(10, 0, 10, 0);
        line_chart.setData(line_data);

        Legend l = line_chart.getLegend();
        l.setEnabled(false);
        line_chart.getAxisLeft().setEnabled(true);
        line_chart.getAxisLeft().setSpaceTop(40);
        line_chart.getAxisRight().setEnabled(true);
        line_chart.getAxisLeft().setSpaceBottom(40);
        line_chart.getXAxis().setEnabled(true);
        line_chart.animateX(2500);
    }

    private LineData get_line_data() {
        ArrayList<String> xVal = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            xVal.add("" + i);

        ArrayList<Entry> yVal = new ArrayList<>();
        yVal.add(new Entry(6, 0));
        yVal.add(new Entry(13, 1));
        yVal.add(new Entry(6, 2));
        yVal.add(new Entry(3, 3));
        yVal.add(new Entry(7, 4));
        yVal.add(new Entry(2, 5));
        yVal.add(new Entry(5, 6));
        yVal.add(new Entry(12, 7));
        yVal.add(new Entry(2, 8));
        yVal.add(new Entry(5, 9));

        LineDataSet set1 = new LineDataSet(yVal, "Data");

        // 设置线宽和颜色
        set1.setLineWidth(1.75f);
        set1.setColor(Color.BLACK);

        // 设置点数据半径和颜色
        set1.setCircleRadius(5f);
        set1.setCircleColor(Color.BLACK);

        // 焦点线
        set1.setHighLightColor(Color.RED);

        // 是否显示值
        set1.setDrawValues(true);

        LineData data = new LineData(xVal, set1);

        return data;
    }
}
