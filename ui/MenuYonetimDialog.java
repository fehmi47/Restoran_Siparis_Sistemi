package ui;

import entity.Menu;
import entity.Enums.YemekKategori;
import service.MenuService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuYonetimDialog extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;
    private MenuService menuService;

    public MenuYonetimDialog(JFrame parent) {
        super(parent, "Menü Yönetimi", true);
        this.menuService = MenuService.getInstance();

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Yemek Adı", "Fiyat (TL)", "Kategori"};
        
        // Tablo modeli 
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücrelere çift tıklayıp değiştirmeyi kapattık, butonla yapacağız
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Tek seferde sadece 1 satır seçilsin
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- BUTON PANELI ---
        JPanel panelButonlar = new JPanel();
        
        JButton btnGuncelle = new JButton("Seçili Yemeği Güncelle");
        JButton btnSil = new JButton("Seçili Yemeği Sil");
        JButton btnKapat = new JButton("Kapat");

        // GÜNCELLEME BUTONU MANTIĞI
        btnGuncelle.addActionListener(e -> yemekGuncelleIslemi());

        // SİLME BUTONU MANTIĞI
        btnSil.addActionListener(e -> yemekSilIslemi());

        // KAPATMA
        btnKapat.addActionListener(e -> dispose());

        panelButonlar.add(btnGuncelle);
        panelButonlar.add(btnSil);
        panelButonlar.add(btnKapat);

        add(panelButonlar, BorderLayout.SOUTH);

        // Ekran açılınca listeyi doldur
        listeyiYenile();
    }

    private void listeyiYenile() {
        // Tabloyu temizle
        tableModel.setRowCount(0);

        // Servisten güncel listeyi çek
        List<Menu> menuListesi = menuService.getMenuListesi();

        // Tabloya satır satır ekle
        for (Menu m : menuListesi) {
            Object[] row = {
                m.getId(),
                m.getAd(),
                m.getFiyat(),
                m.getKategori()
            };
            tableModel.addRow(row);
        }
    }

    private void yemekSilIslemi() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek bir yemek seçiniz!");
            return;
        }

        
        int id = (int) table.getValueAt(selectedRow, 0);
        String yemekAdi = (String) table.getValueAt(selectedRow, 1);

        int onay = JOptionPane.showConfirmDialog(this, 
                yemekAdi + " adlı yemeği silmek istediğinize emin misiniz?", 
                "Silme Onayı", 
                JOptionPane.YES_NO_OPTION);

        if (onay == JOptionPane.YES_OPTION) {
            boolean silindi = menuService.yemekSil(id);
            if (silindi) {
                JOptionPane.showMessageDialog(this, "Yemek silindi.");
                listeyiYenile(); // Tabloyu güncelle
            } else {
                JOptionPane.showMessageDialog(this, "Hata: Yemek silinemedi.");
            }
        }
    }

    private void yemekGuncelleIslemi() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen düzenlenecek bir yemek seçiniz!");
            return;
        }

        // Seçili verileri al
        int id = (int) table.getValueAt(selectedRow, 0);
        String mevcutAd = (String) table.getValueAt(selectedRow, 1);
        double mevcutFiyat = (double) table.getValueAt(selectedRow, 2);
        YemekKategori mevcutKategori = (YemekKategori) table.getValueAt(selectedRow, 3);

        // --- GÜNCELLEME PENCERESİ TASARIMI ---
        JTextField txtAd = new JTextField(mevcutAd);
        JTextField txtFiyat = new JTextField(String.valueOf(mevcutFiyat));
        JComboBox<YemekKategori> cmbKategori = new JComboBox<>(YemekKategori.values());
        cmbKategori.setSelectedItem(mevcutKategori);

        Object[] message = {
            "Yemek Adı:", txtAd,
            "Fiyat (TL):", txtFiyat,
            "Kategori:", cmbKategori
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Yemeği Güncelle", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String yeniAd = txtAd.getText();
                double yeniFiyat = Double.parseDouble(txtFiyat.getText());
                YemekKategori yeniKategori = (YemekKategori) cmbKategori.getSelectedItem();

                // SERVICE ÇAĞRISI
                menuService.yemekGuncelle(id, yeniAd, yeniFiyat, yeniKategori);
                
                JOptionPane.showMessageDialog(this, "Güncelleme Başarılı!");
                listeyiYenile(); // Tabloyu hemen tazele

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir fiyat giriniz!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Bir hata oluştu: " + ex.getMessage());
            }
        }
    }
}