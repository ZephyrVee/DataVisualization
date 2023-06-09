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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LineChartFragment extends Fragment {
    DatasetKetenagakerjaanKabupaten data;

    int kategori = 0;
    String[] kategoriList, jenisKelaminList, warnaNamaArray, jenisKelaminLegendArray;
    int[] warnaArray;
    Map<String, Integer> kategoriMap, jenisKelaminMap;

    ArrayList<Integer> tahunArrayList;
    ArrayList<ILineDataSet> lineDataSetArrayList;

    LineChart lineChart;

    TextView ubahWarnaTitle;
    GridLayout ubahWarnaField;

    public LineChartFragment(){
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
        return inflater.inflate(R.layout.fragment_line_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChart = getView().findViewById(R.id.scatterChart);
        ubahWarnaTitle = getView().findViewById(R.id.chart_scatter_warna_title);
        ubahWarnaField = getView().findViewById(R.id.chart_scatter_warna_field);

        getBundle();
        initVar();
        initMap();

        set();
        initChart();
        refreshData();

        ((TextView)getView().findViewById(R.id.chart_line_title)).setText(getContext().getResources().getStringArray(R.array.chart_title)[kategori]);

        ImageButton ib = getView().findViewById(R.id.chart_keterangan_button);
        if(kategori == DatasetKetenagakerjaanKabupaten.UMUR || kategori == DatasetKetenagakerjaanKabupaten.PENDIDIKAN){
            LinearLayout ll = getView().findViewById(R.id.chart_keterangan_field);
            ll.removeAllViews();
        }
        else{
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll = getView().findViewById(R.id.chart_adjustment);
                    TextView tv = getView().findViewById(R.id.chart_keterangan);
                    if(ll.getVisibility() == View.VISIBLE){
                        ll.setVisibility(View.INVISIBLE);
                        tv.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                        String s = "";
                        String[] s1 = DatasetKetenagakerjaanKabupaten.getList(kategori);
                        String[] s2 = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
                        for(int i = 0; i < s1.length; i++){
                            s += s2[i] +": " +s1[i] + System.getProperty("line.separator");
                        }
                        tv.setText(s);
                        ib.setImageDrawable(getResources().getDrawable(R.drawable.icon_remove));
                    }
                    else {
                        ll.setVisibility(View.VISIBLE);
                        tv.setText("");
                        tv.getLayoutParams().height = 0;
                        ib.setImageDrawable(getResources().getDrawable(R.drawable.icon_add));
                    }
                }
            });
        }
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
        warnaNamaArray = getContext().getResources().getStringArray(R.array.chart_warna_nama_array);
        warnaArray = getContext().getResources().getIntArray(R.array.chart_warna_array);
    }
    private void initMap(){
        kategoriMap = new HashMap<>();
        for(int i = 0; i < kategoriList.length; i++){
            kategoriMap.put(kategoriList[i], 1);
            ((GridLayout)getView().findViewById(R.id.chart_scatter_kategori_field)).addView(addCheckBox(kategoriList[i], 1));
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
        for(int i = 0; i < warnaNamaArray.length; i++){
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
        b.setBackground(getResources().getDrawable(R.drawable.background_white_button_selector));
        b.setTextSize(12);
        b.setTextColor(Color.BLACK);
        b.setText(warnaNamaArray[index]);
        Drawable d = getResources().getDrawable(R.drawable.icon_warna);
        d.mutate();
        d.setColorFilter(new PorterDuffColorFilter(warnaArray[index], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        return b;
    }

    private void set(){
        lineChart.highlightValues(null);
        hideUbahWarna();
        lineDataSetArrayList.clear();
        for(int i = 0; i < jenisKelaminList.length; i++){
            if(jenisKelaminMap.get(jenisKelaminList[i]) == 1){
                for(int k = 0; k < kategoriList.length; k++){
                    if(kategoriMap.get(kategoriList[k]) == 1){
                        ArrayList<Entry> entry = new ArrayList<>();
                        for(Integer t : tahunArrayList){
                            if(i == 2) {
                                entry.add(new Entry(t, data.get(t, kategori).get(k)));
                            }
                            else {
                                entry.add(new Entry(t, data.get(t, i, kategori).get(k)));
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

                        LineDataSet lineDataSet = new LineDataSet(entry, kategoriList[k] + "" + jenisKelaminLegendArray[i]);
                        lineDataSet.setColor(warnaArray[(i + k) % warnaNamaArray.length]);
                        lineDataSet.setCircleColor(Color.BLACK);
                        lineDataSet.setValueTextColor(Color.BLACK);
                        lineDataSet.setValueTextSize(10f);
                        lineDataSet.setLineWidth(4f);
                        lineDataSet.setCircleRadius(6f);
                        lineDataSet.setCircleHoleRadius(3f);
                        lineDataSetArrayList.add(lineDataSet);
                    }
                }
            }
        }
    }

    private void initChart(){
        Legend l = lineChart.getLegend();
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        MarkerView mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setAxisMinimum(getTahunMinimum() - 0.2f);
        lineChart.getXAxis().setAxisMaximum(getTahunMaximum() + 0.2f);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setLabelRotationAngle(45f);
        lineChart.setExtraRightOffset(15f);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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
        LineData lineData = new LineData(lineDataSetArrayList);
        lineData.setDrawValues(false);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private Integer getTahunMaximum(){
        Integer t = tahunArrayList.get(0);
        for(Integer i : tahunArrayList){
            if(i > t){
                t = i;
            }
        }
        return t;
    }
    private Integer getTahunMinimum(){
        Integer t = tahunArrayList.get(0);
        for(Integer i : tahunArrayList){
            if(i < t){
                t = i;
            }
        }
        return t;
    }

    private void hideUbahWarna(){
        ((LinearLayout)getView().findViewById(R.id.chart_scatter_warna)).removeAllViews();
    }
    private void showUbahWarna(int dataSetIndex){
        LinearLayout ll = (LinearLayout)getView().findViewById(R.id.chart_scatter_warna);
        if(ll.getChildCount() == 0) {
            ll.addView(ubahWarnaTitle);
            ll.addView(ubahWarnaField);
        }
        for(int i = 0; i < ubahWarnaField.getChildCount(); i++){
            final int index = i;
            ubahWarnaField.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LineDataSet)lineDataSetArrayList.get(dataSetIndex)).setColor(warnaArray[index]);
                    refreshData();
                    hideUbahWarna();
                }
            });
        }
    }
    public void save(){
        if(lineChart.saveToGallery("line_chart_" + System.currentTimeMillis(), 70)){
            Toast.makeText(getContext(), "Saved at DCIM folder", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Failed to icon_save Chart", Toast.LENGTH_SHORT).show();
        }
    }
}