package com.example.datavisualization;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EditDataActivity extends AppCompatActivity {
    DatasetKetenagakerjaan data =  MainActivity.database1.dataset;

    HorizontalScrollView gridM, gridF;
    ArrayList<GridLayout> arrayGridM, arrayGridF;
    ArrayList<ArrayList<ArrayList<EditText>>> cellM, cellF;
    TextView titleM, titleF, editDataTableIndexM, editDataTableIndexF;
    Button tahunPopup;

    int tableM = 0;
    int tableF = 0;
    int tahun = -1;

    ProgressBar pb;
    Thread saving;
    Handler handler;

    float dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        System.out.println("create called");

        handler = new Handler();
        pb = findViewById(R.id.pb);
        pb.setMax(70);
        pb.setProgress(0);

        if(!MainActivity.thread.containsKey("edit_data_init_tables")){
            MainActivity.thread.put("edit_data_init_tables", new Thread(() -> {
                init();
                initCell();
                initGrid();
                runOnUiThread(this::initUI); //Lambda can be replaced by method reference { this::<MethodName> }
            }));
        }
        if(!MainActivity.thread.containsKey("edit_data_save")){
            MainActivity.thread.put("edit_data_save", new Thread(() -> {
                saveData(true);
            }));
        }

        try{
            MainActivity.thread.get("edit_data_init_tables").start();
        }
        catch(IllegalThreadStateException e){
            MainActivity.thread.get("edit_data_init_tables").run();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.thread.get("edit_data_init_tables").interrupt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrayGridM.clear();
        arrayGridF.clear();
        cellM.clear();
        cellF.clear();
        MainActivity.thread.remove("edit_data_init_tables");
        MainActivity.thread.remove("edit_data_save");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    //These Functions Only Called in Thread
    private synchronized void init(){
        findViewById(R.id.tahunPopup).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_data_linear_layout_1_m).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_data_linear_layout_2_m).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_data_linear_layout_1_f).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_data_linear_layout_2_f).setVisibility(View.INVISIBLE);
        findViewById(R.id.gridM).setVisibility(View.INVISIBLE);
        findViewById(R.id.gridF).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_data_save_button).setVisibility(View.INVISIBLE);

        dp = this.getResources().getDisplayMetrics().density; // this is a scale from dp to int (uses + 0.5f)

        titleM = findViewById(R.id.titleM);
        titleF = findViewById(R.id.titleF);
        editDataTableIndexM = findViewById(R.id.edit_data_table_index_m);
        editDataTableIndexF = findViewById(R.id.edit_data_table_index_f);
        tahunPopup = findViewById(R.id.tahunPopup);

        gridM = findViewById(R.id.gridM);
        gridF = findViewById(R.id.gridF);

    }
    private synchronized void initUI(){
        findViewById(R.id.tahunPopup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        findViewById(R.id.leftM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableM -= 1;
                changeTable(DatasetKetenagakerjaan.LAKI_LAKI, tableM);
            }
        });

        findViewById(R.id.rightM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableM += 1;
                changeTable(DatasetKetenagakerjaan.LAKI_LAKI, tableM);
            }
        });

        findViewById(R.id.leftF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableF -= 1;
                changeTable(DatasetKetenagakerjaan.PEREMPUAN, tableF);
            }
        });

        findViewById(R.id.rightF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableF += 1;
                changeTable(DatasetKetenagakerjaan.PEREMPUAN, tableF);
            }
        });

        findViewById(R.id.edit_data_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.thread.get("edit_data_save").isAlive()){
                    try{
                        MainActivity.thread.get("edit_data_save").start();
                    }
                    catch(IllegalThreadStateException e){
                        MainActivity.thread.get("edit_data_save").run();
                    }
                }
            }
        });

        findViewById(R.id.loadingView).setVisibility(View.INVISIBLE);
        findViewById(R.id.tahunPopup).setVisibility(View.VISIBLE);
    }
    private synchronized void initCell(){
        cellM = new ArrayList<>();
        cellF = new ArrayList<>();
        for(int[] t : DatasetKetenagakerjaan.table){
            cellM.add(newEditTextCell(t));
            cellF.add(newEditTextCell(t));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pb.incrementProgressBy(1);
                }
            });
        }
    }
    private synchronized void initGrid(){
        arrayGridM = new ArrayList<>();
        arrayGridF = new ArrayList<>();

        for(int i = 0; i < DatasetKetenagakerjaan.table.size(); i++){
            arrayGridM.add(newGrid(DatasetKetenagakerjaan.table.get(i), cellM.get(i)));
            arrayGridF.add(newGrid(DatasetKetenagakerjaan.table.get(i), cellF.get(i)));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pb.incrementProgressBy(1);
                }
            });
        }
    }
    //These Functions Only Called in Thread

    private int dpToInt(int d){
        int dps = (int)(d*dp + 0.5f);
        return dps;
    }

    private synchronized TextView addTextViewToGrid(String str){
        return addTextViewToGrid(str, 1, 1);
    }
    private synchronized TextView addTextViewToGrid(String str, int rowSpan, int colSpan){
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

        tv.setText(str);
        return tv;
    }
    private synchronized EditText addEditTextToGrid(){
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
    private synchronized ArrayList<ArrayList<EditText>> newEditTextCell(int[] table){
        ArrayList<ArrayList<EditText>> cell = new ArrayList<>();
        for(int i = 0; i < data.getSize(table[0]); i++){
            ArrayList<EditText> row = new ArrayList<>();
            for(int j = 0; j < data.getSize(table[1]); j++){
                row.add(addEditTextToGrid());
            }
            cell.add(row);
        }
        return cell;
    }
    private synchronized GridLayout newGrid(int[] table, ArrayList<ArrayList<EditText>> cell){
        GridLayout grid = new GridLayout(this);

        //Add Header
        if (table[1] == DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN){
            grid.setColumnCount(DatasetKetenagakerjaan.SKK_LIST.length + 1);
            grid.addView(addTextViewToGrid(data.getNama(table[0]), 3, 1));
            grid.addView(addTextViewToGrid("Angkatan Kerja", 1, 3));
            grid.addView(addTextViewToGrid("Bukan Angkatan Kerja", 1, 3));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[0], 2, 1));
            grid.addView(addTextViewToGrid("Pengangguran", 1, 2));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[3], 2, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[4], 2, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[5], 2, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[1], 1, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[2], 1, 1));

        }
        else {
            grid.setColumnCount(data.getSize(table[1]) + 1);
            grid.addView(addTextViewToGrid(data.getNama(table[0]), 2, 1));
            grid.addView(addTextViewToGrid(data.getNama(table[1]), 1, data.getSize(table[1])));
            for(String list : data.getList(table[1])){
                grid.addView(addTextViewToGrid(list));
            }
        }
        //Add Header

        for(int i = 0; i < cell.size(); i++){
            grid.addView(addTextViewToGrid(data.getList(table[0])[i]));
            for(EditText et : cell.get(i)){
                grid.addView(et);
            }
        }
        return grid;
    }

    private void showMenu(View view){
        PopupMenu menu = new PopupMenu(EditDataActivity.this, view);
        if(data.T.size() > 0){
            for(int i = 0; i < data.T.size(); i++){
                menu.getMenu().add(Integer.toString(data.T.get(i).tahun));
            }
        }
        menu.getMenu().add("Tambah ...");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("Tambah ...")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditDataActivity.this);
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
    private void changeTahun(int t){
        if(tahun > 0){
            saveData();
        }

        tahun = t;
        tableM = 0;
        tableF = 0;
        changeTable(DatasetKetenagakerjaan.LAKI_LAKI, tableM);
        changeTable(DatasetKetenagakerjaan.PEREMPUAN, tableF);

        loadData();

        if(findViewById(R.id.edit_data_linear_layout_1_m).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.edit_data_linear_layout_1_m).setVisibility(View.VISIBLE);
        }
        if(findViewById(R.id.edit_data_linear_layout_2_m).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.edit_data_linear_layout_2_m).setVisibility(View.VISIBLE);
        }
        if(findViewById(R.id.edit_data_linear_layout_1_f).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.edit_data_linear_layout_1_f).setVisibility(View.VISIBLE);
        }
        if(findViewById(R.id.edit_data_linear_layout_2_f).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.edit_data_linear_layout_2_f).setVisibility(View.VISIBLE);
        }
        if(findViewById(R.id.gridM).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.gridM).setVisibility(View.VISIBLE);
        }
        if(findViewById(R.id.gridF).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.gridF).setVisibility(View.VISIBLE);
        }
        if(findViewById(R.id.edit_data_save_button).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.edit_data_save_button).setVisibility(View.VISIBLE);
        }
    }
    private void changeTable(int gender, int t){
        if(gender == DatasetKetenagakerjaan.LAKI_LAKI){
            gridM.removeAllViews();
            gridM.addView(arrayGridM.get(t));
            if(tableM == 0){
                findViewById(R.id.leftM).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.leftM).setVisibility(View.VISIBLE);
            }

            if(tableM == 34){
                findViewById(R.id.rightM).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.rightM).setVisibility(View.VISIBLE);
            }

            titleM.setText(data.tableTitle[tableM]);
            String s = (tableM + 1) + "/35";
            editDataTableIndexM.setText(s);
        }
        else {
            gridF.removeAllViews();
            gridF.addView(arrayGridF.get(t));
            if(tableF == 0){
                findViewById(R.id.leftF).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.leftF).setVisibility(View.VISIBLE);
            }

            if(tableF == 34){
                findViewById(R.id.rightF).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.rightF).setVisibility(View.VISIBLE);
            }

            titleF.setText(data.tableTitle[tableF]);
            String s = (tableF + 1) + "/35";
            editDataTableIndexF.setText(s);
        }
    }
    private void loadData(){
        for(int i = 0; i < DatasetKetenagakerjaan.table.size(); i++){
            ArrayList<ArrayList<Integer>> dM = data.get(tahun, DatasetKetenagakerjaan.LAKI_LAKI).get(i);
            for(int j = 0; j < cellM.get(i).size(); j++){
                for(int k = 0; k < cellM.get(i).get(j).size(); k++){
                    Integer number = dM.get(j).get(k);
                    if(number != -1){
                        cellM.get(i).get(j).get(k).setText(Integer.toString(number));
                    } else {
                        cellM.get(i).get(j).get(k).setText(null);
                    }
                }
            }
            ArrayList<ArrayList<Integer>> dF = data.get(tahun, DatasetKetenagakerjaan.PEREMPUAN).get(i);
            for(int j = 0; j < cellF.get(i).size(); j++){
                for(int k = 0; k < cellF.get(i).get(j).size(); k++){
                    Integer number = dF.get(j).get(k);
                    if(number != -1){
                        cellF.get(i).get(j).get(k).setText(Integer.toString(number));
                    } else {
                        cellF.get(i).get(j).get(k).setText(null);
                    }
                }
            }
        }
    }
    private synchronized void saveData(){
        saveData(false);
    }
    private synchronized void saveData(boolean toDatabase){
        ArrayList<ArrayList<ArrayList<Integer>>> datasetM = new ArrayList<>();
        for(ArrayList<ArrayList<EditText>> cell: cellM){
            ArrayList<ArrayList<Integer>> dataM = new ArrayList<>();
            for(ArrayList<EditText> row : cell){
                ArrayList<Integer> rowM = new ArrayList<>();
                for(EditText et : row){
                    Integer number = -1;
                    if(et.getText().toString().trim().length() > 0){
                        number = Integer.parseInt(et.getText().toString());
                    }
                    rowM.add(number);
                }
                dataM.add(rowM);
            }
            datasetM.add(dataM);
        }
        ArrayList<ArrayList<ArrayList<Integer>>> datasetF = new ArrayList<>();
        for(ArrayList<ArrayList<EditText>> cell: cellF){
            ArrayList<ArrayList<Integer>> dataF = new ArrayList<>();
            for(ArrayList<EditText> row : cell){
                ArrayList<Integer> rowF = new ArrayList<>();
                for(EditText et : row){
                    Integer number = -1;
                    if(et.getText().toString().trim().length() > 0){
                        number = Integer.parseInt(et.getText().toString());
                    }
                    rowF.add(number);
                }
                dataF.add(rowF);
            }
            datasetF.add(dataF);
        }
        if(toDatabase){
            MainActivity.database1.save(datasetM, datasetF, tahun);
        }
        else {
            data.set(datasetM, datasetF, tahun);
        }
    }
    private boolean isComplete(){
        for(ArrayList<ArrayList<EditText>> cell : cellM){
            for(ArrayList<EditText> row : cell){
                for(EditText et : row){
                    if (et.getText().toString().trim().length() == 0){
                        return false;
                    }
                }
            }
        }
        for(ArrayList<ArrayList<EditText>> cell : cellF){
            for(ArrayList<EditText> row : cell){
                for(EditText et : row){
                    if (et.getText().toString().trim().length() == 0){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}