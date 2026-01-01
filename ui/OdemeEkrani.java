package ui;

import entity.Siparis;
import entity.Enums.OdemeYontemi;
import service.OdemeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OdemeEkrani extends JDialog {

    private Siparis siparis;
    private boolean odemeYapildiMi = false; 

    public OdemeEkrani(JFrame parent, Siparis siparis) {
        super(parent, "Ödeme İşlemi - Masa " + siparis.getMasaNo(), true); // Modal
        this.siparis = siparis;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- ÜST KISIM  ---
        JPanel panelBilgi = new JPanel(new GridLayout(2, 1));
        panelBilgi.setBackground(new Color(245, 245, 245));
        panelBilgi.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel lblBaslik = new JLabel("ÖDENECEK TUTAR", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        double tutar = siparis.hesaplaToplamSiparisFiyati();
        JLabel lblTutar = new JLabel(tutar + " TL", SwingConstants.CENTER);
        lblTutar.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTutar.setForeground(new Color(220, 53, 69));

        panelBilgi.add(lblBaslik);
        panelBilgi.add(lblTutar);

        add(panelBilgi, BorderLayout.CENTER);

        // --- ALT KISIM  ---
        JPanel panelButonlar = new JPanel(new GridLayout(1, 2, 10, 10));
        panelButonlar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Nakit Butonu
        JButton btnNakit = new JButton("NAKİT");
        btnNakit.setBackground(new Color(40, 167, 69)); // Yeşil
        btnNakit.setForeground(Color.black);
        btnNakit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnNakit.addActionListener(e -> odemeTamamla(tutar, OdemeYontemi.NAKIT));

        // Kart Butonu
        JButton btnKart = new JButton("KREDİ KARTI");
        btnKart.setBackground(new Color(0, 123, 255)); // Mavi
        btnKart.setForeground(Color.black);
        btnKart.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnKart.addActionListener(e -> odemeTamamla(tutar, OdemeYontemi.KART));

        panelButonlar.add(btnNakit);
        panelButonlar.add(btnKart);

        add(panelButonlar, BorderLayout.SOUTH);
    }

    private void odemeTamamla(double tutar, OdemeYontemi yontem) {
        // Ödeme Service çağrılıyor
        OdemeService.getInstance().odemeAl(siparis.getMasaNo(), tutar, yontem);
        
        JOptionPane.showMessageDialog(this, "Ödeme başarıyla alındı.\nMüşteri oturmaya devam edebilir.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        
        this.odemeYapildiMi = true;
        dispose(); // Pencereyi kapat
    }

    public boolean isOdemeYapildiMi() {
        return odemeYapildiMi;
    }
}