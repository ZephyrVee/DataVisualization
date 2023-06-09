package com.example.datavisualization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InputFragment extends Fragment {
    final int DEFAULT_KATEGORI = 0;

    DatasetKetenagakerjaanKabupaten data;
    Map<String, Integer> tahun = new HashMap<>();
    int kategori;
    Button kategoriButton;

    public InputFragment() {
        // Required empty public constructor
        setArguments(new Bundle());

        data = MainActivity.database.data;
        kategori = DEFAULT_KATEGORI;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments().containsKey("tahun")){
            getBundle();
        }

        kategoriButton = getView().findViewById(R.id.input_kategori);
        initTahun();
        initCheckBox();
        initKategoriButton();
        kategoriButton.setOnClickListener(new View.OnClickListener() {
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
                                kategori = i;
                                kategoriButton.setText("Kategori: " + selected);
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
    }

    private ArrayList<Integer> getTahunList(){
        ArrayList<Integer> al = new ArrayList<>();
        Object[] object = tahun.keySet().toArray();
        for(Object o : object){
            String s = o.toString();
            if(tahun.get(s) == 1){
                Integer i = Integer.parseInt(s);
                al.add(i);
            }
        }
        return al;
    }
    private void setTahunList(ArrayList<Integer> t){
        for(Integer i : t){
            tahun.put(i.toString(), 1);
        }
    }
    private void initTahun(){
        if(data.tahun.size() > 0){
            for(int i = 0; i < data.tahun.size(); i++){
                String s = data.tahun.get(i).value.toString();
                int t = data.tahun.get(i).value;

                //dont forget
                if(!tahun.containsKey(s)){
                    tahun.put(s, 0);
                }
                // if is complete
            }
        }
    }
    private void initKategoriButton(){
        kategoriButton.setText("Kategori: " + DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
    }
    private void initCheckBox(){
        LinearLayout ll = getView().findViewById(R.id.input_tahun);
        ll.removeAllViews();

        Object[] object = tahun.keySet().toArray();
        for(Object o : object){
            String s = o.toString();
            ll.addView(addCheckBox(s));
        }
    }
    private CheckBox addCheckBox(String s){
        CheckBox cb = new CheckBox(getContext());
        cb.setText(s);
        cb.setTextSize(14);
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(tahun.get(s) == 1){
            cb.setChecked(true);
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tahun.put(s, 1);
                }
                else{
                    tahun.put(s, 0);
                }
                setBundle();
            }
        });
        return cb;
    }
    private void setBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("kategori", kategori);
        bundle.putIntegerArrayList("tahun", getTahunList());
        setArguments(bundle);
    }
    private void getBundle(){
        Bundle bundle = getArguments();
        setTahunList(bundle.getIntegerArrayList("tahun"));
        kategori = bundle.getInt("kategori");
    }
}