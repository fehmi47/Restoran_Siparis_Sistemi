package entity;

import java.io.Serializable;

import entity.Enums.MasaDurumu;

public class Masa implements Serializable{

    private static final long serialVersionUID = 1L;

    private int masaNo;
    private int kapasite;
    private MasaDurumu durum;

    public Masa(int masaNo, int kapasite) {
        this.masaNo = masaNo;
        this.setKapasite(kapasite);
        this.setDurum(MasaDurumu.BOS);
    }

    public int getMasaNo() {
        return masaNo;
    }

    public int getKapasite() {
        return kapasite;
    }

    public MasaDurumu getDurum() {
        return durum;
    }

    public void setKapasite(int kapasite) {
        if (kapasite > 0) {
            this.kapasite = kapasite;
        } else {
            throw new IllegalArgumentException("Kapasite pozitif olmalıdır.");
        }
    }

    public void setDurum(MasaDurumu durum) {
        if (durum != null) {
            this.durum = durum;
        } else {
            throw new IllegalArgumentException("Masa durumu null olamaz.");
        }
    }
}
