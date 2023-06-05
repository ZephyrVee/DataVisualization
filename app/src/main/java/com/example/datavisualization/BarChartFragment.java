package com.example.datavisualization;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartFragment extends Fragment {
    DatasetKetenagakerjaanKabupaten data;

    BarDataSet barDataSet;
    BarData barData;
    BarChart barChart;

    ArrayList<IBarDataSet> barDataSets;

    ArrayList<BarEntry> visitors;

    public BarChartFragment(){
        data = MainActivity.database.data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_bar_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barDataSets = new ArrayList<>();
        if(getArguments() != null){
            Bundle b = getArguments();
            int k = b.getInt("kategori");
            ArrayList<Integer> t = b.getIntegerArrayList("tahun");
            ArrayList<Integer> jk = b.getIntegerArrayList("jenis_kelamin");
            ArrayList<Integer> w = b.getIntegerArrayList("warna");
            for(int i = 0; i < t.size(); i++){
                ArrayList<Integer> al = data.get(t.get(i), jk.get(i), k);
                ArrayList<BarEntry> entries = new ArrayList<>();
                for(int j = 0; j < al.size(); j++){
                    entries.add(new BarEntry(j, al.get(j)));
                }
                BarDataSet bds = new BarDataSet(entries, t.get(i).toString());
                bds.setColor(w.get(i));
                barDataSets.add(bds);
            }
            barData = new BarData(barDataSets);
        }
        //setData();
        visualize();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setData(){
        visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014, 420));
        visitors.add(new BarEntry(2015, 475));
        visitors.add(new BarEntry(2016, 508));
        visitors.add(new BarEntry(2017, 550));
        visitors.add(new BarEntry(2018, 650));
        visitors.add(new BarEntry(2019, 470));

        barDataSet = new BarDataSet(visitors, "Visitors");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        barData = new BarData(barDataSet);
    }

    private void visualize(){
        barChart = getView().findViewById(R.id.barChart);

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setFitBars(false);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart");
        if(!barDataSets.isEmpty()){
            if(barDataSets.size() > 1){
                barChart.groupBars(0, 0.5f, 0.03f);
            }
        }

        barChart.invalidate();
        barChart.animateY(1000);
    }
}