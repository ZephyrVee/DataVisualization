package com.example.datavisualization;

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

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class ScatterChartFragment extends Fragment {
    DatasetKetenagakerjaanKabupaten data;

    int kategori = 0;
    String[] kategoriArray, jenisKelaminList, warnaArray, jenisKelaminLegendArray;
    Integer[] warnaList;
    Map<String, Integer> kategoriMap, jenisKelaminMap;

    ArrayList<Integer> tahunArrayList;
    ArrayList<IScatterDataSet> scatterDataSetArrayList;

    ScatterChart scatterChart;

    TextView ubahWarnaTitle;
    GridLayout ubahWarnaField;

    public ScatterChartFragment() {
        // Required empty public constructor
        jenisKelaminLegendArray = new String[]{"(L)", "(P)", "(L+P)"};
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scatter_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scatterChart = getView().findViewById(R.id.scatterChart);
        ubahWarnaTitle = getView().findViewById(R.id.chart_scatter_warna_title);
        ubahWarnaField = getView().findViewById(R.id.chart_scatter_warna_field);

        getBundle();
        initVar();
        initMap();

        set();
        initChart();
        refreshData();
    }

    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        tahunArrayList = new ArrayList<>();
        tahunArrayList = getArguments().getIntegerArrayList("tahun");
    }
    private void initVar(){
        data = MainActivity.database.data;
        scatterDataSetArrayList = new ArrayList<>();
        kategoriArray = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
        jenisKelaminList = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;
        warnaArray = new String[]{"Biru", "Cyan", "Abu-abu gelap", "Abu-abu", "Hijau", "Magenta", "Merah", "Kuning"};
        warnaList = new Integer[]{
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
        for(int i = 0; i < kategoriArray.length; i++){
            kategoriMap.put(kategoriArray[i], 1);
            ((GridLayout)getView().findViewById(R.id.chart_scatter_kategori_field)).addView(addCheckBox(kategoriArray[i], 1));
        }
        jenisKelaminMap = new HashMap<>();
        for(int i = 0; i < jenisKelaminList.length; i++){
            if(i == 2) {
                jenisKelaminMap.put(jenisKelaminList[i], 1);
            }
            else {
                jenisKelaminMap.put(jenisKelaminList[i], 0);
            }
            ((GridLayout)getView().findViewById(R.id.chart_scatter_jenis_kelamin_field)).addView(addCheckBox(jenisKelaminList[i], 0));

        }
        for(int i = 0; i < warnaArray.length; i++){
            ubahWarnaField.addView(addWarnaButton(i));
        }
        hideUbahWarna();
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
                set();
                refreshData();
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
        b.setText(warnaArray[index]);
        Drawable d = getResources().getDrawable(R.drawable.warna);
        d.mutate();
        d.setColorFilter(new PorterDuffColorFilter(warnaList[index], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        return b;
    }

    private void set(){
        scatterDataSetArrayList.clear();
        for(int i = 0; i < jenisKelaminList.length; i++){
            if(jenisKelaminMap.get(jenisKelaminList[i]) == 1){
                for(int k = 0; k < kategoriArray.length; k++){
                    if(kategoriMap.get(kategoriArray[k]) == 1){
                        ArrayList<Entry> entry = new ArrayList<>();
                        for(Integer t : tahunArrayList){
                            if(i == 2) {
                                entry.add(new Entry(t, (float)data.get(t, kategori).get(k)));
                            }
                            else {
                                entry.add(new Entry(t, (float)data.get(t, i, kategori).get(k)));
                            }
                        }

                        Collections.sort(entry, new Comparator<Entry>() {
                            @Override
                            public int compare(Entry e1, Entry e2) {
                                if(e1.getX() < e2.getX()){
                                    return -1;
                                }
                                else{
                                    return 0;
                                }
                            }
                        });

                        ScatterDataSet scatterDataSet = new ScatterDataSet(entry, kategoriArray[k] + " " + jenisKelaminLegendArray[i]);
                        scatterDataSet.setColor(warnaList[(i + k) % 8]);
                        scatterDataSet.setScatterShapeSize(40f);
                        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                        scatterDataSetArrayList.add(scatterDataSet);
                    }
                }
            }
        }
    }

    private void initChart(){
        Legend l = scatterChart.getLegend();
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        MarkerView mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(scatterChart); // For bounds control
        scatterChart.setMarker(mv);

        scatterChart.getAxisRight().setEnabled(false);
        scatterChart.getAxisLeft().setAxisMinimum(0f);
        scatterChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        scatterChart.getXAxis().setGranularityEnabled(true);
        scatterChart.setTouchEnabled(true);
        scatterChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                showUbahWarna(h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                hideUbahWarna();
            }
        });
    }
    private void refreshData(){
        ScatterData scatterData = new ScatterData(scatterDataSetArrayList);
        scatterData.setDrawValues(false);
        scatterChart.setData(scatterData);
        scatterChart.invalidate();
    }

    private void hideUbahWarna(){
        ((LinearLayout)getView().findViewById(R.id.chart_scatter_warna)).removeAllViews();
    }
    private void showUbahWarna(int dataSetIndex){
        LinearLayout ll = getView().findViewById(R.id.chart_scatter_warna);
        if(ll.getChildCount() == 0) {
            ll.addView(ubahWarnaTitle);
            ll.addView(ubahWarnaField);
        }
        for(int i = 0; i < ubahWarnaField.getChildCount(); i++){
            final int index = i;
            ubahWarnaField.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ScatterDataSet)scatterDataSetArrayList.get(dataSetIndex)).setColor(warnaList[index]);
                    refreshData();
                    hideUbahWarna();
                }
            });
        }
    }
}