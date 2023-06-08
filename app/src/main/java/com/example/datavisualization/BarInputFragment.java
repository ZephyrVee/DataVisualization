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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class BarInputFragment extends Fragment {

    DatasetKetenagakerjaanKabupaten data;

    LinearLayout category, fields, type;
    ImageButton multiple, stacked;
    int kategori;
    String tipe;
    ArrayList<Integer> tahun, color, jenisKelamin;
    Integer[] colorList;
    String[] colorName, jkName;

    final Integer DEFAULT_TAHUN = 2021;
    final Integer DEFAULT_JENIS_KELAMIN = 0;
    final Integer DEFAULT_WARNA = 0;

    public BarInputFragment() {
        // Required empty public constructor
        setArguments(new Bundle());
        kategori = -1;
        data = MainActivity.database.data;
        tahun = new ArrayList<>();
        jenisKelamin = new ArrayList<>();
        color = new ArrayList<>();
        tipe = "Multiple";

        colorList = new Integer[]{
            Color.BLUE,
            Color.CYAN,
            Color.DKGRAY,
            Color.GRAY,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.YELLOW
        };
        colorName = new String[]{"Biru", "Cyan", "Abu-abu gelap", "Abu-abu", "Hijau", "Magenta", "Merah", "Kuning"};

        jkName = new String[]{"Laki-laki", "Perempuan", "Laki-laki + Perempuan"};
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bar_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barInit();
    }

    @Override
    public void onDestroyView() { // Tab changed
        super.onDestroyView();
        tahun.clear();
        jenisKelamin.clear();
        color.clear();
    }

    private void barInit(){
        category = getView().findViewById(R.id.input_bar_category);
        fields = getView().findViewById(R.id.input_bar_fields);

        type = getView().findViewById(R.id.input_bar_type);
        type.setVisibility(View.INVISIBLE);
        multiple = getView().findViewById(R.id.input_bar_multiple);
        multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipe = "Multiple";
                LinearLayout.LayoutParams mpr = new LinearLayout.LayoutParams(intToDp(48), intToDp(48));
                mpr.rightMargin = intToDp(16);
                multiple.setLayoutParams(mpr);
                stacked.setLayoutParams(new LinearLayout.LayoutParams(intToDp(32), intToDp(32)));
                setBundle();
            }
        });
        stacked = getView().findViewById(R.id.input_bar_stacked);
        stacked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipe = "Stacked";
                LinearLayout.LayoutParams mpr = new LinearLayout.LayoutParams(intToDp(32), intToDp(32));
                mpr.rightMargin = intToDp(16);
                multiple.setLayoutParams(mpr);
                stacked.setLayoutParams(new LinearLayout.LayoutParams(intToDp(48), intToDp(48)));
                setBundle();
            }
        });

        getBundle();
        if(kategori != -1){
            reloadField();
        }

        //category
        Button b = new Button(getContext());
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.setMargins(0, intToDp(8), 0, intToDp(8));
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.down_arrow), null);
        b.setPadding(intToDp(16), intToDp(0), intToDp(16), intToDp(0));
        b.setTextColor(getResources().getColor(R.color.black));
        if(kategori == -1){
            b.setText("Pilih Kategori");
        }
        else {
            b.setText(DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
        }
        b.setTextSize(10);
        b.setOnClickListener(new View.OnClickListener() {
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
                                b.setText(selected);
                                if(fields.getChildCount() == 0){
                                    addNewField(fields.getChildCount());
                                }
                                break;
                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        category.addView(b);
        //category
    }
    private void reloadField(){
        fields.removeAllViews();
        for(int i = 0; i < tahun.size(); i++){
            LinearLayout ll = addEmptyField();
            ll.addView(addDeleteBar(i));
            ll.addView(addPilihTahunBar(i));
            ll.addView(addPilihJKBar(i));
            ll.addView(addPilihWarnaBar(i));
            fields.addView(ll);
        }
        addNewField(fields.getChildCount());
    }
    private void addNewField(int idx){
        LinearLayout ll = addEmptyField();
        ll.addView(addAddBar(idx));
        fields.addView(ll);
        if(fields.getChildCount() > 2){
            type.setVisibility(View.VISIBLE);
        }
        else{
            type.setVisibility(View.INVISIBLE);
        }
    }
    private LinearLayout addEmptyField(){
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(llpr);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.START);

        ll.setBackground(getResources().getDrawable(R.drawable.cell));
        return ll;
    }

    private ImageButton addAddBar(int idx){
        int marginAndPadding = intToDp(8);

        //Button layout params
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        bpr.setMargins(marginAndPadding, marginAndPadding, marginAndPadding, marginAndPadding);
        //Button layout params

        ImageButton b = new ImageButton(getContext());
        b.setLayoutParams(bpr);
        b.setImageDrawable(getResources().getDrawable(R.drawable.add));
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tahun.add(DEFAULT_TAHUN);
                jenisKelamin.add(DEFAULT_JENIS_KELAMIN);
                color.add(DEFAULT_WARNA);
                setBundle();
                ((LinearLayout)b.getParent()).addView(addDeleteBar(idx));
                ((LinearLayout)b.getParent()).addView(addPilihTahunBar(idx));
                ((LinearLayout)b.getParent()).addView(addPilihJKBar(idx));
                ((LinearLayout)b.getParent()).addView(addPilihWarnaBar(idx));

                addNewField(idx + 1);
                ((LinearLayout)b.getParent()).removeViewAt(0);
            }
        });
        return b;
    }
    private ImageButton addDeleteBar(int idx){
        int marginAndPadding = intToDp(8);

        //Button layout params
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        bpr.setMargins(marginAndPadding, marginAndPadding, marginAndPadding, marginAndPadding);
        //Button layout params

        ImageButton b = new ImageButton(getContext());
        b.setLayoutParams(bpr);
        b.setImageDrawable(getResources().getDrawable(R.drawable.remove));
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tahun.remove(idx);
                jenisKelamin.remove(idx);
                color.remove(idx);
                setBundle();
                reloadField();
            }
        });
        return b;
    }
    private Button addPilihTahunBar(int idx){
        int marginAndPadding = intToDp(8);

        //Button layout params
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        bpr.setMargins(marginAndPadding, marginAndPadding, marginAndPadding, marginAndPadding);
        //Button layout params

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setPadding(marginAndPadding, 0, marginAndPadding, 0);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setText("Tahun: " + tahun.get(idx));
        b.setTextSize(10);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                if(data.tahun.size() > 0){
                    for(int i = 0; i < data.tahun.size(); i++){
                        if(data.isComplete(data.tahun.get(i).value)) {
                            menu.getMenu().add(Integer.toString(data.tahun.get(i).value));
                        }
                    }
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        tahun.set(idx, Integer.parseInt(menuItem.getTitle().toString()));
                        b.setText("Tahun: " + tahun.get(idx));
                        setBundle();
                        return true;
                    }
                });
                menu.show();
            }
        });
        return b;
    }
    private Button addPilihJKBar(int idx){
        int marginAndPadding = intToDp(8);

        //Button layout params
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        bpr.setMargins(marginAndPadding, marginAndPadding, marginAndPadding, marginAndPadding);
        //Button layout params

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setPadding(marginAndPadding, marginAndPadding, marginAndPadding, marginAndPadding);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setText(" Jenis Kelamin: " + DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN[jenisKelamin.get(idx)] + " ");
        b.setTextSize(10);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                for(int i = 0; i < jkName.length; i++){
                    menu.getMenu().add(0, i, i, jkName[i]);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        jenisKelamin.set(idx, menuItem.getItemId());
                        b.setText(" Jenis Kelamin: " + DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN[jenisKelamin.get(idx)] + " ");
                        setBundle();
                        return true;
                    }
                });
                menu.show();
            }
        });
        return b;
    }
    private Button addPilihWarnaBar(int idx){
        int marginAndPadding = intToDp(8);

        //Button layout params
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        bpr.setMargins(marginAndPadding, marginAndPadding, marginAndPadding, marginAndPadding);
        //Button layout params

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setPadding(marginAndPadding, 0, marginAndPadding, 0);

        //design
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setText("Warna: " + colorName[color.get(idx)]);
        b.setTextSize(10);
        Drawable r = getResources().getDrawable(R.drawable.warna);
        r.mutate();
        r.setColorFilter(new PorterDuffColorFilter(colorList[color.get(idx)], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, r, null);
        //design

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                for(int i = 0; i < colorList.length; i++){
                    Drawable r = getResources().getDrawable(R.drawable.warna);
                    r.mutate();
                    menu.getMenu().add(0, i, i, menuIconWithText(r, i, colorName[i]));
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        color.set(idx, item.getItemId());
                        b.setText("Warna: " + colorName[item.getItemId()]);
                        Drawable r = getResources().getDrawable(R.drawable.warna);
                        r.mutate();
                        r.setColorFilter(new PorterDuffColorFilter(colorList[item.getItemId()], PorterDuff.Mode.SRC_IN));
                        b.setCompoundDrawablesWithIntrinsicBounds(null, null, r, null);
                        setBundle();
                        return true;
                    }
                });
                menu.show();
            }
        });
        return b;
    }

    private void setBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("tipe", tipe);
        bundle.putInt("kategori", kategori);

        bundle.putIntegerArrayList("tahun", (ArrayList<Integer>)tahun.clone());
        bundle.putIntegerArrayList("jenis_kelamin", (ArrayList<Integer>)jenisKelamin.clone());
        bundle.putIntegerArrayList("warna", (ArrayList<Integer>)color.clone());
        setArguments(bundle);
    }
    private void getBundle(){
        Bundle bundle = getArguments();
        if(bundle.containsKey("tipe")){
            tipe = bundle.getString("tipe");
            if(tipe.equals("Stacked")){
                LinearLayout.LayoutParams mpr = new LinearLayout.LayoutParams(intToDp(32), intToDp(32));
                mpr.rightMargin = intToDp(16);
                multiple.setLayoutParams(mpr);
                stacked.setLayoutParams(new LinearLayout.LayoutParams(intToDp(48), intToDp(48)));
            }
        }
        if(bundle.containsKey("kategori")){
            kategori = bundle.getInt("kategori");
        }
        if(bundle.containsKey("tahun")){tahun = (ArrayList<Integer>)bundle.getIntegerArrayList("tahun").clone();}
        if(bundle.containsKey("jenis_kelamin")){jenisKelamin = (ArrayList<Integer>)bundle.getIntegerArrayList("jenis_kelamin").clone();}
        if(bundle.containsKey("warna")){color = (ArrayList<Integer>)bundle.getIntegerArrayList("warna").clone();}
    }

    private int intToDp(int d) {
        return (int) (d * this.getResources().getDisplayMetrics().density + 0.5f);
    }
    private CharSequence menuIconWithText(Drawable r, int color, String title) {
        r.setColorFilter(new PorterDuffColorFilter(colorList[color], PorterDuff.Mode.SRC_IN));
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_CENTER);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}