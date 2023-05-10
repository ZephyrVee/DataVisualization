package com.example.datavisualization;

import java.util.HashMap;
import java.util.Map;

public class Nilai {
    private int l;
    private int p;
    private int lp;

    public Nilai(){

    }

    public Nilai(String l, String p, String lp){
        this.l = Integer.parseInt(l);
        this.p = Integer.parseInt(p);
        this.lp = Integer.parseInt(lp);
    }

    public Nilai(int l, int p, int lp){
        this.l = l;
        this.p = p;
        this.lp = lp;
    }

    public Map<String, String> getField(){
        Map<String, String> field = new HashMap<>();
        field.put(Key.MALE.key(), Integer.toString(this.l));
        field.put(Key.FEMALE.key(), Integer.toString(this.p));
        field.put(Key.BOTH.key(), Integer.toString(this.lp));
        return field;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }
}
