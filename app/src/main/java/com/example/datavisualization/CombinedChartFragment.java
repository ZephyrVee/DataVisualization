package com.example.datavisualization;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class CombinedChartFragment extends Fragment {
    final int DEFAULT_KATEGORI = 0;
    final int DEFAULT_JENIS_KELAMIN = 2;

    CombinedChart combinedChart;
    BarDataSet barDataSet;
    LineDataSet lineDataSet;
    TextView ubahWarnaTitle;
    GridLayout ubahWarnaField;
    RadioGroup jenisKelaminBarField, jenisKelaminLineField;


    DatasetKetenagakerjaanKabupaten data;
    int kategori, tahunBar, tahunLine, jenisKelaminBar, jenisKelaminLine, warnaBar, warnaLine;

    String[] kategoriArray, jenisKelaminArray, warnaNameArray, jenisKelaminLegendArray;
    int[] warnaArray;


    public CombinedChartFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        kategori = DEFAULT_KATEGORI;
        jenisKelaminBar = DEFAULT_JENIS_KELAMIN;
        jenisKelaminLine = DEFAULT_JENIS_KELAMIN;
        jenisKelaminArray = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combined_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        combinedChart = getView().findViewById(R.id.combinedChart);
        ubahWarnaField = getView().findViewById(R.id.chart_combined_warna_field);
        ubahWarnaTitle = getView().findViewById(R.id.chart_combined_warna_title);
        jenisKelaminBarField = getView().findViewById(R.id.chart_combined_bar_jenis_kelamin_field);
        jenisKelaminLineField = getView().findViewById(R.id.chart_combined_line_jenis_kelamin_field);

        warnaNameArray = getContext().getResources().getStringArray(R.array.chart_warna_nama_array);
        warnaArray = getContext().getResources().getIntArray(R.array.chart_warna_array);

        warnaBar = warnaArray[0];
        warnaLine = warnaArray[6];


        init();
        initChart();
        set();
        refreshData();

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
                        ib.setImageDrawable(getResources().getDrawable(R.drawable.remove));
                    }
                    else {
                        ll.setVisibility(View.VISIBLE);
                        tv.setText("");
                        tv.getLayoutParams().height = 0;
                        ib.setImageDrawable(getResources().getDrawable(R.drawable.add));
                    }
                }
            });
        }
    }

    private void init(){
        Bundle bundle = getArguments();
        kategori = bundle.getInt("kategori");
        tahunBar = bundle.getInt("tahun_bar");
        tahunLine = bundle.getInt("tahun_line");

        kategoriArray = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
        jenisKelaminLegendArray = new String[]{"(L)","(P)","(L+P)"};

        for (int i = 0; i < jenisKelaminArray.length; i++){
            jenisKelaminBarField.addView(addRadioButton(jenisKelaminArray[i], i), i, new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            jenisKelaminLineField.addView(addRadioButton(jenisKelaminArray[i], i), i, new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        jenisKelaminBarField.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                jenisKelaminBar = checkedId;
                set();
                refreshData();
            }
        });
        jenisKelaminLineField.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                jenisKelaminLine = checkedId;
                set();
                refreshData();
            }
        });
        for(int i = 0; i < warnaNameArray.length; i++){
            ubahWarnaField.addView(addWarnaButton(i));
        }
        hideUbahWarna();
    }
    private void initChart(){
        Legend l = combinedChart.getLegend();
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        combinedChart.getDescription().setEnabled(false);
        combinedChart.getAxisRight().setEnabled(false);
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(kategoriArray));
        xAxis.setAxisMaximum(DatasetKetenagakerjaanKabupaten.getSize(kategori));
        xAxis.setAxisMinimum(-1);

        MarkerView mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(combinedChart); // For bounds control
        combinedChart.setMarker(mv);

        combinedChart.setExtraRightOffset(10);
        combinedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e instanceof BarEntry){
                    showUbahWarna("Bar");
                }
                else {
                    showUbahWarna("Line");
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    private RadioButton addRadioButton(String text, int index){
        RadioButton rb = new RadioButton(getContext());
        rb.setText(text);
        rb.setId(index);
        if(index == 2){
            rb.setChecked(true);
        }
        return rb;
    }
    private Button addWarnaButton(int index){
        GridLayout.LayoutParams bpr = new GridLayout.LayoutParams();
        bpr.setMargins(8,8,8,8);

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setTextSize(12);
        b.setTextColor(Color.BLACK);
        b.setText(warnaNameArray[index]);
        Drawable d = getResources().getDrawable(R.drawable.warna);
        d.mutate();
        d.setColorFilter(new PorterDuffColorFilter(warnaArray[index], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        return b;
    }

    private void set(){
        ArrayList<BarEntry> barEntry = new ArrayList<>();
        ArrayList<Entry> lineEntry = new ArrayList<>();

        ArrayList<Integer> dataEntryBar, dataEntryLine;
        if(jenisKelaminBar == 2){
            dataEntryBar = data.get(tahunBar, kategori);
        }
        else {
            dataEntryBar = data.get(tahunBar, jenisKelaminBar, kategori);
        }
        if(jenisKelaminLine == 2){
            dataEntryLine = data.get(tahunLine, kategori);
        }
        else {
            dataEntryLine = data.get(tahunLine, jenisKelaminLine, kategori);
        }
        for(int i = 0; i < kategoriArray.length; i++){
            barEntry.add(new BarEntry(i, dataEntryBar.get(i)));
            lineEntry.add(new Entry(i, dataEntryLine.get(i)));
        }
        barDataSet = new BarDataSet(barEntry, tahunBar + "" + jenisKelaminLegendArray[jenisKelaminBar]);

        lineDataSet = new LineDataSet(lineEntry, tahunLine + "" + jenisKelaminLegendArray[jenisKelaminLine]);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setLineWidth(4f);
        lineDataSet.setCircleRadius(6f);
        lineDataSet.setCircleHoleRadius(3f);
    }
    private void refreshData(){
        barDataSet.setColor(warnaBar);
        lineDataSet.setColor(warnaLine);

        CombinedData combinedData = new CombinedData();
        combinedData.setDrawValues(false);
        BarData barData = new BarData(barDataSet);
        LineData lineData = new LineData(lineDataSet);
        combinedData.setData(barData);
        combinedData.setData(lineData);

        combinedChart.setData(combinedData);
        combinedChart.invalidate();
    }
    private void hideUbahWarna(){
        ((LinearLayout)getView().findViewById(R.id.chart_combined_warna)).removeAllViews();
    }
    private void showUbahWarna(String instance){
        LinearLayout ll = (LinearLayout)getView().findViewById(R.id.chart_combined_warna);
        if(ll.getChildCount() == 0) {
            ll.addView(ubahWarnaTitle);
            ll.addView(ubahWarnaField);
        }
        for(int i = 0; i < ubahWarnaField.getChildCount(); i++){
            final int index = i;
            ubahWarnaField.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(instance.equals("Bar")){
                        warnaBar = warnaArray[index];
                    }
                    else {
                        warnaLine = warnaArray[index];
                    }
                    refreshData();
                    hideUbahWarna();
                }
            });
        }
    }
    public void save(){
        if(combinedChart.saveToGallery("combined_chart_" + System.currentTimeMillis(), 70)){
            Toast.makeText(getContext(), "Saved at DCIM folder", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Failed to save Chart", Toast.LENGTH_SHORT).show();
        }
    }
}