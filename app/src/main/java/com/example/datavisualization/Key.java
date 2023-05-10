package com.example.datavisualization;

public enum Key {
    MALE("Laki-laki"),
    FEMALE("Perempuan"),
    BOTH("Laki-laki + Perempuan"),

    TABEL_AKUN("col_akun"),
    USERNAME("nama_administrator"),
    PASSWORD("password");

    private String cipher;
    private Key(String str){
        this.cipher = Enkripsi.encrypt(str);
    }

    public String key(){
        return cipher;
    }
}
