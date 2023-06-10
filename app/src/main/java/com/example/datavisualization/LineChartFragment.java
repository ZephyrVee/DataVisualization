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
    String[] kategoriList, jenisKelaminList;
    Map<String, Integer> kategoriMap, jenisKelaminMap;

    ArrayList<Integer> tahunArrayList;
    ArrayList<ILineDataSet> lineDataSetArrayList;

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

        getBundle();
        initVar();
        initMap();
    }

    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        tahunArrayList = new ArrayList<>();
        tahunArrayList = getArguments().getIntegerArrayList("tahun");
    }
    private void initVar(){
        lineDataSetArrayList = new ArrayList<>();
        kategoriList = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
        jenisKelaminList = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;
    }
    private void initMap(){
        kategoriMap = new HashMap<>();
        for(int i = 0; i < kategoriList.length; i++){
            kategoriMap.put(kategoriList[i], 1);
            ((GridLayout)getView().findViewById(R.id.chart_line_kategori_field)).addView(addCheckBox(kategoriList[i], 1));
        }
        jenisKelaminMap = new HashMap<>();
        for(int i = 0; i < jenisKelaminList.length; i++){
            jenisKelaminMap.put(jenisKelaminList[i], 1);
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
        cb.setChecked(true);
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
        ArrayList<ArrayList<Integer>> dataArray = new ArrayList<>();
        ArrayList<String> labelArray = new ArrayList<>();
        for(Integer t : tahunArrayList){
            for(int i = 0; i < jenisKelaminList.length; i++){
                String jk = jenisKelaminList[i];
                if(jenisKelaminMap.get(jk) == 1){
                    if(i == 2){

                    }
                    else {

                    }

                    for(int j = 0; j < kategoriList.length; j++){
                        String k = kategoriList[j];
                        if(kategoriMap.get(k) == 1) {
                            if (i == 2) {
                                dataArray.add(data.get(t, j));
                            } else {
                                dataArray.add(data.get(t, i, j));
                            }
                        }
                    }
                }
            }
        }
        for(int i = 0; i < kategoriList.length; i++){
            ArrayList<Entry> entry = new ArrayList<>();
            for(int j = 0; j < dataArray.size(); j++){
                entry.add(new Entry(j, dataArray.get(j).get(i)));
            }
            LineDataSet lineDataSet = new LineDataSet(entry, "");
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

        lineData = new LineData(lineDataSet);
    }

    private void visualize(){
        lineChart = getView().findViewById(R.id.lineChart);
        lineChart.setData(lineData);
        lineChart.animate();
    }
}