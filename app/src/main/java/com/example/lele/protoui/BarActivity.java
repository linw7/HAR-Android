package com.example.lele.protoui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    public DemoData demodata = new DemoData();

    int sit_yy, stand_yy, upstairs_yy, downstairs_yy, walk_yy, jog_yy;
    int sit_y, stand_y, upstairs_y, downstairs_y, walk_y, jog_y;
    int sit_t, stand_t, upstairs_t, downstairs_t, walk_t, jog_t;

    public ArrayList<ArrayList> get_data(int day, int month){
        OfflineFileRW rw = new OfflineFileRW();
        ArrayList yes_yesterday = new ArrayList();
        ArrayList yesterday = new ArrayList();
        ArrayList today = new ArrayList();
        ArrayList<ArrayList> data = new ArrayList<ArrayList>();

        data.add(yes_yesterday);
        data.add(yesterday);
        data.add(today);
        return data;
    }

    private BarDataSet get_bar_set_1(){
        List<BarEntry> entry_list = new ArrayList<>();

        BarEntry entry_sit = new BarEntry(sit_yy, 0);
        BarEntry entry_stand = new BarEntry(stand_yy, 1);
        BarEntry entry_upstairs = new BarEntry(upstairs_yy, 2);
        BarEntry entry_downstairs = new BarEntry(downstairs_yy, 3);
        BarEntry entry_walk = new BarEntry(walk_yy, 4);
        BarEntry entry_jog = new BarEntry(jog_yy, 5);

        entry_list.add(entry_sit);
        entry_list.add(entry_stand);
        entry_list.add(entry_upstairs);
        entry_list.add(entry_downstairs);
        entry_list.add(entry_walk);
        entry_list.add(entry_jog);

        BarDataSet barSet = new BarDataSet(entry_list, "前两日数据");
        barSet.setColor(Color.rgb(255, 241, 26));
        barSet.setDrawValues(true);
        return barSet;
    }

    private BarDataSet get_bar_set_2(){
        List<BarEntry> entry_list = new ArrayList<>();

        BarEntry entry_sit = new BarEntry(sit_y, 0);
        BarEntry entry_stand = new BarEntry(stand_y, 1);
        BarEntry entry_upstairs = new BarEntry(upstairs_y, 2);
        BarEntry entry_downstairs = new BarEntry(downstairs_y, 3);
        BarEntry entry_walk = new BarEntry(walk_y, 4);
        BarEntry entry_jog = new BarEntry(jog_y, 5);

        entry_list.add(entry_sit);
        entry_list.add(entry_stand);
        entry_list.add(entry_upstairs);
        entry_list.add(entry_downstairs);
        entry_list.add(entry_walk);
        entry_list.add(entry_jog);

        BarDataSet barSet = new BarDataSet(entry_list, "前一日数据");
        barSet.setColor(Color.rgb(255, 21, 226));
        barSet.setDrawValues(true);
        return barSet;
    }

    private BarDataSet get_bar_set_3(){
        List<BarEntry> entry_list = new ArrayList<>();

        BarEntry entry_sit = new BarEntry(sit_t, 0);
        BarEntry entry_stand = new BarEntry(stand_t, 1);
        BarEntry entry_upstairs = new BarEntry(upstairs_t, 2);
        BarEntry entry_downstairs = new BarEntry(downstairs_t, 3);
        BarEntry entry_walk = new BarEntry(walk_t, 4);
        BarEntry entry_jog = new BarEntry(jog_t, 5);

        entry_list.add(entry_sit);
        entry_list.add(entry_stand);
        entry_list.add(entry_upstairs);
        entry_list.add(entry_downstairs);
        entry_list.add(entry_walk);
        entry_list.add(entry_jog);

        BarDataSet barSet = new BarDataSet(entry_list, "今日数据");
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

    private void get_data(){
        demodata.set_data();
        ArrayList<ArrayList> data = demodata.get_elem(HistoryMainActivity.c_day);

        ArrayList yes_yesterday =  data.get(0);
        ArrayList yesterday = data.get(1);
        ArrayList today = data.get(2);

        sit_yy = Integer.valueOf(yes_yesterday.get(0).toString()).intValue();
        sit_y = Integer.valueOf(yesterday.get(0).toString()).intValue();
        sit_t = Integer.valueOf(today.get(0).toString()).intValue();

        stand_yy = Integer.valueOf(yes_yesterday.get(1).toString()).intValue();
        stand_y = Integer.valueOf(yesterday.get(1).toString()).intValue();
        stand_t = Integer.valueOf(today.get(1).toString()).intValue();

        upstairs_yy = Integer.valueOf(yes_yesterday.get(2).toString()).intValue();
        upstairs_y = Integer.valueOf(yesterday.get(2).toString()).intValue();
        upstairs_t = Integer.valueOf(today.get(2).toString()).intValue();

        downstairs_yy = Integer.valueOf(yes_yesterday.get(3).toString()).intValue();
        downstairs_y = Integer.valueOf(yesterday.get(3).toString()).intValue();
        downstairs_t = Integer.valueOf(today.get(3).toString()).intValue();

        walk_yy = Integer.valueOf(yes_yesterday.get(4).toString()).intValue();
        walk_y = Integer.valueOf(yesterday.get(4).toString()).intValue();
        walk_t = Integer.valueOf(today.get(4).toString()).intValue();

        jog_yy = Integer.valueOf(yes_yesterday.get(5).toString()).intValue();
        jog_y = Integer.valueOf(yesterday.get(5).toString()).intValue();
        jog_t = Integer.valueOf(today.get(5).toString()).intValue();
    }

    private void show_data() {
        sit_time.setText(sit_yy + sit_y + sit_t + "分钟");
        stand_time.setText(stand_yy + stand_y + stand_t + "分钟");
        upstairs_time.setText(upstairs_yy + upstairs_y + upstairs_t + "分钟");
        downstairs_time.setText(downstairs_yy + downstairs_y + downstairs_t + "分钟");
        walk_time.setText(walk_yy + walk_y + walk_t + "分钟");
        jog_time.setText(jog_yy + jog_y + jog_t + "分钟");
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

        get_data();
        show_data();
        draw_pic();
    }
}
