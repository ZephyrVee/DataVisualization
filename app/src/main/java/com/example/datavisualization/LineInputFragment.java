package com.example.datavisualization;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import java.util.HashMap;
import java.util.Map;

public class LineInputFragment extends Fragment {
    final Integer DEFAULT_COLOR = 0;

    DatasetKetenagakerjaanKabupaten data;

    Map<String, Integer> tahun;


    public LineInputFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        tahun = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineInit();
    }

    private void lineInit(){
        if(data.tahun.size() > 0){
            for(int i = 0; i < data.tahun.size(); i++){
                addTahun(i);
            }
        }
    }

    private void addTahun(int i){
        if(data.isComplete(i)){

            LinearLayout ll = addEmptyField();

            //add checkbox

            //add checkbox


        }
    }
    private LinearLayout addEmptyField(){
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(llpr);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.START);
        return ll;
    }
    private CheckBox addCheckBox(int i){
        final String s = data.tahun.get(i).value.toString();
        tahun.put(s, 0);

        CheckBox cb = new CheckBox(getContext());
        cb.setText(s);
        cb.setTextSize(10);
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tahun.put(s, 1);
                }
                else{
                    tahun.remove(s);
                }
            }
        });
        return cb;
    }
    private void setBundle(){
        Bundle bundle = new Bundle();
    }
}