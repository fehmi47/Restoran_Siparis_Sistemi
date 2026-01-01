package ui;

import entity.Enums.YemekKategori;
import service.MenuService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class YemekEkleDialog extends JDialog {

    private JTextField txtAd;
    private JTextField txtFiyat;
    private JComboBox<YemekKategori> cmbKategori;

    public YemekEkleDialog(JFrame parent) {
        super(parent, "Yeni Yemek/Ürün Ekle", true); // Modal pencere

        // --- PENCERE AYARLARI ---
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- ORTA PANEL ---
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 20));
        panelForm.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        panelForm.add(new JLabel("Yemek/Ürün Adı:"));
        txtAd = new JTextField();
        panelForm.add(txtAd);

        
        panelForm.add(new JLabel("Fiyat (TL):"));
        txtFiyat = new JTextField();
        panelForm.add(txtFiyat);

        panelForm.add(new JLabel("Kategori:"));
        
        cmbKategori = new JComboBox<>(YemekKategori.values());
        panelForm.add(cmbKategori);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelButonlar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButonlar.setBorder(new EmptyBorder(0, 10, 10, 10));

        JButton btnIptal = new JButton("İptal");
        btnIptal.addActionListener(e -> dispose());

        JButton btnKaydet = new JButton("Kaydet");
        btnKaydet.setBackground(new Color(40, 167, 69)); 
        btnKaydet.setForeground(Color.BLACK);
        btnKaydet.addActionListener(e -> kaydetIslemi());

        panelButonlar.add(btnIptal);
        panelButonlar.add(btnKaydet);

        add(panelButonlar, BorderLayout.SOUTH);
    }

    private void kaydetIslemi() {
        String ad = txtAd.getText().trim();
        String strFiyat = txtFiyat.getText().trim();
        YemekKategori secilenKategori = (YemekKategori) cmbKategori.getSelectedItem();

        if (ad.isEmpty() || strFiyat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen ürün adı ve fiyatını giriniz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double fiyat = Double.parseDouble(strFiyat);

            if (fiyat <= 0) {
                JOptionPane.showMessageDialog(this, "Fiyat 0'dan büyük olmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            MenuService.getInstance().yemekEkle(ad, fiyat, secilenKategori);

            JOptionPane.showMessageDialog(this, ad + " menüye başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            
            txtAd.setText("");
            txtFiyat.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Fiyat alanına geçerli bir sayı giriniz! (Örn: 150.50)", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Beklenmedik bir hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}