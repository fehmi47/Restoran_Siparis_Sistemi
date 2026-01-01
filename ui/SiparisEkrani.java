package ui;

import entity.Masa;
import entity.Menu;
import entity.Siparis;
import entity.SiparisKalemi;
import entity.Enums.YemekKategori;
import entity.Enums.SiparisDurumu; // Enum eklendi
import service.MenuService;
import service.SiparisService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;

public class SiparisEkrani extends JFrame {

    private Masa masa;
    
    private DefaultTableModel tableModel;
    private JLabel lblToplamTutar;
    private JLabel lblDurum; // Sipariş durumunu göstermek için
    private JTable tableSiparis;
    
    // Butonları global yaptık ki durumlarına göre açıp kapatabilelim
    private JButton btnOdemeAl;
    private JButton btnMasaKapat;
    private JButton btnAzalt;
    private JButton btnIptal;

    public SiparisEkrani(Masa masa, AnaEkran anaEkran) {
        this.masa = masa;

        setTitle("Sipariş - Masa " + masa.getMasaNo());
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                anaEkran.masalariYenile();
            }
        });

        // --- SOL PANEL ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        for (YemekKategori kategori : YemekKategori.values()) {
            tabbedPane.addTab(kategori.name(), menuPanelOlustur(kategori));
        }
        add(tabbedPane, BorderLayout.CENTER);


        // --- SAĞ PANEL ---
        JPanel panelSag = new JPanel(new BorderLayout());
        panelSag.setPreferredSize(new Dimension(450, 0));
        panelSag.setBorder(BorderFactory.createTitledBorder("Masa Detayı"));

        // Durum Göstergesi
        lblDurum = new JLabel("DURUM: BOŞ");
        lblDurum.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDurum.setHorizontalAlignment(SwingConstants.CENTER);
        lblDurum.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        panelSag.add(lblDurum, BorderLayout.NORTH);

        // Tablo
        String[] kolonlar = {"Ürün Adı", "Birim Fiyat", "Adet", "Tutar"};
        tableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableSiparis = new JTable(tableModel);
        tableSiparis.setRowHeight(25);
        panelSag.add(new JScrollPane(tableSiparis), BorderLayout.CENTER);

        // --- SAĞ ALT (BUTONLAR) ---
        JPanel panelAltIslemler = new JPanel(new GridLayout(5, 1, 5, 5)); // 5 satır oldu
        panelAltIslemler.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblToplamTutar = new JLabel("TOPLAM: 0.0 TL");
        lblToplamTutar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblToplamTutar.setForeground(new Color(220, 53, 69));
        lblToplamTutar.setHorizontalAlignment(SwingConstants.RIGHT);
        panelAltIslemler.add(lblToplamTutar);

        // 1. Ürün Azalt
        btnAzalt = new JButton("Seçili Ürünü Azalt");
        btnAzalt.setBackground(Color.ORANGE);
        btnAzalt.addActionListener(e -> urunAzaltIslemi());
        panelAltIslemler.add(btnAzalt);

        // 2. Ödeme Al (Yeni Ekranı Açar)
        btnOdemeAl = new JButton("ÖDEME AL ");
        btnOdemeAl.setBackground(new Color(0, 123, 255)); // Mavi
        btnOdemeAl.setForeground(Color.black);
        btnOdemeAl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOdemeAl.addActionListener(e -> odemeEkraniAc());
        panelAltIslemler.add(btnOdemeAl);

        // 3. Masayı Kapat (Sadece Ödenince Açılır)
        btnMasaKapat = new JButton("MASAYI KAPAT ");
        btnMasaKapat.setBackground(new Color(40, 167, 69)); // Yeşil
        btnMasaKapat.setForeground(Color.black);
        btnMasaKapat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMasaKapat.setEnabled(false); 
        btnMasaKapat.addActionListener(e -> masaKapatIslemi());
        panelAltIslemler.add(btnMasaKapat);

        // 4. İptal
        btnIptal = new JButton("Siparişi İptal Et");
        btnIptal.setBackground(Color.GRAY);
        btnIptal.setForeground(Color.black);
        btnIptal.addActionListener(e -> siparisIptalIslemi());
        panelAltIslemler.add(btnIptal);

        panelSag.add(panelAltIslemler, BorderLayout.SOUTH);
        add(panelSag, BorderLayout.EAST);

        siparisTablosunuGuncelle();
    }

   
    private void odemeEkraniAc() {
        Siparis siparis = SiparisService.getInstance().masaSiparisiniGetir(masa.getMasaNo());
        if (siparis == null || siparis.getSiparisKalemleri().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ortada bir sipariş yok!");
            return;
        }

        OdemeEkrani dialog = new OdemeEkrani(this, siparis);
        dialog.setVisible(true);

        // Dialog kapandığında kod buradan devam eder
        if (dialog.isOdemeYapildiMi()) {
            siparisTablosunuGuncelle(); // Durumu güncellemek için tabloyu yenile
        }
    }

    private void masaKapatIslemi() {
        // SiparisService zaten ödeme
        int onay = JOptionPane.showConfirmDialog(this, 
            "Müşteriler kalktı mı? Masa tamamen kapatılıp boşaltılacak.", 
            "Masa Kapatma", JOptionPane.YES_NO_OPTION);
            
        if (onay == JOptionPane.YES_OPTION) {
            boolean kapatildi = SiparisService.getInstance().siparisiKapat(masa.getMasaNo());
            if (kapatildi) {
                JOptionPane.showMessageDialog(this, "Masa boşaltıldı.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Masa kapatılamadı! Ödeme alınmamış olabilir.");
            }
        }
    }

 
    private void siparisTablosunuGuncelle() {
        tableModel.setRowCount(0);
        Siparis siparis = SiparisService.getInstance().masaSiparisiniGetir(masa.getMasaNo());

        if (siparis != null) {
            for (SiparisKalemi kalem : siparis.getSiparisKalemleri()) {
                tableModel.addRow(new Object[]{
                    kalem.getMenu().getAd(), kalem.getMenu().getFiyat(), kalem.getMiktar(), kalem.hesaplaToplamFiyat()
                });
            }
            lblToplamTutar.setText("TOPLAM: " + siparis.hesaplaToplamSiparisFiyati() + " TL");
            
            // --- DURUM KONTROLÜ VE BUTON YÖNETİMİ ---
            lblDurum.setText("DURUM: " + siparis.getDurum());

            if (siparis.getDurum() == SiparisDurumu.ODENDI) {
                // Ödenmiş Sipariş:
                lblDurum.setForeground(new Color(40, 167, 69)); 
                btnOdemeAl.setEnabled(false);  
                btnOdemeAl.setText("ÖDENDİ");
                
                btnMasaKapat.setEnabled(true); 
                
                btnAzalt.setEnabled(false);
                btnIptal.setEnabled(false);
            } else {
                // Normal Sipariş:
                lblDurum.setForeground(Color.BLACK);
                btnOdemeAl.setEnabled(true);
                btnOdemeAl.setText("ÖDEME AL (Masayı Kapatmaz)");
                
                btnMasaKapat.setEnabled(false); // Ödeme almadan kapatamaz
                
                btnAzalt.setEnabled(true);
                btnIptal.setEnabled(true);
            }

        } else {
            // Sipariş Yok
            lblToplamTutar.setText("TOPLAM: 0.0 TL");
            lblDurum.setText("DURUM: BOŞ");
            btnOdemeAl.setEnabled(false);
            btnMasaKapat.setEnabled(false);
            btnAzalt.setEnabled(false);
            btnIptal.setEnabled(false);
        }
    }

   
    private JPanel menuPanelOlustur(YemekKategori kategori) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        List<Menu> tumYemekler = MenuService.getInstance().getMenuListesi();
        List<Menu> kategoriYemekleri = tumYemekler.stream().filter(y -> y.getKategori() == kategori).collect(Collectors.toList());
        for (Menu m : kategoriYemekleri) {
            JButton btnYemek = new JButton("<html><center>" + m.getAd() + "<br/>" + m.getFiyat() + " TL</center></html>");
            btnYemek.setPreferredSize(new Dimension(120, 80));
            btnYemek.setBackground(Color.WHITE);
            btnYemek.setFocusable(false);
            
            // ÖDENMİŞ MASAYA EKLEME YAPILAMASIN KONTROLÜ
            btnYemek.addActionListener(e -> {
                 Siparis s = SiparisService.getInstance().masaSiparisiniGetir(masa.getMasaNo());
                 if (s != null && s.getDurum() == SiparisDurumu.ODENDI) {
                     JOptionPane.showMessageDialog(this, "Ödemesi alınmış masaya ürün eklenemez!\nÖnce masayı kapatıp yeni sipariş açmalısınız.");
                     return;
                 }
                 SiparisService.getInstance().sipariseUrunEkle(masa.getMasaNo(), m);
                 siparisTablosunuGuncelle();
            });
            panel.add(btnYemek);
        }
        return panel;
    }

    private void urunAzaltIslemi() {
        int seciliSatir = tableSiparis.getSelectedRow();
        if (seciliSatir == -1) { JOptionPane.showMessageDialog(this, "Ürün seçiniz."); return; }
        Siparis siparis = SiparisService.getInstance().masaSiparisiniGetir(masa.getMasaNo());
        if(siparis != null) {
            SiparisKalemi kalem = siparis.getSiparisKalemleri().get(seciliSatir);
            SiparisService.getInstance().siparistenUrunCikar(masa.getMasaNo(), kalem.getMenu().getId());
            siparisTablosunuGuncelle();
        }
    }
    
    private void siparisIptalIslemi() {
         int onay = JOptionPane.showConfirmDialog(this, "İptal edilsin mi?", "Onay", JOptionPane.YES_NO_OPTION);
         if(onay == JOptionPane.YES_OPTION) {
             SiparisService.getInstance().siparisiIptalEt(masa.getMasaNo());
             siparisTablosunuGuncelle();
             dispose();
         }
    }
}