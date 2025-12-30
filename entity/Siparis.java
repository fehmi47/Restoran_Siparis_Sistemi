package entity;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entity.Enums.SiparisDurumu;

public class Siparis implements Serializable{

    private static final long serialVersionUID = 3L;

    List<SiparisKalemi> siparisKalemleri = new ArrayList<>();
    private int siparisId;
    private int masaNo;
    private SiparisDurumu durum;
    private LocalDate tarih;

    public Siparis(int siparisId,int masaNo) {
        this.siparisId = siparisId;
        this.setDurum(SiparisDurumu.HAZIRLANIYOR);
        this.masaNo = masaNo;
        this.tarih = LocalDate.now();
    }

    public int getSiparisId() {
        return siparisId;
    }

    public int getMasaNo() {
        return masaNo;
    }

    public Enums.SiparisDurumu getDurum() {
        return durum;
    }

    public LocalDate getDate(){
        return tarih;
    }

    public void setDurum(SiparisDurumu durum) {
        if (durum != null) {
            this.durum = durum;
        } else {
            throw new IllegalArgumentException("Sipariş durumu null olamaz.");
        }
    }

    public List<SiparisKalemi> getSiparisKalemleri() {
        return siparisKalemleri;
    }

    public void ekleSiparisKalemi(SiparisKalemi kalem) {
        if (kalem != null) {
            this.siparisKalemleri.add(kalem);
        } else {
            throw new IllegalArgumentException("Sipariş kalemi null olamaz.");
        }
    }

    public double hesaplaToplamSiparisFiyati() {
        double toplam = 0;
        for (SiparisKalemi kalem : siparisKalemleri) {
            toplam += kalem.hesaplaToplamFiyat();
        }
        return toplam;
    }


    
}