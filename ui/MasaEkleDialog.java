package ui;

import entity.Masa;
import service.MasaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MasaEkleDialog extends JDialog {

    private JTextField txtMasaNo;
    private JTextField txtKapasite;
    private AnaEkran anaEkran; 

    public MasaEkleDialog(AnaEkran parent) {
        super(parent, "Yeni Masa Ekle", true);
        this.anaEkran = parent;

        // --- PENCERE AYARLARI ---
        setSize(350, 250);
        setLocationRelativeTo(parent); // Ana ekranın ortasında açıl
        setLayout(new BorderLayout());

        // --- ORTA PANEL (FORM ALANI) ---
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(new EmptyBorder(20, 20, 20, 20)); // Kenar boşlukları

        // Masa No
        panelForm.add(new JLabel("Masa Numarası:"));
        txtMasaNo = new JTextField();
        panelForm.add(txtMasaNo);

        // Kapasite
        panelForm.add(new JLabel("Kapasite (Kişi):"));
        txtKapasite = new JTextField();
        panelForm.add(txtKapasite);

        add(panelForm, BorderLayout.CENTER);

        // --- ALT PANEL (BUTONLAR) ---
        JPanel panelButonlar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButonlar.setBorder(new EmptyBorder(0, 10, 10, 10));

        JButton btnIptal = new JButton("İptal");
        btnIptal.addActionListener(e -> dispose()); // Pencereyi kapat

        JButton btnKaydet = new JButton("Kaydet");
        btnKaydet.setBackground(new Color(40, 167, 69)); 
        btnKaydet.setForeground(Color.black);
        
        // Kaydetme İşlemi
        btnKaydet.addActionListener(e -> kaydetIslemi());

        panelButonlar.add(btnIptal);
        panelButonlar.add(btnKaydet);

        add(panelButonlar, BorderLayout.SOUTH);
    }

    private void kaydetIslemi() {
        String strMasaNo = txtMasaNo.getText().trim();
        String strKapasite = txtKapasite.getText().trim();

        // 1. Boş Alan Kontrolü
        if (strMasaNo.isEmpty() || strKapasite.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurunuz!", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int masaNo = Integer.parseInt(strMasaNo);
            int kapasite = Integer.parseInt(strKapasite);

            // 2. Mantıksal Kontroller
            if (masaNo <= 0 || kapasite <= 0) {
                JOptionPane.showMessageDialog(this, "Masa no ve kapasite pozitif olmalıdır!", "Hata", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            Masa yeniMasa = MasaService.getInstance().masaEkle(masaNo, kapasite);

            if (yeniMasa != null) {
                JOptionPane.showMessageDialog(this, "Masa başarıyla eklendi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                
                if (anaEkran != null) {
                    anaEkran.masalariYenile();
                }
                
                dispose(); // Pencereyi kapat
            } else {
                JOptionPane.showMessageDialog(this, "Bu numaralı masa zaten mevcut!", "Hata", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen sayısal değer giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}