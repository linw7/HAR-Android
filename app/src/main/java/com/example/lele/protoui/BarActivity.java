package com.example.lele.protoui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;


public class BarActivity extends AppCompatActivity {
    private BarChart bar_chart;
    private TextView sit_time;
    private TextView stand_time;
    private TextView upstairs_time;
    private TextView downstairs_time;
    private TextView walk_time;
    private TextView jog_time;

    private BarDataSet get_bar_set_1(){
        List<BarEntry> entry_list = new ArrayList<>();

        BarEntry entry_sit = new BarEntry(4, 0);
        BarEntry entry_stand = new BarEntry(2, 1);
        BarEntry entry_upstairs = new BarEntry(3, 2);
        BarEntry entry_downstairs = new BarEntry(6, 3);
        BarEntry entry_walk = new BarEntry(4, 4);
        BarEntry entry_jog = new BarEntry(7, 5);

        entry_list.add(entry_sit);
        entry_list.add(entry_stand);
        entry_list.add(entry_upstairs);
        entry_list.add(entry_downstairs);
        entry_list.add(entry_walk);
        entry_list.add(entry_jog);

        BarDataSet barSet = new BarDataSet(entry_list, "3月29日");
        barSet.setColor(Color.rgb(255, 241, 26));
        barSet.setDrawValues(true);
        return barSet;
    }

    private BarDataSet get_bar_set_2(){
        List<BarEntry> entry_list = new ArrayList<>();

        BarEntry entry_sit = new BarEntry(2, 0);
        BarEntry entry_stand = new BarEntry(5, 1);
        BarEntry entry_upstairs = new BarEntry(4, 2);
        BarEntry entry_downstairs = new BarEntry(5, 3);
        BarEntry entry_walk = new BarEntry(3, 4);
        BarEntry entry_jog = new BarEntry(1, 5);

        entry_list.add(entry_sit);
        entry_list.add(entry_stand);
        entry_list.add(entry_upstairs);
        entry_list.add(entry_downstairs);
        entry_list.add(entry_walk);
        entry_list.add(entry_jog);

        BarDataSet barSet = new BarDataSet(entry_list, "3月30日");
        barSet.setColor(Color.rgb(255, 21, 226));
        barSet.setDrawValues(true);
        return barSet;
    }

    private BarDataSet get_bar_set_3(){
        List<BarEntry> entry_list = new ArrayList<>();

        BarEntry entry_sit = new BarEntry(5, 0);
        BarEntry entry_stand = new BarEntry(1, 1);
        BarEntry entry_upstairs = new BarEntry(6, 2);
        BarEntry entry_downstairs = new BarEntry(3, 3);
        BarEntry entry_walk = new BarEntry(5, 4);
        BarEntry entry_jog = new BarEntry(3, 5);

        entry_list.add(entry_sit);
        entry_list.add(entry_stand);
        entry_list.add(entry_upstairs);
        entry_list.add(entry_downstairs);
        entry_list.add(entry_walk);
        entry_list.add(entry_jog);

        BarDataSet barSet = new BarDataSet(entry_list, "3月31日");
        barSet.setColor(Color.rgb(155, 241, 226));
        barSet.setDrawValues(true);
        return barSet;
    }


    private List<IBarDataSet> get_bar_list(){
        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(get_bar_set_1());
        dataSets.add(get_bar_set_2());
        dataSets.add(get_bar_set_3());
        return dataSets;
    }

    private BarData getBarData(){
        List<IBarDataSet> dataSets = get_bar_list();

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("静坐");
        xVals.add("站立");
        xVals.add("上楼");
        xVals.add("下楼");
        xVals.add("步行");
        xVals.add("慢跑");

        BarData barData = new BarData(xVals,dataSets);
        return barData;
    }

    private void data(){

    }

    private void show_data() {
        sit_time.setText("5分钟");
        stand_time.setText("1分钟");
        upstairs_time.setText("6分钟");
        downstairs_time.setText("3分钟");
        walk_time.setText("5分钟");
        jog_time.setText("3分钟");
    }

    private void draw_pic(){
        XAxis xAxis = bar_chart.getXAxis();//获取x轴
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.BLACK);

        bar_chart.getAxisRight().setEnabled(false);
        bar_chart.getAxisLeft().setAxisLineColor(Color.BLACK);

        bar_chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        bar_chart.setDescriptionColor(Color.WHITE);
        bar_chart.setDescription("");

        bar_chart.setData(getBarData());
        bar_chart.animateXY(2000,2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        bar_chart = (BarChart)findViewById(R.id.bar_chart);
        sit_time = (TextView)findViewById(R.id.sit_time);
        stand_time = (TextView)findViewById(R.id.stand_time);
        upstairs_time = (TextView)findViewById(R.id.upstairs_time);
        downstairs_time = (TextView)findViewById(R.id.downstairs_time);
        walk_time = (TextView)findViewById(R.id.walk_time);
        jog_time = (TextView)findViewById(R.id.jog_time);

        show_data();
        draw_pic();
    }
}
