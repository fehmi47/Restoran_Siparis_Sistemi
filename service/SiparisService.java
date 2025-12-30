package service;

import kayit.DosyaIslemleri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Menu;
import entity.Siparis;
import entity.SiparisKalemi;
import entity.Enums.MasaDurumu;
import entity.Enums.SiparisDurumu;

public class SiparisService {
    
    private static SiparisService instance;
    private final String AKTIF_SIPARISLER_DOSYASI = "aktif_siparisler.dat";
    private final String GECMIS_SIPARISLER_DOSYASI = "gecmis_siparisler.dat";

    
    private Map<Integer, Siparis> aktifSiparislerMap;
    private List<Siparis> gecmisSiparislerListesi;

    private SiparisService() {
        this.aktifSiparislerMap = new HashMap<>();
        List<Siparis> dosyadakiListe = DosyaIslemleri.verileriOku(AKTIF_SIPARISLER_DOSYASI);
        
        for (Siparis siparis : dosyadakiListe) {
            this.aktifSiparislerMap.put(siparis.getMasaNo(), siparis);
        }

        this.gecmisSiparislerListesi = DosyaIslemleri.verileriOku(GECMIS_SIPARISLER_DOSYASI);
        if (this.gecmisSiparislerListesi == null) {
            this.gecmisSiparislerListesi = new ArrayList<>();
        }
    }

    public static SiparisService getInstance() {
        if (instance == null) {
            instance = new SiparisService();
        }
        return instance;
    }


    public Siparis masaSiparisiniGetir(int masaId) {
        return aktifSiparislerMap.get(masaId);
    }


    public void sipariseUrunEkle(int masaId, Menu urun) {
        Siparis siparis = aktifSiparislerMap.get(masaId);

        if (siparis == null) {
            int yeniSiparisId = gecmisSiparislerListesi.size() + aktifSiparislerMap.size() + 1;
            siparis = new Siparis(yeniSiparisId, masaId);
            aktifSiparislerMap.put(masaId, siparis);
            MasaService.getInstance().masaDurumuGuncelle(masaId, MasaDurumu.DOLU);
        }

        boolean urunVarMi = false;
        
        for (SiparisKalemi kalem : siparis.getSiparisKalemleri()) {
            if (kalem.getMenu().getId() == urun.getId()) {
                kalem.setMiktar(kalem.getMiktar() + 1);
                urunVarMi = true;
                break;
            }
        }

        if (!urunVarMi) {
            SiparisKalemi yeniKalem = new SiparisKalemi(urun, 1);
            siparis.ekleSiparisKalemi(yeniKalem);
        }

        aktifSiparisleriKaydet();
        System.out.println(masaId + " nolu masaya " + urun.getAd() + " eklendi.");
    }

 
    public void siparistenUrunCikar(int masaId, int urunId) {
        Siparis siparis = aktifSiparislerMap.get(masaId);
        if (siparis == null) return;

        siparis.getSiparisKalemleri().removeIf(kalem -> {
            if (kalem.getMenu().getId() == urunId) {
                if (kalem.getMiktar() > 1) {
                    kalem.setMiktar(kalem.getMiktar() - 1);
                    return false; 
                }
                return true; 
            }
            return false;
        });

        aktifSiparisleriKaydet();
    }


    public boolean siparisiKapat(int masaId) {
        Siparis siparis = aktifSiparislerMap.get(masaId);

        if (siparis == null) {
            System.out.println("Hata: Aktif sipariş bulunamadı.");
            return false;
        }
        if (siparis.getDurum() != SiparisDurumu.ODENDI) {
            System.out.println("Hata: Ödeme alınmadan masa kapatılamaz!");
            return false;
        }

        aktifSiparislerMap.remove(masaId);
        siparis.setDurum(SiparisDurumu.KAPATILDI); 
        gecmisSiparislerListesi.add(siparis);

        MasaService.getInstance().masaDurumuGuncelle(masaId, MasaDurumu.BOS);

        aktifSiparisleriKaydet();
        gecmisSiparisleriKaydet();
        System.out.println("Sipariş başarıyla kapatıldı.");
        return true;
    }

    public void siparisiIptalEt(int masaId) {
        Siparis iptalEdilenSiparis = aktifSiparislerMap.remove(masaId);

        if (iptalEdilenSiparis != null) {
            iptalEdilenSiparis.setDurum(SiparisDurumu.IPTAL_EDILDI);
            gecmisSiparislerListesi.add(iptalEdilenSiparis);
            MasaService.getInstance().masaDurumuGuncelle(masaId, MasaDurumu.BOS);
            aktifSiparisleriKaydet();
            gecmisSiparisleriKaydet();
        }
    }

    public void siparisDurumunuGuncelle(int siparisId, SiparisDurumu yeniDurum) {
        for (Siparis s : aktifSiparislerMap.values()) {
            if (s.getSiparisId() == siparisId) {
                s.setDurum(yeniDurum);
                aktifSiparisleriKaydet();
                System.out.println("Sipariş (ID: " + siparisId + ") durumu " + yeniDurum + " olarak güncellendi.");
                return;
            }
        }
        System.out.println("Hata: Sipariş bulunamadı ID: " + siparisId);
    }


    private void aktifSiparisleriKaydet() {
        DosyaIslemleri.veriyiKaydet(AKTIF_SIPARISLER_DOSYASI, new ArrayList<>(aktifSiparislerMap.values()));
    }

    private void gecmisSiparisleriKaydet() {
        DosyaIslemleri.veriyiKaydet(GECMIS_SIPARISLER_DOSYASI, gecmisSiparislerListesi);
    }
}





