package com.example.travelist.Model;

public class Kategori {
    private String ad;
    private String resim;

    public  Kategori(){
    }
    public Kategori(String ad,String resim){
        this.ad=ad;
        this.resim=resim;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getAd() {
        return ad;
    }

    public String getResim() {
        return resim;
    }
}
