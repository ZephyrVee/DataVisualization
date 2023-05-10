package com.example.datavisualization;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class LineChartFragment extends Fragment {

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;

    ArrayList<Entry> entry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
        visualize();
    }

    private void setData(){
        entry = new ArrayList<>();

        entry.add(new Entry(2020,2));
        entry.add(new Entry(2021,3));
        entry.add(new Entry(2022,1));
        entry.add(new Entry(2023,1));

        lineDataSet = new LineDataSet(entry, "entry");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setValueTextColor(Color.YELLOW);
        lineDataSet.setValueTextSize(16f);

        lineData = new LineData(lineDataSet);
    }

    private void visualize(){
        lineChart = getView().findViewById(R.id.lineChart);
        lineChart.setData(lineData);
        lineChart.animate();
    }
}