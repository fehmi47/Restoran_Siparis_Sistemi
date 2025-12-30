package entity;

import java.io.Serializable;

public class SiparisKalemi implements Serializable{

    private static final long serialVersionUID = 4L;

    private Menu menu;
    private int miktar;

    public SiparisKalemi(Menu menu, int miktar) {
        this.setMenu(menu);
        this.setMiktar(miktar);
    }

    public Menu getMenu() {
        return menu;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMenu(Menu menu) {
        if (menu != null) {
            this.menu = menu;
        } else {
            throw new IllegalArgumentException("Menu null olamaz.");
        }
    }

    public void setMiktar(int miktar) {
        if (miktar > 0) {
            this.miktar = miktar;
        } else {
            throw new IllegalArgumentException("Miktar pozitif olmalıdır.");
        }
    }

    public double hesaplaToplamFiyat() {
        return this.menu.getFiyat() * this.miktar;
    }   

    @Override
    public String toString() {
        return "SiparişKalemi [menu=" + menu.getAd() + 
                ", miktar=" + miktar + 
                ", toplamFiyat=" + hesaplaToplamFiyat() + "]";
    }




}
