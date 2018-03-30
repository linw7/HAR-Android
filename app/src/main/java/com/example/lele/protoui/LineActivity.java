package com.example.lele.protoui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;


public class LineActivity extends AppCompatActivity {
    private LineChart line_chart;

    /*
    private XAxis xAxis;
    private YAxis yAxis;
    private YAxis yAxisR;

    private void show_line_chart(LineChart line_chart, LineData line_data) {
        // 右下角图表描述、颜色、位置、字体、大小
        line_chart.setDescription("行为");
        line_chart.setDescriptionColor(Color.BLACK);

        // 是否显示格子背景
        line_chart.setDrawGridBackground(true);

        // 设置背景色
        line_chart.setBackgroundColor(Color.WHITE);

        // 是否可触摸、拖拽、缩放、同时缩放、自动缩放
        line_chart.setTouchEnabled(true);
        line_chart.setDragEnabled(true);
        line_chart.setScaleEnabled(true);
        line_chart.setPinchZoom(true);
        line_chart.setDoubleTapToZoomEnabled(true);
        line_chart.setAutoScaleMinMaxEnabled(true);

        yAxis.resetAxisMaxValue();
        yAxis.resetAxisMinValue();
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        xAxis.resetAxisMinValue();
        xAxis.resetAxisMaxValue();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        line_chart.setData(line_data);
        line_chart.animateX(100);
        line_chart.invalidate();
    }

    private LineData get_line_data() {
        // 生成x轴数据
        ArrayList<String> xVal = new ArrayList<>();
        xVal.add("" + 0);
        xVal.add("" + 1);
        xVal.add("" + 2);
        xVal.add("" + 3);
        xVal.add("" + 4);
        xVal.add("" + 5);
        xVal.add("" + 6);
        xVal.add("" + 7);

        // 生成y轴数据
        ArrayList<Entry> yVal = new ArrayList<>();
        yVal.add(new Entry(3, 1));
        yVal.add(new Entry(7, 2));
        yVal.add(new Entry(2, 3));
        yVal.add(new Entry(5, 4));
        yVal.add(new Entry(4, 5));
        yVal.add(new Entry(6, 6));

        LineDataSet set1 = new LineDataSet(yVal, "Data");
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

        // 设置线宽和颜色
        set1.setLineWidth(1f);
        set1.setColor(Color.rgb(244, 117, 117));

        // 设置点数据半径和颜色
        set1.setCircleRadius(2f);
        set1.setCircleColor(Color.RED);

        // 焦点线
        set1.setHighLightColor(Color.RED);

        // 是否显示值
        set1.setDrawValues(true);
        LineData data = new LineData(xVal, set1);
        return data;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        line_chart = (LineChart)findViewById(R.id.line_chart);

        /*
        xAxis = line_chart.getXAxis();
        yAxis = line_chart.getAxisLeft();
        yAxisR = line_chart.getAxisRight();
        LineData line_data = get_line_data();
        show_line_chart(line_chart, line_data);
        */
    }
}
