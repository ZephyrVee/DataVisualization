package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class LineChartActivity extends AppCompatActivity {
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;

    ArrayList<Entry> entry = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            ArrayList<String> value = extras.getStringArrayList("table");

            for(int i = 0; i < value.size(); i++){
                String[] str = value.get(i).split(",");
                entry.add(new BarEntry(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
            }
        } else {
            entry.add(new Entry(1,2));
            entry.add(new Entry(2,3));
        }

        lineDataSet = new LineDataSet(entry, "entry");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setValueTextColor(Color.YELLOW);
        lineDataSet.setValueTextSize(16f);

        lineData = new LineData(lineDataSet);

        lineChart = findViewById(R.id.lineChart);
        lineChart.setData(lineData);
        lineChart.animate();

        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}