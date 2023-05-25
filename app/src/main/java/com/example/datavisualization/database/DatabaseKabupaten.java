package com.example.datavisualization.database;

import com.example.datavisualization.Enkripsi;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseKabupaten {
    private FirebaseFirestore ff;
    DocumentReference db;
    DatasetKetenagakerjaanKabupaten data;

    private static final String collectionName = Enkripsi.encrypt("col_database");
    private static final String documentName = Enkripsi.encrypt("doc_Skripsi221810477");
    private static final String databaseName = Enkripsi.encrypt("Ketenagakerjaan Jakarta Utara");
    private static final String tahunCollection = Enkripsi.encrypt("Tahun Jakarta Utara");

    public DatabaseKabupaten(){
        ff = FirebaseFirestore.getInstance();
        db = ff.collection(collectionName).document(documentName);
        data = new DatasetKetenagakerjaanKabupaten();
    }

    public void save(){

    }

    public void load(){

    }
}
