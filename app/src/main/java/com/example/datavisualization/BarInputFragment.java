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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class BarInputFragment extends Fragment {

    DatasetKetenagakerjaanKabupaten data;

    LinearLayout category, fields;
    ImageButton addButton;
    int kategori;
    ArrayList<Integer> tahun, color, colorList, jenisKelamin;
    String[] colorName, jkName;

    public BarInputFragment() {
        // Required empty public constructor
        setArguments(new Bundle());
        kategori = -1;
        data = MainActivity.database.data;
        tahun = new ArrayList<>();
        jenisKelamin = new ArrayList<>();
        color = new ArrayList<>();

        colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(Color.CYAN);
        colorList.add(Color.DKGRAY);
        colorList.add(Color.GRAY);
        colorList.add(Color.GREEN);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.RED);
        colorList.add(Color.YELLOW);
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
        Bundle bundle = getArguments();
        if(bundle.containsKey("tahun")){
            ((Button)category.getChildAt(0)).setText(DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
            tahun = (ArrayList<Integer>)bundle.getIntegerArrayList("tahun").clone();
            jenisKelamin = (ArrayList<Integer>)bundle.getIntegerArrayList("jenis_kelamin").clone();
            color = (ArrayList<Integer>)bundle.getIntegerArrayList("warna").clone();
            fields.removeAllViews();
            for(int i = 0; i < tahun.size(); i++){
                addBar(i, tahun.get(i).toString(), jkName[jenisKelamin.get(i)], color.get(i));
                ((LinearLayout)fields.getChildAt(i)).getChildAt(4).setVisibility(View.VISIBLE);
            }
            addButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroyView() { // Tab changed
        super.onDestroyView();
        tahun.clear();
        jenisKelamin.clear();
        color.clear();
    }

    private void barInit(){
        fields = getView().findViewById(R.id.input_fields);
        category = getView().findViewById(R.id.input_category);
        addButton = getView().findViewById(R.id.visualisasi_add_button);
        addButton.setVisibility(View.INVISIBLE);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton.setVisibility(View.INVISIBLE);
                addBar(fields.getChildCount());
            }
        });
        //category
        Button b = new Button(getContext());
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.setMargins(0, intToDp(8), 0, intToDp(8));
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.down_arrow), null);
        b.setPadding(intToDp(16), intToDp(0), intToDp(16), intToDp(0));
        b.setTextColor(getResources().getColor(R.color.black));
        b.setText("Pilih Kategori");
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
                                addButton.setVisibility(View.VISIBLE);
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
    public void addBar(int idx){
        tahun.add(-1);
        jenisKelamin.add(-1);
        color.add(-1);

        addBar(idx, "Pilih Tahun", "Jenis Kelamin", Color.BLACK);
        ((LinearLayout)fields.getChildAt(idx)).getChildAt(2).setVisibility(View.INVISIBLE);
    }
    public void addBar(int idx, String t, String jk, Integer c){
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(llpr);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.START);

        ll.addView(addPilihTahunBar(idx, t));
        ll.addView(addPilihJKBar(idx, jk));
        ll.addView(addWarnaBar());
        ll.addView(addPilihWarnaBar(idx, c));
        ll.addView(addDeleteBar(idx));
        fields.addView(ll);
    }
    private Button addPilihTahunBar(int idx, String text){
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        int m = intToDp(8);
        bpr.setMargins(0, m, m, m);
        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setText(text);
        b.setTextSize(10);
        b.setPadding(0, 0, 0, 0);
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
                        b.setText(menuItem.getTitle().toString());
                        ((LinearLayout)b.getParent()).getChildAt(1).setVisibility(View.VISIBLE);
                        if(color.get(idx) != -1){
                            setBundle();
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        return b;
    }
    private Button addPilihJKBar(int idx, String text){
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        int m = intToDp(8);
        bpr.setMargins(m, m, m, m);
        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setText(text);
        b.setTextSize(10);
        b.setPadding(0, 0, 0, 0);
        if(text.equals("Jenis Kelamin")){
            b.setVisibility(View.INVISIBLE);
        }
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
                        b.setText(menuItem.getTitle().toString());
                        ((LinearLayout)b.getParent()).getChildAt(2).setVisibility(View.VISIBLE);
                        ((LinearLayout)b.getParent()).getChildAt(3).setVisibility(View.VISIBLE);
                        if(color.get(idx) != -1){
                            setBundle();
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        return b;
    }
    private TextView addWarnaBar(){
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams tvpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvpr.setMargins(intToDp(16), 0, intToDp(8), 0);
        tv.setLayoutParams(tvpr);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(12);
        tv.setGravity(Gravity.CENTER);
        tv.setText("Warna:");
        return tv;
    }
    private ImageButton addPilihWarnaBar(int idx, Integer cl){
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        ImageButton c = new ImageButton(getContext());
        c.setLayoutParams(bpr);
        c.setPadding(intToDp(16), intToDp(16), intToDp(16), intToDp(16));
        c.setBackgroundColor(cl);
        if(cl == Color.BLACK){
            c.setVisibility(View.INVISIBLE);
        }
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                for(int i = 0; i < colorList.size(); i++){
                    Drawable r = getResources().getDrawable(R.drawable.warna);
                    r.mutate();
                    menu.getMenu().add(0, i, i, menuIconWithText(r, i, colorName[i]));
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        color.set(idx, colorList.get(item.getItemId()));
                        c.setBackgroundColor(colorList.get(item.getItemId()));
                        setBundle();
                        addButton.setVisibility(View.VISIBLE);
                        ((LinearLayout)c.getParent()).getChildAt(4).setVisibility(View.VISIBLE);
                        return true;
                    }
                });
                menu.show();
            }
        });
        return c;
    }
    private ImageButton addDeleteBar(int idx){
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.setMargins(intToDp(16), 0, intToDp(16), 0);
        bpr.gravity = Gravity.CENTER;
        ImageButton b = new ImageButton(getContext());
        b.setLayoutParams(bpr);
        b.setImageDrawable(getResources().getDrawable(R.drawable.remove));
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tahun.remove(idx);
                jenisKelamin.remove(idx);
                color.remove(idx);
                setBundle();
                fields.removeViewAt(idx);
            }
        });
        return b;
    }

    private void setBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("tipe", "Multiple");
        bundle.putInt("kategori", kategori);

        ArrayList<Integer> t = (ArrayList<Integer>) tahun.clone();
        ArrayList<Integer> jk = (ArrayList<Integer>) jenisKelamin.clone();
        ArrayList<Integer> c = (ArrayList<Integer>) color.clone();
        bundle.putIntegerArrayList("tahun", t);
        bundle.putIntegerArrayList("jenis_kelamin", jk);
        bundle.putIntegerArrayList("warna", c);
        setArguments(bundle);
    }

    private int intToDp(int d) {
        return (int) (d * this.getResources().getDisplayMetrics().density + 0.5f);
    }
    private CharSequence menuIconWithText(Drawable r, int color, String title) {
        r.setColorFilter(new PorterDuffColorFilter(colorList.get(color), PorterDuff.Mode.MULTIPLY));
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_CENTER);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}