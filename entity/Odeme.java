package entity;

import java.io.Serializable;
import java.time.LocalDate;
import entity.Enums.OdemeYontemi;

public class Odeme implements Serializable {
    private static final long serialVersionUID = 5L;

    private int id;
    private int siparisId; // Hangi siparişin ödemesi olduğu (Foreign Key mantığı)
    private double tutar;
    private OdemeYontemi odemeYontemi;
    private LocalDate islemTarihi;

    public Odeme(int id, int siparisId, double tutar, OdemeYontemi odemeYontemi) {
        this.id = id;
        this.siparisId = siparisId;
        this.tutar = tutar;
        this.odemeYontemi = odemeYontemi;
        this.islemTarihi = LocalDate.now(); // Nesne oluştuğu anın tarihi
    }

    // Getter ve Setter'lar...
    public int getId() { return id; }
    public int getSiparisId() { return siparisId; }
    public double getTutar() { return tutar; }
    public OdemeYontemi getOdemeYontemi() { return odemeYontemi; }
    public LocalDate getIslemTarihi() { return islemTarihi; }
    
    @Override
    public String toString() {
        return "Ödeme [ID=" + id + ", Tutar=" + tutar + ", Yöntem=" + odemeYontemi + "]";
    }
}