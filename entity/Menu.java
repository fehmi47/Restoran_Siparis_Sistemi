package entity;

import java.io.Serializable;

import entity.Enums.YemekKategori;

public class Menu implements Serializable{
    
    private static final long serialVersionUID = 2L;

    private int id;
    private String ad;
    private double fiyat;
    private YemekKategori kategori;
     

    public Menu(int id, String ad, double fiyat, YemekKategori kategori) {
        this.id = id;
        this.setAd(ad);
        this.setFiyat(fiyat);   
        this.setKategori(kategori);
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public double getFiyat() {
        return fiyat;
    }

    public YemekKategori getKategori() {
        return kategori;
    }

    public void setAd(String ad) {
        if (ad != null && !ad.trim().isEmpty()) {
            this.ad = ad;
        } else {
            throw new IllegalArgumentException("Ad boş olamaz.");
        }
    }

    public void setFiyat(double fiyat) {
        if (fiyat >= 0) {
            this.fiyat = fiyat;
        } else {
            throw new IllegalArgumentException("Fiyat negatif olamaz.");
        }
    }

    public void setKategori(YemekKategori kategori) {
        if (kategori != null) {
            this.kategori = kategori;
        } else {
            throw new IllegalArgumentException("Kategori null olamaz.");
        }
    }

    @Override
    public String toString() {
        return "Menu [id=" + id + 
                ", ad=" + ad +
                ", fiyat=" + fiyat +
                ", kategori=" + kategori + "]";
    }





}
