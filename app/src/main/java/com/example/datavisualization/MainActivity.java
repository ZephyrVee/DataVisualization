package com.example.datavisualization;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static Map<String, Thread> thread;
    public static DatabaseKabupaten database;

    Handler handler;

    Intent kelolaDataActivity, loginActivity, visualisasiActivity;
    ActivityResultLauncher<Intent> login = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        RelativeLayout.LayoutParams pr = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        pr.setMarginStart(16);
                        pr.setMarginEnd(16);

                        Button kelolaDataButton = new Button(MainActivity.this);
                        kelolaDataButton.setLayoutParams(pr);
                        kelolaDataButton.setText("Masuk ke halaman kelola data ketenagakerjaan");
                        kelolaDataButton.setBackground(getResources().getDrawable(R.drawable.background_green_button_selector));
                        kelolaDataButton.setTextColor(Color.WHITE);
                        kelolaDataButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(kelolaDataActivity);
                            }
                        });
                        RelativeLayout rl = findViewById(R.id.main_kelola_data_button_field);
                        rl.addView(kelolaDataButton);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.chart_button_background)));
        setContentView(R.layout.activity_main);

        thread = new HashMap<>();
        kelolaDataActivity = new Intent(getApplicationContext(), KelolaDataActivity.class);
        loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        visualisasiActivity = new Intent(getApplicationContext(), VisualisasiActivity.class);


        handler = new Handler();
        /*
        thread.put("thread_checker", new Thread(new Runnable() {
            @Override
            public void run() {
                Object[] threads = thread.keySet().toArray();
                for(Object t : threads){
                    String s = t.toString();
                    if(!thread.get(s).isAlive()){
                        thread.icon_remove(s);
                    }
                }
                handler.postDelayed(this, 3000);
            }
        }));

         */
        thread.put("main_loading_database", new Thread(new Runnable() {
            @Override
            public void run() {
                database = new DatabaseKabupaten();
                thread.put("main_loading_screen", new Thread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        if(database.noConnection){
                            TextView tv = findViewById(R.id.main_loading);
                            tv.setText("Tidak ada jaringan internet");
                            tv.setTextColor(Color.RED);
                            database.init();
                        }
                        else{
                            TextView tv = findViewById(R.id.main_loading);
                            tv.setText("Sedang Memperoleh Data. Mohon tunggu ...");
                            tv.setTextColor(Color.BLACK);
                        }

                        if(database.retrieved){
                            ((LinearLayout)findViewById(R.id.main_loading).getParent()).removeViewAt(0);
                        }
                        else {
                            handler.postDelayed(this, 1000);
                        }
                    }
                }));
                try {
                    thread.get("main_loading_screen").start();
                }
                catch(IllegalThreadStateException e){
                    thread.get("main_loading_screen").run();
                }
            }
        }));

        runAllThread();

        findViewById(R.id.main_bar_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visualisasiActivity.putExtra("Chart", "Bar");
                startActivity(visualisasiActivity);
            }
        });

        findViewById((R.id.main_line_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visualisasiActivity.putExtra("Chart", "Line");
                startActivity(visualisasiActivity);
            }
        });
        findViewById((R.id.main_pie_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visualisasiActivity.putExtra("Chart", "Pie");
                startActivity(visualisasiActivity);
            }
        });
        findViewById((R.id.main_scatter_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualisasiActivity.putExtra("Chart", "Scatter");
                startActivity(visualisasiActivity);
            }
        });
        findViewById((R.id.main_radar_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualisasiActivity.putExtra("Chart", "Radar");
                startActivity(visualisasiActivity);
            }
        });
        findViewById((R.id.main_combined_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualisasiActivity.putExtra("Chart", "Combined");
                startActivity(visualisasiActivity);
            }
        });

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.main_popup);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(-1, -1);
        dialog.show();
        Button popupClose = dialog.findViewById(R.id.main_popup_close);
        popupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED
        );
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(Object o : thread.keySet().toArray()){
            String s = o.toString();
            thread.get(s).interrupt();
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showMenu();
        return true;
    }

    private void runAllThread(){
        for(Object o : thread.keySet().toArray()){
            String s = o.toString();
            try {
                thread.get(s).start();
            }
            catch(IllegalThreadStateException e){
                thread.get(s).run();
            }
        }
    }

    private void showMenu(){
        PopupMenu menu = new PopupMenu(MainActivity.this, MainActivity.this.findViewById(R.id.account));
        menu.getMenu().add("Admin? Login disini!");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("Admin? Login disini!")){
                    login.launch(loginActivity);
                }
                return true;
            }
        });
        menu.show();
    }
}