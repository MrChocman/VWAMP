import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.media.AudioSpectrumListener;
import java.io.File;

public class Reproductor {
    private static Reproductor instancia;
    private MediaPlayer mediaPlayer;
    private AudioSpectrumListener visualizadorListener; 

    private Reproductor() {}

    public static Reproductor getInstancia() {
        if (instancia == null) instancia = new Reproductor();
        return instancia;
    }

    public void cargarCancion(String ruta) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        try {
            Media media;
            if (ruta.startsWith("http")) {
                media = new Media(ruta); // Para URLs (Google Drive)
            } else {
                media = new Media(new File(ruta).toURI().toString()); // Para archivos locales
            }

            mediaPlayer = new MediaPlayer(media);

            // Reconectar el visualizador si existe
            if (visualizadorListener != null) {
                configurarVisualizadorInterno();
            }

        } catch (Exception e) {
            System.err.println("Error al cargar MP3: " + e.getMessage());
        }
    }

    public void play() {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    public void setVolumen(double volumen) {
        if (mediaPlayer != null) mediaPlayer.setVolume(volumen);
    }

    public void saltarA(double segundos) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(segundos));
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setAudioSpectrumListener(AudioSpectrumListener listener) {
        this.visualizadorListener = listener;
        if (mediaPlayer != null) {
            configurarVisualizadorInterno();
        }
    }
    
    private void configurarVisualizadorInterno() {
        mediaPlayer.setAudioSpectrumListener(visualizadorListener);
        mediaPlayer.setAudioSpectrumInterval(0.05);
        mediaPlayer.setAudioSpectrumNumBands(64);
        mediaPlayer.setAudioSpectrumThreshold(-60);
    }
}