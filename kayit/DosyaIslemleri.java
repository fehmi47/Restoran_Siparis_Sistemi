package kayit;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DosyaIslemleri {
    public static <T> void veriyiKaydet(String dosyaAdi, List<T> veriListesi) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dosyaAdi))) {
            oos.writeObject(veriListesi);
        } catch (IOException e) {
            System.out.println("Dosyaya veri kaydetme hatası: " + e.getMessage());

        }
    }


@SuppressWarnings("unchecked")
public static <T> List<T> verileriOku(String dosyaAdi) {
        List<T> liste = new ArrayList<>();
        File dosya = new File(dosyaAdi);

        if (!dosya.exists()) {
            return liste;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dosyaAdi))) {
            Object okunanNesne = ois.readObject();
            if (okunanNesne instanceof List) {
                liste = (List<T>) okunanNesne;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        return liste;
    }
}
