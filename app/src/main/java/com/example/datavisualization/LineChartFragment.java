package com.example.datavisualization;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LineChartFragment extends Fragment {
    DatasetKetenagakerjaanKabupaten data;

    int kategori = 0;
    String[] kategoriList, jenisKelaminList, warnaList;
    Integer[] warnaArrayList;
    Map<String, Integer> kategoriMap, jenisKelaminMap;

    ArrayList<Integer> tahunArrayList;
    ArrayList<ILineDataSet> lineDataSetArrayList;

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;

    ArrayList<Entry> entry;

    public LineChartFragment(){
    }

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
        lineChart = getView().findViewById(R.id.lineChart);

        getBundle();
        initVar();
        initMap();

        set();
        visualize();
    }

    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        tahunArrayList = new ArrayList<>();
        tahunArrayList = getArguments().getIntegerArrayList("tahun");
    }
    private void initVar(){
        data = MainActivity.database.data;
        lineDataSetArrayList = new ArrayList<>();
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
        kategoriMap = new HashMap<>();
        for(int i = 0; i < kategoriList.length; i++){
            kategoriMap.put(kategoriList[i], 1);
            ((GridLayout)getView().findViewById(R.id.chart_line_kategori_field)).addView(addCheckBox(kategoriList[i], 1));
        }
        jenisKelaminMap = new HashMap<>();
        for(int i = 0; i < jenisKelaminList.length; i++){
            if(i == 2) {
                jenisKelaminMap.put(jenisKelaminList[i], 1);
            }
            else {
                jenisKelaminMap.put(jenisKelaminList[i], 0);
            }
            ((GridLayout)getView().findViewById(R.id.chart_line_jenis_kelamin_field)).addView(addCheckBox(jenisKelaminList[i], 0));

        }
    }
    private CheckBox addCheckBox(String text, int field){
        GridLayout.LayoutParams cbpr = new GridLayout.LayoutParams();
        cbpr.setMargins(8,8,8,8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cbpr.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        }

        CheckBox cb = new CheckBox(getContext());
        cb.setLayoutParams(cbpr);
        cb.setText(text);
        cb.setTextSize(10);
        cb.setTextColor(Color.BLACK);
        if(field == 0){
            if(jenisKelaminMap.get(text) == 1){
                cb.setChecked(true);
            }
        }
        else if(field == 1){
            if(kategoriMap.get(text) == 1){
                cb.setChecked(true);
            }
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switch(field){
                        case 0:
                            jenisKelaminMap.put(text, 1);
                            break;
                        case 1:
                            kategoriMap.put(text, 1);
                            break;
                    }
                }
                else{
                    switch(field){
                        case 0:
                            jenisKelaminMap.put(text, 0);
                            break;
                        case 1:
                            kategoriMap.put(text, 0);
                            break;
                    }
                }
            }
        });
        return cb;
    }

    private void set(){
        for(int i = 0; i < jenisKelaminList.length; i++){
            if(jenisKelaminMap.get(jenisKelaminList[i]) == 1){
                for(int k = 0; k < kategoriList.length; k++){
                    if(kategoriMap.get(kategoriList[k]) == 1){
                        ArrayList<Entry> entry = new ArrayList<>();
                        for(Integer t: tahunArrayList){
                            if(i == 2) {
                                entry.add(new Entry(t, data.get(t, kategori).get(k)));
                            }
                            else {
                                entry.add(new Entry(t, data.get(t, i, kategori).get(k)));
                            }
                        }
                        LineDataSet lineDataSet = new LineDataSet(entry, jenisKelaminList[i] + " " + kategoriList[k]);
                        int c = (i + k) % warnaList.length;
                        lineDataSet.setColor(warnaArrayList[c]);
                        lineDataSet.setCircleColor(Color.BLACK);
                        lineDataSetArrayList.add(lineDataSet);
                    }
                }
            }
        }
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

    }

    private void visualize(){
        LineData lineData = new LineData(lineDataSetArrayList);
        lineChart.setData(lineData);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.invalidate();
    }
}