package com.example.datavisualization;

import android.os.Handler;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private FirebaseFirestore db;
    public DocumentReference database;

    private static final String databaseNamePlain = "col_database";
    private static final String documentNamePlain = "doc_Skripsi221810477";

    //private final String akunCollection = Enkripsi.encrypt("col_akun");

    private static final String jenisKelaminColPlain = "col_jenis_kelamin";
    private static final String namaJenisKelaminFieldPlain = "nama_jenis_kelamin";

    private static final String databaseName = Enkripsi.encrypt(databaseNamePlain);
    private static final String documentName = Enkripsi.encrypt(documentNamePlain);

    private static final String dataDB = Enkripsi.encrypt("Ketenagakerjaan");


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

    public synchronized void save(ArrayList<ArrayList<Integer>> data, int index, int tahun, int gender){
        Map<String, String> map = new HashMap<>();
        //map.put( Enkripsi.encrypt(Integer.toString(gender)), Enkripsi.encrypt(Integer.toString(tahun)) );
        //database.collection( Enkripsi.encrypt("tahun") ).document(Enkripsi.encrypt(Integer.toString(tahun))).set(map, SetOptions.merge());
        //map = new HashMap<>();
        int idx = 0;
        for(ArrayList<Integer> d : data){
            for(Integer i : d){
                map.put( Enkripsi.encrypt(Integer.toString(idx)), Enkripsi.encrypt(Integer.toString(i)) );
                idx += 1;
            }
        }
        String t = Enkripsi.encrypt(Integer.toString(tahun));
        String g = Enkripsi.encrypt(Integer.toString(gender));
        String i = Enkripsi.encrypt(Integer.toString(index));
        database.collection(dataDB).document(t).collection(g).document(i).set(map, SetOptions.merge());
        map.clear();
    }
    public ArrayList<ArrayList<ArrayList<Integer>>> loadByTahun(int tahun, int gender){
        List<int[]> table = DatasetKetenagakerjaan.table;
        ArrayList<ArrayList<ArrayList<Integer>>> data = new ArrayList<>();
        for(int i = 0; i < table.size(); i++){
            String[] r = DatasetKetenagakerjaan.getList(table.get(i)[0]);
            String[] c = DatasetKetenagakerjaan.getList(table.get(i)[1]);
            ArrayList<ArrayList<Integer>> cell = new ArrayList<>();
            DocumentReference loadFrom = database.
                    collection( Enkripsi.encrypt(Integer.toString(gender)) ). // gender
                    document( Enkripsi.encrypt(Integer.toString(tahun)) ). // tahun
                    collection( Enkripsi.encrypt(Integer.toString(table.get(i)[0])) ). //from
                    document( Enkripsi.encrypt(Integer.toString(table.get(i)[1])) ); //destination

            final int index = i;
            for(int j = 0; j < r.length; j++){
                ArrayList<Integer> row = new ArrayList<>();
                CollectionReference cr = loadFrom.collection( Enkripsi.encrypt(r[j]) );
                for(int k = 0; k < c.length; k++){
                    cr.document( Enkripsi.encrypt(c[k]) ).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String s = documentSnapshot.getString( Enkripsi.encrypt(Integer.toString(table.get(index)[2])) );
                            row.add(Integer.parseInt(Enkripsi.decrypt(s)));
                        }
                    });
                }
                cell.add(row);
            }
            data.add(cell);
        }
        return data;
    }
    public synchronized ArrayList<Integer> loadAllTahun(){
        ArrayList<Integer> ar = new ArrayList<>();
        final boolean[] isProcessing = {false};
        database.collection( dataDB ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot ds : documentSnapshots){
                    String s = Enkripsi.decrypt(ds.getId());
                    ar.add( Integer.parseInt(s) );
                }
                isProcessing[0] = true;
            }
        });
        while (!isProcessing[0]){

        }
        return ar;
    }
}
