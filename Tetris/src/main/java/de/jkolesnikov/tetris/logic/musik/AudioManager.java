package de.jkolesnikov.tetris.logic.musik;

import de.jkolesnikov.tetris.Main;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Die AudioManager Klasse ist für die Soundverwaltung zuständig.
 */
public class AudioManager {

    private static AudioManager instanz;
    private MediaPlayer mediaPlayer;
    private boolean abspielen;

    private AudioManager() {}

    public static synchronized AudioManager getInstanz() {
        if (instanz == null) instanz = new AudioManager();
        return instanz;
    }

    public void startMusik(String fileName) {
        if (!abspielen) {
            String pfad = Main.class.getResource(fileName).toString();
            Media media = new Media(pfad);

            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            abspielen = true;
        }
    }

    public void stopMusik() {
        if (abspielen) {
            mediaPlayer.stop();
            abspielen = true;
        }
    }
}




