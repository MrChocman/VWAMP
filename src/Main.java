import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {

            System.err.println("No se pudo establecer Nimbus Look and Feel.");
        }
        

        SwingUtilities.invokeLater(() -> {
            MiReproductorApp app = new MiReproductorApp();
            app.setVisible(true);
        });
    }
}
