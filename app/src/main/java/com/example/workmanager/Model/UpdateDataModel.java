package com.example.workmanager.Model;

public class UpdateDataModel {


    private Integer id;
    private String n;
    private String r;
    private String re;
    private String s;
    private String p;
    private String i;
    private String a;

    public UpdateDataModel() {

    }

    public UpdateDataModel(Integer id, String n, String r, String re, String s, String p, String i, String a) {
        this.id = id;
        this.n = n;
        this.r = r;
        this.re = re;
        this.s = s;
        this.p = p;
        this.i = i;
        this.a = a;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
