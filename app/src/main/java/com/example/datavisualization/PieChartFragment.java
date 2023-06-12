package com.example.datavisualization;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class PieChartFragment extends Fragment {
    final int DEFAULT_JENIS_KELAMIN = 2;

    PieChart pieChart;
    PieDataSet pieDataSet;

    DatasetKetenagakerjaanKabupaten data;
    int kategori, tahun, jenisKelamin;

    String[] kategoriArray, warnaNameArray;
    Integer[] warnaArray;

    ArrayList<Integer> warnaList;


    public PieChartFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        jenisKelamin = DEFAULT_JENIS_KELAMIN;
        warnaNameArray = new String[]{"Biru", "Cyan", "Abu-abu gelap", "Abu-abu", "Hijau", "Magenta", "Merah", "Kuning"};
        warnaArray = new Integer[]{
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = getView().findViewById(R.id.pieChart);
        getBundle();

        initChart();
        set();
        refreshData();
    }

    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        tahun = getArguments().getInt("tahun");

        kategoriArray = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
    }
    private void set(){
        ArrayList<PieEntry> pieEntry = new ArrayList<>();
        ArrayList<Integer> dataEntry;

        warnaList = new ArrayList<>();

        if(jenisKelamin == 2){
            dataEntry = data.get(tahun, kategori);
        }
        else {
            dataEntry = data.get(tahun, jenisKelamin, kategori);
        }
        for(int i = 0; i < dataEntry.size(); i++){
            pieEntry.add(new PieEntry(dataEntry.get(i), kategoriArray[i]));
            warnaList.add(warnaArray[i % 8]);
        }
        pieDataSet = new PieDataSet(pieEntry, kategoriArray[kategori] + " " + tahun);
        pieDataSet.setColors(warnaList);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setSliceSpace(2f);
    }

    private void initChart(){
        Legend l = pieChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40);
    }
    private void refreshData(){
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.animate();
    }
}