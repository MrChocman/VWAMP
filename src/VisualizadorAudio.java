import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javafx.scene.media.AudioSpectrumListener;


public class VisualizadorAudio extends JPanel implements AudioSpectrumListener {

    private final int bandas = 64; 
    private float[] magnitudes;    
    
    public VisualizadorAudio() {
        this.setBackground(new Color(28, 28, 28)); 
        this.magnitudes = new float[bandas];
        
        for (int i = 0; i < bandas; i++) {
            magnitudes[i] = -60;
        }
    }


    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        System.arraycopy(magnitudes, 0, this.magnitudes, 0, magnitudes.length);
        

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int anchoBarra = (ancho / bandas); 

        g2d.setColor(new Color(30, 215, 96));

        for (int i = 0; i < bandas; i++) {

            float magnitud = this.magnitudes[i];
            

            int alturaBarra = (int) ((magnitud + 60) * (alto / 60.0));
            
            if (alturaBarra < 2) alturaBarra = 2;
            if (alturaBarra > alto) alturaBarra = alto;

            int x = i * anchoBarra;
            int y = alto - alturaBarra;

            g2d.fillRect(x, y, anchoBarra - 1, alturaBarra);
        }
    }
}