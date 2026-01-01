package ui;

import entity.Masa;
import entity.Enums.MasaDurumu; // Enum'ı direkt import ettik
import service.MasaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnaEkran extends JFrame {

    private MasaService masaService;
    private JPanel panelMasalar;

    public AnaEkran() {
        
        masaService = MasaService.getInstance();

        //pencere ayarları
        setTitle("Restoran Otomasyon Sistemi ");
        setSize(1200, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initMenu();

        //Baslık
        JPanel panelBaslik = new JPanel();
        panelBaslik.setBackground(new Color(45, 45, 45)); 
        panelBaslik.setPreferredSize(new Dimension(100, 80));
        panelBaslik.setLayout(new GridBagLayout());

        JLabel lblBaslik = new JLabel("MASA GENEL BAKIŞ");
        lblBaslik.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblBaslik.setForeground(Color.WHITE);
        panelBaslik.add(lblBaslik);

        add(panelBaslik, BorderLayout.NORTH);

        //Masalar Paneli
        panelMasalar = new JPanel();
        panelMasalar.setLayout(new GridLayout(0, 4, 20, 20)); 
        panelMasalar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelMasalar.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(panelMasalar);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        masalariYenile();
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setOpaque(true);

        JMenu menuYonetim = new JMenu("Yönetim Paneli");
        menuYonetim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menuYonetim.setForeground(Color.BLACK);

        // Masa Ekle
        JMenuItem itemMasaEkle = new JMenuItem("Yeni Masa Ekle");
        stilVerMenuye(itemMasaEkle);
        itemMasaEkle.addActionListener(e -> {
            new MasaEkleDialog(this).setVisible(true);
        });

        // Yemek Ekle
        JMenuItem itemYemekEkle = new JMenuItem("Yeni Yemek/Ürün Ekle");
        stilVerMenuye(itemYemekEkle);
        itemYemekEkle.addActionListener(e -> {
            new YemekEkleDialog(this).setVisible(true);
        });
        
        // Yenile
        JMenuItem itemYenile = new JMenuItem("Ekranı Yenile");
        stilVerMenuye(itemYenile);
        itemYenile.addActionListener(e -> masalariYenile());

        // Çıkış
        JMenuItem itemCikis = new JMenuItem("Çıkış");
        stilVerMenuye(itemCikis);
        itemCikis.addActionListener(e -> System.exit(0));

        JMenuItem itemCiro = new JMenuItem("Günlük Ciro Raporu");
        stilVerMenuye(itemCiro);
        itemCiro.addActionListener(e -> {
        double ciro = service.OdemeService.getInstance().gunlukCiroHesapla();
        JOptionPane.showMessageDialog(this, "Bugünkü Toplam Ciro: " + ciro + " TL", "Ciro Raporu", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem itemMenuYonetim = new JMenuItem("Yemek Listesi / Düzenle");
        stilVerMenuye(itemMenuYonetim);
        itemMenuYonetim.addActionListener(e -> {
            new MenuYonetimDialog(this).setVisible(true);
        });
        
       
        menuYonetim.add(itemCiro);
        menuYonetim.add(itemMasaEkle);
        menuYonetim.add(itemYemekEkle);
        menuYonetim.add(itemMenuYonetim);
        menuYonetim.add(itemYenile);
        menuYonetim.addSeparator();
        menuYonetim.add(itemCikis);

        menuBar.add(menuYonetim);
        setJMenuBar(menuBar);
    }

    private void stilVerMenuye(JMenuItem item) {
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        item.setBackground(Color.WHITE);
        item.setForeground(Color.BLACK);
    }

    public void masalariYenile() {
        panelMasalar.removeAll();
        // Service'den güncel listeyi çekiyoruz
        List<Masa> masalar = masaService.getMasaListesi();

        if (masalar.isEmpty()) {
            JLabel lblBos = new JLabel("<html><center>Henüz tanımlı masa yok.<br>Yönetim Panelinden ekleyiniz.</center></html>", SwingConstants.CENTER);
            lblBos.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            panelMasalar.setLayout(new BorderLayout()); 
            panelMasalar.add(lblBos, BorderLayout.CENTER);
        } else {
            //masaları düzenler
            panelMasalar.setLayout(new GridLayout(0, 4, 20, 20));

            for (Masa m : masalar) {
                // Masa Durumu kontrolü (Enum yapına uygun)
                boolean isDolu = (m.getDurum() == MasaDurumu.DOLU);
                boolean isArizali = (m.getDurum() == MasaDurumu.ARIZALI);
                
                // Renk Belirleme
                Color butonRengi;
                String durumMetni;
                
                if (isDolu) {
                    butonRengi = new Color(220, 53, 69); // Kırmızı
                    durumMetni = "DOLU";
                } else if (isArizali) {
                    butonRengi = new Color(108, 117, 125); // Gri
                    durumMetni = "ARIZALI";
                } else {
                    butonRengi = new Color(40, 167, 69); // Yeşil
                    durumMetni = "BOŞ";
                }

                JButton btnMasa = new JButton();
                btnMasa.setLayout(new BorderLayout());

                // HTML Label ile Şık Görünüm
                JLabel lblIcerik = new JLabel(String.format(
                        "<html><center>" +
                                "<font size='5'><b>MASA %d</b></font><br><br>" +
                                "<font size='4'>%d Kişilik</font><br>" +
                                "<font size='3'><i>%s</i></font>" +
                                "</center></html>",
                        m.getMasaNo(),
                        m.getKapasite(),
                        durumMetni));

                lblIcerik.setHorizontalAlignment(SwingConstants.CENTER);
                lblIcerik.setForeground(Color.black);

                btnMasa.add(lblIcerik, BorderLayout.CENTER);

                // Buton Stil Ayarları
                btnMasa.setBackground(butonRengi);
                btnMasa.setOpaque(true);
                btnMasa.setBorderPainted(false);
                btnMasa.setFocusable(false);

                // Hover Efekti
                btnMasa.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent evt) {
                        btnMasa.setBackground(butonRengi.darker());
                    }
                    @Override
                    public void mouseExited(MouseEvent evt) {
                        btnMasa.setBackground(butonRengi);
                    }
                });

                //sağ tıklama fonksiyonları
                JPopupMenu popupMenu = new JPopupMenu();
                // kapasite guncelleme
                JMenuItem itemKapasite = new JMenuItem("Kapasiteyi Güncelle");
                itemKapasite.addActionListener(e -> {
                    String input = JOptionPane.showInputDialog(this, "Yeni kapasite giriniz:", m.getKapasite());
                    if (input != null && !input.trim().isEmpty()) {
                        try {
                            int yeniKap = Integer.parseInt(input);
                            if(yeniKap > 0) {
                                masaService.masaKapasiteGuncelle(m.getMasaNo(), yeniKap);
                                masalariYenile();
                            } else {
                                JOptionPane.showMessageDialog(this, "Kapasite pozitif olmalı!");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Lütfen sayı giriniz!");
                        }
                    }
                });
                
                //masa durumu guncelleme
                JMenuItem itemAriza = new JMenuItem(isArizali ? "Arızayı Gider" : "Arızalı Olarak İşaretle");
                itemAriza.addActionListener(e -> {
                     if(isDolu) {
                         JOptionPane.showMessageDialog(this, "Dolu masa arızalı yapılamaz!");
                         return;
                     }
                     if(isArizali) {
                         masaService.masaDurumuGuncelle(m.getMasaNo(), MasaDurumu.BOS);
                     } else {
                         masaService.masaDurumuGuncelle(m.getMasaNo(), MasaDurumu.ARIZALI);
                     }
                     masalariYenile();
                });

                popupMenu.add(itemKapasite);
                popupMenu.add(itemAriza);
                btnMasa.setComponentPopupMenu(popupMenu);

                //sipariş paneline gitmemizi sağlar
                btnMasa.addActionListener(e -> {
                    if (isArizali) {
                        JOptionPane.showMessageDialog(this, "Arızalı masada işlem yapılamaz!");
                    } else {
                        new SiparisEkrani(m, this).setVisible(true);
                    }
                });

                panelMasalar.add(btnMasa);
            }
        }
        panelMasalar.revalidate();
        panelMasalar.repaint();
    }


}