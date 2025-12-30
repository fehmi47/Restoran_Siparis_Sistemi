package service;

import java.util.ArrayList;
import java.util.List;

import entity.Menu;
import entity.Enums.YemekKategori;
import kayit.DosyaIslemleri;

public class MenuService {
    private static MenuService instance;
    private List<Menu> menuListesi;
    private final String MENU_DOSYA_ADI = "menu.dat";

    private MenuService() {
        menuListesi = DosyaIslemleri.verileriOku(MENU_DOSYA_ADI);
        if(menuListesi == null) {
            menuListesi = new ArrayList<>();
        }
    }

    public static MenuService getInstance() {
        if (instance == null) {
            instance = new MenuService();
        }
        return instance;
    }

    public void yemekEkle(String ad, double fiyat, YemekKategori kategori) {
        for (Menu menu : menuListesi) {
            if (menu.getAd().equalsIgnoreCase(ad)) {
                System.out.println("Hata: " + ad + " adlı yemek zaten menüde var!");
                return;
            }
        }   

        int yeniId = 1;
        if (!menuListesi.isEmpty()) {
            yeniId = menuListesi.get(menuListesi.size() - 1).getId() + 1;
        }
        
        Menu yeniMenu = new Menu(yeniId, ad, fiyat, kategori);

        menuListesi.add(yeniMenu);
        menuyuKaydet();
    }

    public void yemekGuncelle(int id,String ad,double fiyat, YemekKategori kategori){
        Menu menu = idIleYemekBul(id);
        menu.setAd(ad);
        menu.setFiyat(fiyat);
        menu.setKategori(kategori);
    }

    public boolean yemekSil(int id) {
        boolean silindi = menuListesi.removeIf(menu -> menu.getId() == id);
        if (silindi) {
            menuyuKaydet();
        } 
        return silindi;
    }


    public List<Menu> getMenuListesi() {
        return new ArrayList<>(menuListesi);
    }

    public Menu idIleYemekBul(int id) {
        for (Menu menu : menuListesi) {
            if (menu.getId() == id) {
                return menu;
            }
        }
        return null; 
    }

    private void menuyuKaydet() {
        DosyaIslemleri.veriyiKaydet(MENU_DOSYA_ADI, menuListesi);
    }

}
