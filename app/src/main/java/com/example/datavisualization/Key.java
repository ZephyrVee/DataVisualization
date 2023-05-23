package com.example.datavisualization;

public enum Key {
    TABEL_AKUN("col_akun"),
    USERNAME("nama_administrator"),
    PASSWORD("password");

    private final String cipher;
    Key(String str){
        this.cipher = Enkripsi.encrypt(str);
    }

    public String key(){
        return cipher;
    }
}
