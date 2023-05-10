package com.example.datavisualization;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private FirebaseFirestore db;
    public DocumentReference database;

    private static final String databaseNamePlain = "col_database";
    private static final String documentNamePlain = "doc_Skripsi221810477";



    private static final String jenisKelaminColPlain = "col_jenis_kelamin";
    private static final String namaJenisKelaminFieldPlain = "nama_jenis_kelamin";

    private static final String databaseName = Enkripsi.encrypt(databaseNamePlain);
    private static final String documentName = Enkripsi.encrypt(documentNamePlain);



    private static final String jenisKelaminCol = Enkripsi.encrypt(jenisKelaminColPlain);
    private static final String namaJenisKelaminField = Enkripsi.encrypt(namaJenisKelaminFieldPlain);


    public Database(){
        this.db = FirebaseFirestore.getInstance();
        this.database = db.collection(databaseName).document(documentName);

    }

    public DocumentReference docRef(DocumentReference from, ArrayList<String> query){
        CollectionReference cr = from.collection(query.get(0));
        query.remove(0);
        if(query.size() > 1){
            DocumentReference dr = cr.document(query.get(0));
            query.remove(0);
            return docRef(dr, query);
        }
        return cr.document(query.get(0));
    }
}
