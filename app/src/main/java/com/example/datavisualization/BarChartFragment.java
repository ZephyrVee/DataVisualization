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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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

    float groupSpace = 0.4f;
    float barSpace = 0.1f;
    float barWidth = 0.5f;

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
        if(getArguments() != null && getArguments().containsKey("tahun")){
            Bundle b = getArguments();
            String type = b.getString("tipe");
            int k = b.getInt("kategori");
            ArrayList<Integer> t = b.getIntegerArrayList("tahun");
            ArrayList<Integer> jk = b.getIntegerArrayList("jenis_kelamin");
            ArrayList<Integer> w = b.getIntegerArrayList("warna");
            if(type.equals("Multiple")){
                ArrayList<IBarDataSet> iBarDataSet = new ArrayList<>();
                for(int i = 0; i < t.size(); i++){
                    ArrayList<BarEntry> barEntry = new ArrayList<>();
                    ArrayList<Integer> dataSet;
                    if(jk.get(i) < 2){
                        dataSet = data.get(t.get(i), jk.get(i), k);
                    }
                    else {
                        dataSet = data.get(t.get(i), k);
                    }
                    for(int j = 0; j < dataSet.size(); j++){
                        barEntry.add(new BarEntry(j, dataSet.get(j)));
                    }
                    BarDataSet bds = new BarDataSet(barEntry, t.get(i).toString());
                    bds.setColor(w.get(i));
                    iBarDataSet.add(bds);
                }
                multiple(iBarDataSet, k);
            }
            else if(type.equals("Stacked")){
                ArrayList<IBarDataSet> iBarDataSet = new ArrayList<>();
                ArrayList<BarEntry> barEntry = new ArrayList<>();
                ArrayList<ArrayList<Integer>> stackedBarDataSet = new ArrayList<>();
                for(int i = 0; i < t.size(); i++){
                    stackedBarDataSet.add(data.get(t.get(i), jk.get(i), k));
                }
                for(int i = 0; i < DatasetKetenagakerjaanKabupaten.getSize(k); i++){
                    float[] stackedEntry = new float[stackedBarDataSet.size()];
                    for(int j = 0; j < stackedBarDataSet.size(); j++){
                        stackedEntry[j] = stackedBarDataSet.get(j).get(i);
                    }
                    barEntry.add(new BarEntry(i, stackedEntry));
                }
                int[] colors = new int[w.size()];
                for(int i = 0; i < w.size(); i++){
                    colors[i] = w.get(i);
                }
                BarDataSet barDataSet = new BarDataSet(barEntry, "Stacked Bar Chart");
                barDataSet.setColors(colors);
                barDataSet.setStackLabels(DatasetKetenagakerjaanKabupaten.getTableList(k));

                iBarDataSet.add(barDataSet);
                stacked(iBarDataSet, k);
            }
        }
        //setData();
        //visualize(k);
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

    private void multiple(ArrayList<IBarDataSet> iBarDataSet, int k){
        int dataSetCount = iBarDataSet.size();
        BarData barData = new BarData(iBarDataSet);
        if(iBarDataSet.size() > 1) {
            barData.setBarWidth(barWidth / dataSetCount);
        }

        BarChart barChart = getView().findViewById(R.id.barChart);
        barChart.setData(barData);

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(DatasetKetenagakerjaanKabupaten.getTableList(k)));
        xAxis.setAxisMaximum(DatasetKetenagakerjaanKabupaten.getSize(k));
        xAxis.setGranularityEnabled(true);

        barChart.setFitBars(true);
        barChart.getDescription().setText("Bar Chart");
        barChart.setDrawGridBackground(false);
        if(iBarDataSet.size() > 1){
            xAxis.setCenterAxisLabels(true);
            barChart.groupBars(0, groupSpace, barSpace / dataSetCount);
        }
        barChart.invalidate();
        barChart.animateY(1000);
    }
    private void stacked(ArrayList<IBarDataSet> iBarDataSet, int k){
        BarData barData = new BarData(iBarDataSet);
        barData.setValueTextColor(Color.WHITE);

        BarChart barChart = getView().findViewById(R.id.barChart);
        barChart.setData(barData);
        barChart.setDrawValueAboveBar(false);
        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(DatasetKetenagakerjaanKabupaten.getTableList(k)));
        xAxis.setAxisMaximum(DatasetKetenagakerjaanKabupaten.getSize(k));
        xAxis.setGranularityEnabled(true);

        barChart.setFitBars(false);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart");
        barChart.setDrawGridBackground(false);

        barChart.animateY(1000);
    }
}