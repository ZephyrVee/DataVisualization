package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String USERNAME = Enkripsi.encrypt("nama_administrator");
    private static final String PASSWORD = Enkripsi.encrypt("password");

    EditText username, password;

    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //hide status bar
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);

        status = findViewById(R.id.statusTextView);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        this.finish();
    }

    private void login(){
        showStatus("No Connection");
        CollectionReference tabelAkun = MainActivity.database.akun;
        tabelAkun.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    String name = Enkripsi.encrypt(username.getText().toString());
                    String pass = Enkripsi.encrypt(password.getText().toString(), true);

                    List<DocumentSnapshot> ds = queryDocumentSnapshots.getDocuments();
                    for(int i = 0; i < ds.size(); i++){
                        String userName = ds.get(i).getString(USERNAME);
                        String userPass = ds.get(i).getString(PASSWORD);
                        if((name.equals(userName)) && (pass.equals(userPass))){
                            setResult(RESULT_OK);
                            finish();
                        }
                        else if(i == ds.size() - 1){
                            showStatus("Wrong Username or Password");
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //Lost Connection starts here
                showStatus("No Connection");
            }
        });
    }

    public void showStatus(String str){
        status.setText(str);
    }
}