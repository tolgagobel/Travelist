package com.example.travelist.Model;

public class Mekan {

    private String id;
    private String mekanadi;
    private String mekanozet;
    private String turid;
    private String resim;



    public Mekan(){

    }

    public Mekan(String mekanadi, String mekanozet, String turid, String resim) {
        this.mekanadi = mekanadi;
        this.mekanozet = mekanozet;
        this.turid = turid;
        this.resim = resim;
    }

    public String getMekanadi() {
        return mekanadi;
    }

    public void setMekanadi(String mekanadi) {
        this.mekanadi = mekanadi;
    }

    public String getMekanozet() {
        return mekanozet;
    }

    public void setMekanozet(String mekanozet) {
        this.mekanozet = mekanozet;
    }

    public String getTurid() {
        return turid;
    }

    public void setTurid(String turid) {
        this.turid = turid;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}
