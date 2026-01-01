
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.AnaEkran;



public class Main {

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new AnaEkran().setVisible(true));
    }
}