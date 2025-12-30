package service;
import entity.Odeme;
import entity.Siparis;
import entity.Enums.OdemeYontemi;
import kayit.DosyaIslemleri;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entity.Enums.SiparisDurumu;;

public class OdemeService {
    private static OdemeService instance;
    private List<Odeme> odemeListesi;
    private final String ODEME_DOSYASI = "odemeler.dat";

    private OdemeService() {
        this.odemeListesi = DosyaIslemleri.verileriOku(ODEME_DOSYASI);
        if (this.odemeListesi == null) {
            this.odemeListesi = new ArrayList<>();
        }
    }

    public static OdemeService getInstance() {
        if (instance == null) {
            instance = new OdemeService();
        }
        return instance;
    }

    public void odemeAl(int masaId, double tutar, OdemeYontemi yontem) {

        Siparis siparis = SiparisService.getInstance().masaSiparisiniGetir(masaId);

        if (siparis == null) {
            System.out.println("Bu masa için aktif sipariş yok.");
            return;
        }

        if (siparis.getDurum() == SiparisDurumu.IPTAL_EDILDI) {
            System.out.println("İptal edilmiş sipariş için ödeme alınamaz.");
            return;
        }

        if (siparis.getDurum() == SiparisDurumu.ODENDI) {
            System.out.println("Bu siparişin ödemesi zaten alınmış.");
            return;
        }

        double toplamTutar = siparis.hesaplaToplamSiparisFiyati();

        if (tutar != toplamTutar) {
            System.out.println("Ödeme tutarı sipariş toplamına eşit olmalıdır.");
            return;
        }

        int yeniId = odemeListesi.size() + 1;
        Odeme yeniOdeme = new Odeme(yeniId, siparis.getSiparisId(), tutar, yontem);

        odemeListesi.add(yeniOdeme);
        odemeleriKaydet();

        SiparisService.getInstance()
            .siparisDurumunuGuncelle(siparis.getSiparisId(), SiparisDurumu.ODENDI);

        System.out.println("Ödeme alındı. Sipariş ödenmiş olarak işaretlendi.");
    }

    public List<Odeme> getOdemeListesi() {
        return new ArrayList<>(odemeListesi);
    }

    public double gunlukCiroHesapla() {
        double toplam = 0;
        LocalDate date = LocalDate.now();
        for (Odeme o : odemeListesi) {
            if(o.getIslemTarihi().equals(date)){
                toplam += o.getTutar();
            }
        }
        return toplam;
    }

    private void odemeleriKaydet() {
        DosyaIslemleri.veriyiKaydet(ODEME_DOSYASI, odemeListesi);
    }
}
