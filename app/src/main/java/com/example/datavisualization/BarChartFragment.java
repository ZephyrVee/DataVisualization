package com.example.datavisualization;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BarChartFragment extends Fragment {
    DatasetKetenagakerjaanKabupaten data;

    //Bundle variables
    int kategori;
    ArrayList<Integer> tahunArrayList, stackedColor;
    //Bundle variables

    String type = "Multiple";

    String[] kategoriList, jenisKelaminList, warnaList;
    Integer[] warnaArrayList;
    Map<String, Integer> jenisKelaminMap;

    ArrayList<IBarDataSet> multipleBarDataSetArrayList, stackedBarDataSetArrayList;

    TextView ubahWarnaTitle;
    GridLayout ubahWarnaField;
    BarChart barChart;

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
        barChart = getView().findViewById(R.id.barChart);
        ubahWarnaTitle = getView().findViewById(R.id.chart_bar_warna_title);
        ubahWarnaField = getView().findViewById(R.id.chart_bar_warna_field);
        getView().findViewById(R.id.chart_bar_multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Multiple";
                refreshData(type);
            }
        });
        getView().findViewById(R.id.chart_bar_stacked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Stacked";
                refreshData(type);
            }
        });

        getBundle();
        initVar();
        initMap();
        set();
        refreshData(type);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        tahunArrayList = new ArrayList<>();
        tahunArrayList = getArguments().getIntegerArrayList("tahun");
    }
    private void initVar(){
        data = MainActivity.database.data;
        kategoriList = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
        jenisKelaminList = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;
        warnaList = new String[]{"Biru", "Cyan", "Abu-abu gelap", "Abu-abu", "Hijau", "Magenta", "Merah", "Kuning"};
        warnaArrayList = new Integer[]{
                Color.BLUE,
                Color.CYAN,
                Color.DKGRAY,
                Color.GRAY,
                Color.GREEN,
                Color.MAGENTA,
                Color.RED,
                Color.YELLOW
        };
    }
    private void initMap(){
        jenisKelaminMap = new HashMap<>();
        for(int i = 0; i < jenisKelaminList.length; i++){
            if(i == 2) {
                jenisKelaminMap.put(jenisKelaminList[i], 1);
            }
            else {
                jenisKelaminMap.put(jenisKelaminList[i], 0);
            }
            ((GridLayout)getView().findViewById(R.id.chart_bar_jenis_kelamin_field)).addView(addCheckBox(jenisKelaminList[i]));

        }
        for(int i = 0; i < warnaList.length; i++){
            ubahWarnaField.addView(addWarnaButton(i));
        }
        hideUbahWarna();
    }
    private CheckBox addCheckBox(String text){
        GridLayout.LayoutParams cbpr = new GridLayout.LayoutParams();
        cbpr.setMargins(8,8,8,8);

        CheckBox cb = new CheckBox(getContext());
        cb.setLayoutParams(cbpr);
        cb.setText(text);
        cb.setTextSize(10);
        cb.setTextColor(Color.BLACK);
        if(jenisKelaminMap.get(text) == 1){
            cb.setChecked(true);
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    jenisKelaminMap.put(text, 1);
                }
                else{
                    jenisKelaminMap.put(text, 0);
                }
                set();
                refreshData(type);
            }
        });
        return cb;
    }
    private Button addWarnaButton(int index){
        GridLayout.LayoutParams bpr = new GridLayout.LayoutParams();
        bpr.setMargins(8,8,8,8);

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setTextSize(12);
        b.setTextColor(Color.BLACK);
        b.setText(warnaList[index]);
        Drawable d = getResources().getDrawable(R.drawable.warna);
        d.mutate();
        d.setColorFilter(new PorterDuffColorFilter(warnaArrayList[index], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        return b;
    }

    private void set(){
        multipleBarDataSetArrayList = new ArrayList<>();
        for(Integer t : tahunArrayList){
            for(int i = 0; i < jenisKelaminList.length; i++){
                if(jenisKelaminMap.get(jenisKelaminList[i]) == 1){
                    ArrayList<BarEntry> barEntry = new ArrayList<>();
                    ArrayList<Integer> dataEntry;
                    if(i == 2){
                        dataEntry = data.get(t, kategori);
                    }
                    else {
                        dataEntry = data.get(t, i, kategori);
                    }
                    for(int k = 0; k < dataEntry.size(); k++){
                        barEntry.add(new BarEntry(k, dataEntry.get(k)));
                    }
                    BarDataSet barDataSet = new BarDataSet(barEntry, jenisKelaminList[i] + " " + t);
                    barDataSet.setColor(warnaArrayList[(i + t) % 8]);
                    multipleBarDataSetArrayList.add(barDataSet);
                }
            }
        }

        stackedBarDataSetArrayList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> stackedBarDataSet = new ArrayList<>();
        ArrayList<BarEntry> barEntry = new ArrayList<>();
        stackedColor = new ArrayList<>();
        String[] labels = new String[kategoriList.length];
        int stackedIndex = 0;
        for(Integer t : tahunArrayList){
            for(int i = 0; i < jenisKelaminList.length; i++) {
                if (jenisKelaminMap.get(jenisKelaminList[i]) == 1) {
                    if(i == 2){
                        stackedBarDataSet.add(data.get(t, kategori));
                    }
                    else {
                        stackedBarDataSet.add(data.get(t, i, kategori));
                    }
                    labels[stackedIndex] = jenisKelaminList[i] + " " + t;
                    stackedColor.add(warnaArrayList[stackedIndex % 8]);
                    stackedIndex++;
                }
            }
        }
        for(int i = 0; i < kategoriList.length; i++){
            float[] barEntryInteger = new float[stackedBarDataSet.size()];
            for(int j = 0; j < stackedBarDataSet.size(); j++){
                barEntryInteger[j] = stackedBarDataSet.get(j).get(i);
            }
            barEntry.add(new BarEntry(i, barEntryInteger));
        }
        BarDataSet barDataSetStacked = new BarDataSet(barEntry, "");
        barDataSetStacked.setStackLabels(labels);
        barDataSetStacked.setColors(stackedColor);
        stackedBarDataSetArrayList.add(barDataSetStacked);
    }

    private void multiple(){
        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        Legend l = barChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(DatasetKetenagakerjaanKabupaten.getTableList(kategori)));
        xAxis.setAxisMaximum(DatasetKetenagakerjaanKabupaten.getSize(kategori));
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(330f);

        barChart.setFitBars(true);
        barChart.getDescription().setText("Bar Chart");
        barChart.setDrawGridBackground(false);
        if(multipleBarDataSetArrayList.size() > 1){
            xAxis.setCenterAxisLabels(true);
            barChart.groupBars(0, groupSpace, barSpace / multipleBarDataSetArrayList.size());
        }
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int dataSetIndex = h.getDataSetIndex();
                showUbahWarna(dataSetIndex, type);
            }

            @Override
            public void onNothingSelected() {
                hideUbahWarna();
            }
        });

        barChart.invalidate();
        barChart.animateY(1000);
    }
    private void stacked(){
        barChart.getBarData().setValueTextColor(Color.WHITE);

        Legend l = barChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        barChart.setDrawValueAboveBar(false);
        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(DatasetKetenagakerjaanKabupaten.getTableList(kategori)));
        xAxis.setAxisMaximum(DatasetKetenagakerjaanKabupaten.getSize(kategori));
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(45f);

        barChart.setFitBars(false);
        barChart.getDescription().setText("Bar Chart");
        barChart.setDrawGridBackground(false);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int dataSetIndex = h.getStackIndex();
                showUbahWarna(dataSetIndex, type);
            }

            @Override
            public void onNothingSelected() {
                hideUbahWarna();
            }
        });

        barChart.invalidate();
        barChart.animateY(1000);
    }

    private void refreshData(String type){
        if(type.equals("Multiple")) {
            BarData barData = new BarData(multipleBarDataSetArrayList);
            if(multipleBarDataSetArrayList.size() > 1) {
                barData.setBarWidth(barWidth / multipleBarDataSetArrayList.size());
            }
            barChart.setData(barData);
            multiple();
        }
        else {
            BarData barData = new BarData(stackedBarDataSetArrayList);
            barChart.setData(barData);
            stacked();
        }
    }

    private void hideUbahWarna(){
        ((LinearLayout)getView().findViewById(R.id.chart_bar_warna)).removeAllViews();
    }
    private void showUbahWarna(int dataSetIndex, String type){
        LinearLayout ll = getView().findViewById(R.id.chart_bar_warna);
        if(ll.getChildCount() == 0) {
            ll.addView(ubahWarnaTitle);
            ll.addView(ubahWarnaField);
        }
        for(int i = 0; i < ubahWarnaField.getChildCount(); i++){
            final int index = i;
            ubahWarnaField.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type.equals("Multiple")){
                        ((BarDataSet)multipleBarDataSetArrayList.get(dataSetIndex)).setColor(warnaArrayList[index]);
                    }
                    else{
                        stackedColor.set(dataSetIndex, warnaArrayList[index]);
                        ((BarDataSet)stackedBarDataSetArrayList.get(0)).setColors(stackedColor);
                    }
                    refreshData(type);
                    hideUbahWarna();
                }
            });
        }
    }

}