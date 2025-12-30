package service;

import java.util.ArrayList;
import java.util.List;

import entity.Masa;
import entity.Enums.MasaDurumu;
import kayit.DosyaIslemleri;

public class MasaService {
   
    private static MasaService instance;
    private List<Masa> masaListesi;
    private final String MASA_DOSYA_ADI = "masalar.dat";

    private MasaService() {
        this.masaListesi = DosyaIslemleri.verileriOku(MASA_DOSYA_ADI);
        
        if(this.masaListesi == null) {
            this.masaListesi = new ArrayList<>();
        }
    }

    public static MasaService getInstance() {
        if (instance == null) {
            instance = new MasaService();
        }
        return instance;
    }


    public Masa masaEkle(int masaNo, int kapasite) {
        if (masaNoIleMasaBul(masaNo) != null) {
            System.out.println("Hata: " + masaNo + " numaralı masa zaten var!");
            return null;
        }

        Masa yeniMasa = new Masa(masaNo, kapasite);
        masaListesi.add(yeniMasa);
        masalariKaydet();
        return yeniMasa;
    }

    public void masaKapasiteGuncelle(int masaNo, int yeniKapasite) {
        Masa masa = masaNoIleMasaBul(masaNo); 
        if (masa != null) {
            masa.setKapasite(yeniKapasite);
            masalariKaydet();
        } else {
            System.out.println("Hata: " + masaNo + " numaralı masa bulunamadı!");
        }
    }

    public void masaDurumuGuncelle(int masaNo, MasaDurumu yeniDurum) {
        Masa masa = masaNoIleMasaBul(masaNo);
        if (masa == null) {
            System.out.println("Hata: " + masaNo + " numaralı masa bulunamadı!");
            return;
        }
        if (masa.getDurum() == MasaDurumu.DOLU && yeniDurum == MasaDurumu.ARIZALI) {
            System.out.println("Dolu masa arızalı yapılamaz!");
            return;
        }
        masa.setDurum(yeniDurum);
        masalariKaydet();
    }

    public List<Masa> getMasaListesi() {
        return new ArrayList<>(masaListesi);
    }

    public Masa masaNoIleMasaBul(int masaNo) {
        for (Masa masa : masaListesi) {
            if (masa.getMasaNo() == masaNo) {
                return masa;
            }
        }
        return null; 
    }

    private void masalariKaydet() {
        DosyaIslemleri.veriyiKaydet(MASA_DOSYA_ADI, masaListesi);
    }
}