package com.example.datavisualization;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class KelolaDataActivity extends AppCompatActivity {
    DatasetKetenagakerjaanKabupaten data = MainActivity.database.data;
    float dp;

    HorizontalScrollView tableViewL, tableViewP;
    Button tahunPopup, kategoriLButton, kategoriPButton;
    LinearLayout page;

    ArrayList<GridLayout> gridL, gridP;
    ArrayList<ArrayList<EditText>> cellsL, cellsP;

    Handler handler;
    ProgressBar progressBar;

    int tahun, kategoriL, kategoriP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //hide status bar

        setContentView(R.layout.activity_kelola_data);

        tableViewL = findViewById(R.id.kelola_data_lakilaki_scroll);
        tableViewP = findViewById(R.id.kelola_data_perempuan_scroll);
        tahunPopup = findViewById(R.id.kelola_data_tahun_button);
        kategoriLButton = findViewById(R.id.kelola_data_kategori_lakilaki_button);
        kategoriPButton = findViewById(R.id.kelola_data_kategori_perempuan_button);
        page = findViewById(R.id.kelola_data_page);

        dp = this.getResources().getDisplayMetrics().density; // this is a scale from dp to int (uses + 0.5f)

        handler = new Handler();
        progressBar = findViewById(R.id.kelola_data_progress_bar);
        progressBar.setMax(32);
        progressBar.setProgress(0);

        if(!MainActivity.thread.containsKey("kelola_data_init_tables")){
            MainActivity.thread.put("kelola_data_init_tables", new Thread(() -> {
                init();
                runOnUiThread(this::initUI); //Lambda can be replaced by method reference { this::<MethodName> }
            }));
        }
        if(!MainActivity.thread.containsKey("kelola_data_simpan")){
            MainActivity.thread.put("kelola_data_simpan", new Thread(() -> {
                save();
                MainActivity.database.save();
            }));
        }

        try{
            MainActivity.thread.get("kelola_data_init_tables").start();
        }
        catch(IllegalThreadStateException e){
            MainActivity.thread.get("kelola_data_init_tables").run();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.thread.get("kelola_data_init_tables").interrupt();
        MainActivity.thread.get("kelola_data_simpan").interrupt();
        MainActivity.thread.remove("kelola_data_init_tables");
        MainActivity.thread.remove("kelola_data_simpan");
    }

    public synchronized void init(){
        for(int i = 1; i < page.getChildCount(); i++){
            page.getChildAt(i).setVisibility(View.INVISIBLE);
        }

        gridL = new ArrayList<>();
        gridP = new ArrayList<>();
        cellsL = new ArrayList<>();
        cellsP = new ArrayList<>();

        kategoriL = 0;
        kategoriP = 0;
        tahun = -1;

        for(int i = 0; i < 8; i++){
            cellsL.add(getCells(i));
            increment();
            cellsP.add(getCells(i));
            increment();
            addGrid(i, DatasetKetenagakerjaanKabupaten.LAKI_LAKI);
            increment();
            addGrid(i, DatasetKetenagakerjaanKabupaten.PEREMPUAN);
            increment();
        }
    }
    public synchronized void initUI(){
        tahunPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
        kategoriLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKategori(view, DatasetKetenagakerjaanKabupaten.LAKI_LAKI);
            }
        });
        kategoriPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKategori(view, DatasetKetenagakerjaanKabupaten.PEREMPUAN);
            }
        });
        findViewById(R.id.kelola_data_simpan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.thread.get("kelola_data_simpan").isAlive()) {
                    try {
                        MainActivity.thread.get("kelola_data_simpan").start();
                    } catch (IllegalThreadStateException e) {
                        MainActivity.thread.get("kelola_data_simpan").run();
                    }
                }
            }
        });
        page.getChildAt(0).setVisibility(View.INVISIBLE);
        page.getChildAt(1).setVisibility(View.VISIBLE);
    }
    private void increment(){
        handler.post(() -> progressBar.incrementProgressBy(1));
    }

    private int dpToInt(int d){
        int dps = (int)(d*dp + 0.5f);
        return dps;
    }
    private void addGrid(int kategori, int jk){
        GridLayout grid = new GridLayout(this);

        grid.setBackground(getResources().getDrawable(R.drawable.grid));
        //Add Header
        grid.setColumnCount(DatasetKetenagakerjaanKabupaten.getSize(kategori));
        if(kategori == DatasetKetenagakerjaanKabupaten.JENIS_KEGIATAN){
            grid.addView(addTextViewToGrid("Angkatan Kerja", 1, 3));
            grid.addView(addTextViewToGrid("Bukan Angkatan Kerja", 1, 3));
            grid.addView(addTextViewToGrid("Bekerja", 2, 1));
            grid.addView(addTextViewToGrid("Pengangguran", 1, 2));
            grid.addView(addTextViewToGrid("Sekolah", 2, 1));
            grid.addView(addTextViewToGrid("Mengurus Rumah Tangga", 2, 1));
            grid.addView(addTextViewToGrid("Lainnya", 2, 1));
            grid.addView(addTextViewToGrid("Pernah Bekerja", 1, 1));
            grid.addView(addTextViewToGrid("Tidak Pernah Bekerja", 1, 1));
        }
        else if(kategori == DatasetKetenagakerjaanKabupaten.PENGANGGURAN){
            grid.addView(addTextViewToGrid("Pengangguran Terbuka", 1, 4));
            grid.addView(addTextViewToGrid("Setengah Pengangguran", 1, 2));
            for(String s : DatasetKetenagakerjaanKabupaten.getList(kategori)){
                grid.addView(addTextViewToGrid(s));
            }
        }
        else {
            for(String s : DatasetKetenagakerjaanKabupaten.getList(kategori)){
                grid.addView(addTextViewToGrid(s));
            }
        }
        //Add Header

        if(jk == DatasetKetenagakerjaanKabupaten.LAKI_LAKI){
            for(EditText et : cellsL.get(kategori)){
                grid.addView(et);
            }
            gridL.add(grid);
        }
        else {
            for(EditText et : cellsP.get(kategori)){
                grid.addView(et);
            }
            gridP.add(grid);
        }
    }
    private TextView addTextViewToGrid(String str){
        return addTextViewToGrid(str, 1, 1);
    }
    private TextView addTextViewToGrid(String str, int rowSpan, int colSpan){
        TextView tv = new TextView(this);
        GridLayout.LayoutParams pr = new GridLayout.LayoutParams();
        pr.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, rowSpan);
        pr.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, colSpan);
        pr.setGravity(Gravity.FILL);

        tv.setLayoutParams(pr);
        tv.setGravity(Gravity.CENTER);

        int pd = dpToInt(8);
        tv.setPadding(pd, pd, pd, pd);
        tv.setBackground(ContextCompat.getDrawable(this, R.drawable.cell));
        tv.setTextColor(Color.BLACK);

        tv.setText(str);
        return tv;
    }
    private EditText addEditTextToGrid(){
        EditText et = new EditText(this);
        GridLayout.LayoutParams pr = new GridLayout.LayoutParams();
        pr.setGravity(Gravity.FILL);

        et.setLayoutParams(pr);

        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        et.setGravity(Gravity.CENTER);

        int pd = dpToInt(8);
        et.setPadding(pd, pd, pd, pd);
        et.setBackground(ContextCompat.getDrawable(this, R.drawable.cell));

        return et;
    }
    private ArrayList<EditText> getCells(int kategori){
        ArrayList<EditText> cells = new ArrayList<>();
        for(int i = 0; i < DatasetKetenagakerjaanKabupaten.getSize(kategori); i++){
            cells.add(addEditTextToGrid());
        }
        return cells;
    }

    private void showMenu(View view){
        PopupMenu menu = new PopupMenu(KelolaDataActivity.this, view);
        if(data.tahun.size() > 0){
            for(int i = 0; i < data.tahun.size(); i++){
                menu.getMenu().add(Integer.toString(data.tahun.get(i).value));
            }
        }
        menu.getMenu().add("Tambah ...");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("Tambah ...")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(KelolaDataActivity.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_tambah_tahun, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(true);
                    dialog.setTitle("Tambah");

                    TextView warningTextView = (TextView) dialogView.findViewById(R.id.dialog_warning_text_view);
                    EditText inputNumberEditText = (EditText) dialogView.findViewById(R.id.dialog_input_number_edit_text);
                    inputNumberEditText.setText(null);

                    dialog.setPositiveButton("Submit", null);
                    dialog.setNegativeButton("Cancel", null);

                    AlertDialog ad = dialog.show();
                    ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(inputNumberEditText.getText().toString().trim().length() == 0){
                                warningTextView.setText("Tahun yang dimasukkan tidak boleh kosong");
                            }
                            else {
                                int t = Integer.parseInt(inputNumberEditText.getText().toString());
                                if(data.isTahunExist(t) == true){
                                    warningTextView.setText("Tahun yang dimasukkan sudah ada");
                                }
                                else {
                                    data.newTahun(t);
                                    changeTahun(t);
                                    ad.dismiss();
                                }
                            }
                        }
                    });
                    ;                }
                else {
                    changeTahun(Integer.parseInt(menuItem.getTitle().toString()));
                }
                return true;
            }
        });
        menu.show();
    }
    private void showKategori(View view, int jk){
        PopupMenu menu = new PopupMenu(KelolaDataActivity.this, view);
        for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
            menu.getMenu().add(DatasetKetenagakerjaanKabupaten.KATEGORI[i]);
        }
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String selected = menuItem.getTitle().toString();
                for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
                    if(selected.equals(DatasetKetenagakerjaanKabupaten.KATEGORI[i])){
                        changeTable(i, jk);
                        break;
                    }
                }
                return true;
            }
        });
        menu.show();
    }
    private void changeTahun(int t){
        if(tahun != -1){
            save();
        }
        tahun = t;

        tahunPopup.setText(Integer.toString(tahun));
        for(int i = 0; i < cellsL.size(); i++){
            ArrayList<Integer> al = data.get(tahun, DatasetKetenagakerjaanKabupaten.LAKI_LAKI, i);
            for(int j = 0; j < al.size(); j++){
                if(al.get(j) == -1){
                    cellsL.get(i).get(j).setText(null);
                }
                else {
                    cellsL.get(i).get(j).setText(al.get(j).toString());
                }
            }
        }
        for(int i = 0; i < cellsP.size(); i++){
            ArrayList<Integer> al = data.get(tahun, DatasetKetenagakerjaanKabupaten.PEREMPUAN, i);
            for(int j = 0; j < al.size(); j++){
                if(al.get(j) == -1){
                    cellsP.get(i).get(j).setText(null);
                }
                else {
                    cellsP.get(i).get(j).setText(al.get(j).toString());
                }
            }
        }
        changeTable(kategoriL, DatasetKetenagakerjaanKabupaten.LAKI_LAKI);
        changeTable(kategoriP, DatasetKetenagakerjaanKabupaten.PEREMPUAN);

        for(int i = 2; i < page.getChildCount(); i++){
            page.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }
    private void changeTable(int kategori, int jk){
        if(jk == DatasetKetenagakerjaanKabupaten.LAKI_LAKI){
            if(tableViewL.getChildCount() > 0){
                tableViewL.removeAllViews();
            }
            tableViewL.addView(gridL.get(kategori));
            kategoriLButton.setText(DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
        }
        else{
            if(tableViewP.getChildCount() > 0){
                tableViewP.removeAllViews();
            }
            tableViewP.addView(gridP.get(kategori));
            kategoriPButton.setText(DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
        }
    }

    private synchronized void save(){
        ArrayList<ArrayList<Integer>> alL = new ArrayList<>();
        ArrayList<ArrayList<Integer>> alP = new ArrayList<>();

        for(ArrayList<EditText> row : cellsL){
            ArrayList<Integer> r = new ArrayList<>();
            for(EditText et : row){
                int number = -1;
                if(et.getText().toString().trim().length() > 0) {
                    number = Integer.parseInt(et.getText().toString());
                }
                r.add(number);
            }
            alL.add(r);
        }
        for(ArrayList<EditText> row : cellsP){
            ArrayList<Integer> r = new ArrayList<>();
            for(EditText et : row){
                int number = -1;
                if(et.getText().toString().trim().length() > 0) {
                    number = Integer.parseInt(et.getText().toString());
                }
                r.add(number);
            }
            alP.add(r);
        }
        data.setAll(alL, alP, tahun);
    }
}