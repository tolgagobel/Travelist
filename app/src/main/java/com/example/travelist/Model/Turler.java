package com.example.travelist.Model;

public class Turler {
    private String ad;
    private String resim;
    private String kategoriid;


    public  Turler(){
    }

    public Turler(String ad, String resim, String kategoriid) {
        this.ad = ad;
        this.resim = resim;
        this.kategoriid = kategoriid;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getKategoriId() {
        return kategoriid;
    }

    public void setKategoriId(String kategoriId) {
        this.kategoriid = kategoriId;
    }
}
