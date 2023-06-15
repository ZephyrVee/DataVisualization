package com.example.datavisualization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CombinedInputFragment extends Fragment {
    final int DEFAULT_KATEGORI = 0;
    final int DEFAULT_TAHUN = 2022;

    DatasetKetenagakerjaanKabupaten data;
    int kategoriBar, kategoriLine, tahunBar, tahunLine;
    Button kategoriBarButton, kategoriLineButton;
    RadioGroup barRadioGroup, lineRadioGroup;

    public CombinedInputFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        kategoriBar = DEFAULT_KATEGORI;
        kategoriLine = DEFAULT_KATEGORI;
        tahunBar = DEFAULT_TAHUN;
        tahunLine = DEFAULT_TAHUN;

        setBundle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combined_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        kategoriBarButton = getView().findViewById(R.id.input_3_bar_kategori);
        kategoriBarButton.setText("Bar CHart: " + DatasetKetenagakerjaanKabupaten.KATEGORI[kategoriBar]);
        kategoriBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
                    menu.getMenu().add(DatasetKetenagakerjaanKabupaten.KATEGORI[i]);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selected = menuItem.getTitle().toString();
                        for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
                            if(selected.equals(DatasetKetenagakerjaanKabupaten.KATEGORI[i])){
                                kategoriBar = i;
                                kategoriBarButton.setText("Bar Chart: " + selected);
                                setBundle();
                                break;
                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        kategoriLineButton = getView().findViewById(R.id.input_3_line_kategori);
        kategoriLineButton.setText("Line Chart: " + DatasetKetenagakerjaanKabupaten.KATEGORI[kategoriLine]);
        kategoriLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
                    menu.getMenu().add(DatasetKetenagakerjaanKabupaten.KATEGORI[i]);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selected = menuItem.getTitle().toString();
                        for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
                            if(selected.equals(DatasetKetenagakerjaanKabupaten.KATEGORI[i])){
                                kategoriLine = i;
                                kategoriLineButton.setText("Line Chart: " + selected);
                                setBundle();
                                break;
                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        barRadioGroup = getView().findViewById(R.id.input_3_bar_tahun);
        lineRadioGroup = getView().findViewById(R.id.input_3_line_tahun);
        if(data.tahun.size() > 0){
            for(int i = 0; i < data.tahun.size(); i++){
                String s = data.tahun.get(i).value.toString();
                int t = data.tahun.get(i).value;
                if(data.isComplete(t)) {
                    addBarRadioButton(t);
                    addLineRadioButton(t);
                }
            }
        }
        barRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tahunBar = checkedId;
                setBundle();
            }
        });
        lineRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tahunLine = checkedId;
                setBundle();
            }
        });
    }

    private void addBarRadioButton(int t){
        RadioGroup.LayoutParams rbpr = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RadioButton rb = new RadioButton(getContext());
        rb.setText(Integer.toString(t));
        rb.setId(t);
        if(t == tahunBar){
            rb.setChecked(true);
        }

        int index = barRadioGroup.getChildCount();
        barRadioGroup.addView(rb, index, rbpr);
    }
    private void addLineRadioButton(int t){
        RadioGroup.LayoutParams rbpr = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RadioButton rb = new RadioButton(getContext());
        rb.setText(Integer.toString(t));
        rb.setId(t);
        if(t == tahunLine){
            rb.setChecked(true);
        }

        int index = lineRadioGroup.getChildCount();
        lineRadioGroup.addView(rb, index, rbpr);
    }

    private void setBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("kategori_bar", kategoriBar);
        bundle.putInt("kategori_line", kategoriLine);
        bundle.putInt("tahun_bar", tahunBar);
        bundle.putInt("tahun_line", tahunLine);
        setArguments(bundle);
    }
    private void getBundle(){
        kategoriBar = getArguments().getInt("kategori_bar");
        kategoriLine = getArguments().getInt("kategori_line");
        tahunBar = getArguments().getInt("tahun_bar");
        tahunLine = getArguments().getInt("tahun_line");
    }
}